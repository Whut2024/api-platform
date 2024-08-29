package com.whut.apiplatform.core.canal.interfaceinfo;

import cn.hutool.core.util.StrUtil;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.whut.apiplatform.service.RouteService;
import lombok.AllArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author whut2024
 * @since 2024-08-29
 */

@AllArgsConstructor
@Component
public class InterfaceInfoRouteListener extends InterfaceInfoCanalListener {


    @DubboReference
    private final RouteService routeService;


    @Override
    public void insertOperation(List<CanalEntry.Column> afterColumnList) {
        routeService.add(afterColumnList.get(0).getValue(), afterColumnList.get(2).getValue());
    }


    @Override
    public void updateOperation(List<CanalEntry.Column> beforeColumnList, List<CanalEntry.Column> afterColumnList) {
        final String beforeValue = beforeColumnList.get(2).getValue();
        final String afterValue = afterColumnList.get(2).getValue();

        if (!StrUtil.isAllNotBlank(beforeValue, afterValue) || beforeValue.equals(afterValue))
            return;

        routeService.update(afterColumnList.get(0).getValue(), afterValue);
    }


    @Override
    public void deleteOperation(List<CanalEntry.Column> beforColumnList) {
        routeService.delete(beforColumnList.get(0).getValue());
    }
}
