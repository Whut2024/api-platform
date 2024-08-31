package com.whut.apiplatform.core.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whut.apiplatform.core.mapper.InterfaceInfoMapper;
import com.whut.apiplatform.core.utils.SqlUtils;
import com.whut.apiplatform.core.utils.UserHolder;
import com.whut.apiplatform.model.dto.interfaceinfo.InterfaceInfoPageRequest;
import com.whut.apiplatform.model.dto.interfaceinfo.InterfaceInfoUpdateRequest;
import com.whut.apiplatform.model.entity.InterfaceInfo;
import com.whut.apiplatform.model.entity.User;
import com.whut.apiplatform.model.entity.UserInterfaceInfo;
import com.whut.apiplatform.model.enums.UserRoleEnum;
import com.whut.apiplatform.service.InterfaceInfoService;
import com.whut.apiplatform.service.UserInterfaceInfoService;
import com.whut.apiplatform.starter.RequestClient;
import com.whut.common.DeleteRequest;
import com.whut.common.constant.CommonConstant;
import com.whut.webs.exception.ErrorCode;
import com.whut.webs.exception.ThrowUtils;
import lombok.AllArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.whut.apiplatform.constant.InterfaceInfoConstant.*;

/**
 * @author laowang
 * @description 针对表【interface_info(接口信息表)】的数据库操作Service实现
 * @createDate 2024-08-24 20:59:24
 */
@DubboService
@AllArgsConstructor
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
        implements InterfaceInfoService {


    private final UserInterfaceInfoService userInterfaceInfoService;


    private final StringRedisTemplate redisTemplate;


    private final RequestClient requestClient;


    private final RedissonClient redissonClient;


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

    @Override
    public QueryWrapper<InterfaceInfo> getPageWrapper(InterfaceInfoPageRequest interfaceInfoPageRequest) {
        final QueryWrapper<InterfaceInfo> wrapper = new QueryWrapper<>();

        final Long id = interfaceInfoPageRequest.getId();
        final String name = interfaceInfoPageRequest.getName();
        final String url = interfaceInfoPageRequest.getUrl();
        final String description = interfaceInfoPageRequest.getDescription();
        final String method = interfaceInfoPageRequest.getMethod();
        final String requestParam = interfaceInfoPageRequest.getRequestParam();
        final String requestHeader = interfaceInfoPageRequest.getRequestHeader();
        final String requestBody = interfaceInfoPageRequest.getRequestBody();
        final String responseHeader = interfaceInfoPageRequest.getResponseHeader();
        final String responseBody = interfaceInfoPageRequest.getResponseBody();
        final String status = interfaceInfoPageRequest.getStatus();
        final String sortField = interfaceInfoPageRequest.getSortField();
        final String sortOrder = interfaceInfoPageRequest.getSortOrder();
        final Long latestId = interfaceInfoPageRequest.getLatestId();


        if (id != null) {
            wrapper.eq("id", id);
            return wrapper;
        } else if (latestId != null)
            wrapper.gt("id", latestId);

        wrapper.eq(StrUtil.isNotBlank(url), "url", url);
        wrapper.eq(StrUtil.isNotBlank(name), "name", name);
        wrapper.eq(StrUtil.isNotBlank(description), "description", description);
        wrapper.eq(StrUtil.isNotBlank(method), "method", method);
        wrapper.eq(StrUtil.isNotBlank(requestHeader), "request_header", requestHeader);
        wrapper.eq(StrUtil.isNotBlank(requestParam), "request_param", requestParam);
        wrapper.eq(StrUtil.isNotBlank(requestHeader), "response_header", responseHeader);
        wrapper.eq(StrUtil.isNotBlank(requestBody), "request_body", requestBody);
        wrapper.eq(StrUtil.isNotBlank(responseBody), "response_body", responseBody);
        wrapper.eq(StrUtil.isNotBlank(status), "status", status);


        User user = UserHolder.get();
        if (!UserRoleEnum.isAdmin(user)) {
            List<UserInterfaceInfo> userInterfaceInfoList = userInterfaceInfoService.list(new LambdaQueryWrapper<>(UserInterfaceInfo.class).eq(UserInterfaceInfo::getUserId, user.getId()));

            if (CollectionUtil.isNotEmpty(userInterfaceInfoList)) {
                List<Long> idList = userInterfaceInfoList.stream().map(UserInterfaceInfo::getInterfaceInfoId).collect(Collectors.toList());

                wrapper.in("id", idList);
            } else
                wrapper.eq("id", -1);
        }


        wrapper.orderBy(SqlUtils.validSortField(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);

        return wrapper;
    }

    @Override
    public Boolean updateInterfaceInfo(InterfaceInfoUpdateRequest interfaceInfoUpdateRequest) {
        final Long id = interfaceInfoUpdateRequest.getId();

        // is admin ?
        User user = UserHolder.get();

        checkRole(user, id);

        // update
        InterfaceInfo interfaceInfo = BeanUtil.copyProperties(interfaceInfoUpdateRequest, InterfaceInfo.class);
        return this.updateById(interfaceInfo);
    }

    @Override
    public Boolean deleteInterfaceInfo(DeleteRequest deleteRequest) {
        final Long id = deleteRequest.getId();

        User user = UserHolder.get();
        checkRole(user, id);

        return this.removeById(id);
    }

    @Override
    public Boolean checkExistenceById(String id) {
        // select from redis cache 
        final String cacheKey = INTERFACE_INFO_STATUS_KEY + id;
        String status = redisTemplate.opsForValue().get(cacheKey);

        // it exists: check whether the interface info is online
        if (StrUtil.isNotBlank(status)) {
            redisTemplate.expire(cacheKey, STATUS_TTL, TimeUnit.MINUTES);
            return ONLINE.equals(status);
        }


        final RLock lock = redissonClient.getLock(INTERFACE_INFO_STATUS_LOCK_KEY + id);

        while (true) {
            final boolean locked = lock.tryLock();
            if (!locked) {
                try {
                    Thread.sleep(10L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                continue;
            }

            status = redisTemplate.opsForValue().get(cacheKey);
            if (StrUtil.isNotBlank(status)) {
                redisTemplate.expire(cacheKey, STATUS_TTL, TimeUnit.MINUTES);
                return ONLINE.equals(status);
            }


            // else: select from MySQL and cache it
            final LambdaQueryWrapper<InterfaceInfo> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(InterfaceInfo::getId, id);

            final String selectedStatus = this.baseMapper.selectStatus(Long.parseLong(id));
            redisTemplate.opsForValue().set(cacheKey, selectedStatus, STATUS_TTL, TimeUnit.MINUTES);

            return ONLINE.equals(selectedStatus);
        }
    }

    @Override
    public String getIdAndUrlStr(Long latestId) {
        List<InterfaceInfo> interfaceInfoList = this.baseMapper.getIdAndUrlList(latestId);

        StringBuilder builder = new StringBuilder();
        final int size = interfaceInfoList.size();
        for (int i = 0; i < size; i++) {
            InterfaceInfo interfaceInfo = interfaceInfoList.get(i);
            builder.append(interfaceInfo.getId()).append(' ').append(interfaceInfo.getUrl());

            if (i != size - 1) builder.append(' ');
        }

        return builder.toString();
    }

    @Override
    public Boolean offlineInterfaceInfo(Long id) {
        final User user = UserHolder.get();
        checkRole(user, id);

        final LambdaUpdateWrapper<InterfaceInfo> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(InterfaceInfo::getId, id);

        InterfaceInfo interfaceInfo = new InterfaceInfo();
        interfaceInfo.setStatus(OFFLINE);
        interfaceInfo.setId(id);

        return this.updateById(interfaceInfo);
    }

    @Override
    public Boolean onlineInterfaceInfo(Long id) {
        final User user = UserHolder.get();
        checkRole(user, id);

        final LambdaUpdateWrapper<InterfaceInfo> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(InterfaceInfo::getId, id);

        InterfaceInfo interfaceInfo = new InterfaceInfo();
        interfaceInfo.setStatus(ONLINE);
        interfaceInfo.setId(id);

        return this.updateById(interfaceInfo);
    }

    @Override
    public Object invoke(Long id, String requestParamJson, String requestHeaderJson, String requestBodyStr) {
        final InterfaceInfo interfaceInfo = this.baseMapper.getMethodAndUrl(id);
        final URL url = URLUtil.url(interfaceInfo.getUrl());

        final Map<String, String> paramMap = JSONUtil.toBean(requestParamJson, new TypeReference<Map<String, String>>() {
        }, false);
        final Map<String, List<String>> headerMap = JSONUtil.toBean(requestHeaderJson, new TypeReference<Map<String, List<String>>>() {
        }, false);

        return requestClient.invoke(id, url.getHost(), String.valueOf(url.getPort()), interfaceInfo.getMethod(), paramMap, headerMap, requestBodyStr);
    }


    public void checkRole(User user, Long interfaceInfoId) {
        if (!UserRoleEnum.isAdmin(user)) {
            // whether it contains this

            final UserInterfaceInfo userInterfaceInfo = userInterfaceInfoService.getUserIdByInterfaceInfoId(interfaceInfoId);

            ThrowUtils.throwIf(userInterfaceInfo == null, ErrorCode.PARAMS_ERROR, "接口信息不存在");
            ThrowUtils.throwIf(user.getId().equals(userInterfaceInfo.getUserId()), ErrorCode.NO_AUTH_ERROR);
        }
    }
}




