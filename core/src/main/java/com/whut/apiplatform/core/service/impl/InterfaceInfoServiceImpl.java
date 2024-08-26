package com.whut.apiplatform.core.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whut.apiplatform.core.mapper.InterfaceInfoMapper;
import com.whut.apiplatform.model.entity.InterfaceInfo;
import com.whut.apiplatform.model.entity.User;
import com.whut.apiplatform.model.entity.UserInterfaceInfo;
import com.whut.apiplatform.service.InterfaceInfoService;
import com.whut.apiplatform.service.UserInterfaceInfoService;
import com.whut.webs.exception.ErrorCode;
import com.whut.webs.exception.ThrowUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
* @author laowang
* @description 针对表【interface_info(接口信息表)】的数据库操作Service实现
* @createDate 2024-08-24 20:59:24
*/
@Service
@AllArgsConstructor
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
    implements InterfaceInfoService{


    private final UserInterfaceInfoService userInterfaceInfoService;


    @Override
    public void saveInterfaceInfo(InterfaceInfo interfaceInfo, User user) {
        boolean saved = this.save(interfaceInfo);
        ThrowUtils.throwIf(!saved, ErrorCode.OPERATION_ERROR, "请重试");

        UserInterfaceInfo userInterfaceInfo = new UserInterfaceInfo();
        userInterfaceInfo.setInterfaceInfoId(interfaceInfo.getId());
        userInterfaceInfo.setUserId(user.getId());
        userInterfaceInfo.setLeftNum(0);
        userInterfaceInfo.setTotalNum(0);

        saved = userInterfaceInfoService.save(userInterfaceInfo);
        ThrowUtils.throwIf(!saved, ErrorCode.OPERATION_ERROR, "请重试");
    }
}




