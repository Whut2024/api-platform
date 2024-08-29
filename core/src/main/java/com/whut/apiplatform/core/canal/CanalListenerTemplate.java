package com.whut.apiplatform.core.canal;

import java.util.List;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.CanalEntry.Column;
import com.alibaba.otter.canal.protocol.CanalEntry.Entry;
import com.alibaba.otter.canal.protocol.CanalEntry.EntryType;
import com.alibaba.otter.canal.protocol.CanalEntry.EventType;
import com.alibaba.otter.canal.protocol.CanalEntry.RowChange;
import com.alibaba.otter.canal.protocol.CanalEntry.RowData;
import lombok.extern.slf4j.Slf4j;

/**
 * @author whut2024
 * @since 2024-08-29
 */
@Slf4j
public abstract class CanalListenerTemplate {




    /**
     * 调用方法，处理传入的Entry对象。
     *
     * @param entry 包含一行数据变化的Entry对象
     * @throws RuntimeException 如果解析RowChange对象失败
     */
    public void invoke(Entry entry) {
        //RowChange对象，包含了一行数据变化的所有特征
        //比如isDdl 是否是ddl变更操作 sql 具体的ddl sql beforeColumns afterColumns 变更前后的数据字段等等
        RowChange rowChage;
        try {
            rowChage = RowChange.parseFrom(entry.getStoreValue());
        } catch (Exception e) {
            throw new RuntimeException("ERROR ## parser of eromanga-event has an error , data:" + entry, e);
        }
        //获取操作类型：insert/update/delete类型
        final EventType eventType = rowChage.getEventType();

        //打印Header信息
        final CanalEntry.Header entryHeader = entry.getHeader();
        log.info("================》; binlog[{}:{}] , name[{},{}] , eventType : {}",
                entryHeader.getLogfileName(), entryHeader.getLogfileOffset(),
                entryHeader.getSchemaName(), entryHeader.getTableName(),
                eventType);

        //判断是否是DDL语句
        if (rowChage.getIsDdl()) {
            System.out.println("================》;isDdl: true,sql:" + rowChage.getSql());
            return;
        }


        //获取RowChange对象里的每一行数据，打印出来
        for (RowData rowData : rowChage.getRowDatasList()) {

            final List<Column> beforeColumnsList = rowData.getBeforeColumnsList();
            final List<Column> afterColumnsList = rowData.getAfterColumnsList();

            //如果是删除语句
            if (eventType == EventType.DELETE)
                deleteOperation(beforeColumnsList);
                //如果是新增语句
            else if (eventType == EventType.INSERT)
                insertOperation(afterColumnsList);
                //如果是更新的语句
            else
                updateOperation(beforeColumnsList, afterColumnsList);

        }
    }


    /**
     * 插入操作函数
     *
     * @param afterColumnList 插入操作后的列列表
     */
    public void insertOperation(List<Column> afterColumnList) {

    }


    /**
     * 更新操作函数
     *
     * @param beforeColumnList 更新操作前的列列表
     * @param afterColumnList 更新操作后的列列表
     */
    public void updateOperation(List<Column> beforeColumnList, List<Column> afterColumnList) {}


    /**
     * 删除操作函数
     *
     * @param beforColumnList 删除操作前的列列表
     */
    public void deleteOperation(List<Column> beforColumnList) {}


    /**
     * 执行查询操作
     *
     * @param columnList 包含查询列信息的列表
     */
    public void selectOperation(List<Column> columnList) {}



    /*private void printColumn(List<Column> columns) {
        for (Column column : columns) {
            System.out.println(column.getName() + " : " + column.getValue() + "    update=" + column.getUpdated());
        }
    }*/


    /**
     * subclass can overwrite it to implement sorting.
     * this class's method (invoke) has higher priority to be invoked if this method returning value is smaller
     */
    public int getOrder() {
        return 0;
    }
}

//================》; binlog[binlog.000070:5154] , name[canal01,user01] , eventType : INSERT
//id : 1    update=true
//username : a    update=true
//password : a    update=true
