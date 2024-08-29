package com.whut.apiplatform.core.job;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import com.alibaba.otter.canal.protocol.exception.CanalClientException;
import com.whut.apiplatform.constant.InterfaceInfoConstant;
import com.whut.apiplatform.core.canal.CanalListenerTemplate;
import com.whut.apiplatform.core.canal.interfaceinfo.InterfaceInfoCanalListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * {@link CanalListenerTemplate}
 * {@link CanalConnector}
 * @author whut2024
 * @since 2024-08-29
 */
@Slf4j
@Component
public class ListenCanalJob {


    private final CanalConnector connector;


    private final Map<String, List<? extends CanalListenerTemplate>> canalListenerTemplateListMap = new HashMap<>();



    public ListenCanalJob(CanalConnector connector, List<InterfaceInfoCanalListener> interfaceInfoCanalListenerList) {
        this.connector = connector;

        interfaceInfoCanalListenerList.sort(Comparator.comparingInt(CanalListenerTemplate::getOrder));
        canalListenerTemplateListMap.put(InterfaceInfoConstant.TABLE_NAME, interfaceInfoCanalListenerList);
    }

    /**
     * 监听函数，定时执行，用于获取并处理Canal中的数据。
     *
     * @throws RuntimeException 当Canal客户端异常时，抛出运行时异常。
     */
    @Scheduled(fixedRate = 2000L, initialDelay = 1000L)
    void listen() {
        try {
            // 获取指定数量的数据
            final int BATCH_SIZE = 1024;
            final Message message = connector.getWithoutAck(BATCH_SIZE);

            //获取批量ID
            final long batchId = message.getId();

            //进行 batch id 的确认。确认之后，小于等于此 batchId 的 Message 都会被确认。
            connector.ack(batchId);

            //获取批量的数量
            final List<CanalEntry.Entry> entryList = message.getEntries();
            int size = entryList.size();

            //如果没有数据
            if (batchId == -1 || size == 0)
                return;

            //如果有数据,处理数据
            for (CanalEntry.Entry entry : entryList) {
                if (entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONBEGIN || entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONEND) {
                    //开启/关闭事务的实体类型，跳过
                    continue;
                }

                // 获取表名称
                final String tableName = entry.getHeader().getTableName();
                List<? extends CanalListenerTemplate> canalListenerTemplateList = canalListenerTemplateListMap.get(tableName);
                for (CanalListenerTemplate canalListenerTemplate : canalListenerTemplateList) {
                    canalListenerTemplate.invoke(entry);
                }

            }
        } catch (CanalClientException e) {
            connector.disconnect();
            throw new RuntimeException(e);
        }
    }

}
