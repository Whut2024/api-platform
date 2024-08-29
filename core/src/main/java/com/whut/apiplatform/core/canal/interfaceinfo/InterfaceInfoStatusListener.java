package com.whut.apiplatform.core.canal.interfaceinfo;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.whut.apiplatform.constant.InterfaceInfoConstant;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author whut2024
 * @since 2024-08-29
 */

@Component
@AllArgsConstructor
public class InterfaceInfoStatusListener extends InterfaceInfoCanalListener {


    private final StringRedisTemplate redisTemplate;


    @Override
    public void insertOperation(List<CanalEntry.Column> afterColumnList) {
        final long id = Long.parseLong(afterColumnList.get(0).getValue());

        deleteCacheStatus(id);
    }

    @Override
    public void updateOperation(List<CanalEntry.Column> beforeColumnList, List<CanalEntry.Column> afterColumnList) {
        final String beforeStatus = beforeColumnList.get(10).getValue();
        final String afterStatus = afterColumnList.get(10).getValue();
        final long id = Long.parseLong(afterColumnList.get(0).getValue());

        if (!beforeStatus.equals(afterStatus))
            deleteCacheStatus(id);

    }

    @Override
    public void deleteOperation(List<CanalEntry.Column> beforColumnList) {
        final long id = Long.parseLong(beforColumnList.get(0).getValue());

        deleteCacheStatus(id);
    }


    private void deleteCacheStatus(Long id) {
        String cacheKey = InterfaceInfoConstant.INTERFACE_INFO_STATUS_KEY + id;

        redisTemplate.delete(cacheKey);
    }
}
