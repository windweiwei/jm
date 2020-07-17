package com.tt.sns.service.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tt.sns.enums.ResponseEnum;
import com.tt.sns.mapper.PushConfigMapper;
import com.tt.sns.mapper.PushTokensMapper;
import com.tt.sns.model.Alarm;
import com.tt.sns.model.PushToken;
import com.tt.sns.request.PushTokenSaveParam;
import com.tt.sns.response.BaseResponse;
import com.tt.sns.service.Push;
import com.tt.sns.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

import com.tt.sns.model.PushConfig;
import com.tt.sns.model.PushMessage;
import com.tt.sns.result.PushResult;
import com.tt.sns.service.PushService;
import com.tt.sns.util.PushFactory;

import java.util.Date;
import java.util.List;
import java.util.Optional;


/**
 * Created by wind on 2020/7/8.
 */
@Service
@Slf4j
public class PushServiceImpl implements PushService {
    @Autowired
    private PushTokensMapper pushTokensMapper;

    @Autowired
    private PushConfigMapper pushConfigMapper;
    private PushConfig conf;

    @Override
    public BaseResponse savePushToken(PushTokenSaveParam param, HttpServletRequest request) {
        BaseResponse response = new BaseResponse();
        //todo 1、判断token是否存在。 跟新token 防止
        QueryWrapper<PushToken> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("token", param.getToken());
        PushToken pushToken = pushTokensMapper.selectOne(queryWrapper);
        if (null == pushToken) {
            pushToken = new PushToken();
            Date now = new Date();
            BeanUtils.copyProperties(param, pushToken);
            pushToken.setLastPushTime(now);
            pushToken.setDeleted(0);
            pushToken.setMtime(now);
            pushToken.setCtime(now);
            pushTokensMapper.insert(pushToken);
        }
        return ResponseUtil.getBaseResponse(response, ResponseEnum.SUCCESS);
    }


    @Override
    public BaseResponse acceptAndPush(Alarm alarm, HttpServletRequest request) {
        BaseResponse response = new BaseResponse();
        QueryWrapper<PushToken> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid", alarm.getUid());
        List<PushToken> pushTokens = pushTokensMapper.selectList(queryWrapper);
        QueryWrapper<PushConfig> configQueryWrapper = new QueryWrapper<>();
        List<PushConfig> pushConfigs = pushConfigMapper.selectList(configQueryWrapper);
        for (PushToken pushToken : pushTokens) {
            Optional<PushConfig> optional = pushConfigs.stream()
                    .filter(pushConfig -> pushConfig.getPushType().equals(pushToken.getPushType())).findFirst();

            if (optional.isPresent()) {
                PushConfig config = optional.get();
                Push push = PushFactory.createPush(config);
                if (push != null) {
                    PushMessage message = new PushMessage();
                    message.setTitle(alarm.getTitle());
                    message.setMessage(alarm.getMsg());
                    PushResult result = push.push(pushToken.getToken(), message, alarm.getAppVersion());
                    //不成功就把那个删掉
                    if (!result.isSuccess()) {
                        QueryWrapper deleteWrapper = new QueryWrapper();
                        deleteWrapper.eq("token", pushToken.getToken());
                        pushTokensMapper.delete(deleteWrapper);
                    }
                }
            }

        }

        return ResponseUtil.getBaseResponse(response, ResponseEnum.SUCCESS);
    }

    @Override
    public BaseResponse savePushConfig(PushConfig pushConfig, HttpServletRequest request) {
        log.info("1");
        BaseResponse response=new BaseResponse();
        if (pushConfig == null || pushConfig.getPushType() == null
                || pushConfig.getApiKey() == null
                || pushConfig.getApiSecret() == null
                || pushConfig.getPackname() == null) {
            return ResponseUtil.getBaseResponse(response, ResponseEnum.INVALID_PARAMS);
        }
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("push_type",pushConfig.getPushType());
        queryWrapper.eq("firm_id",pushConfig.getFirmId());
       PushConfig config=pushConfigMapper.selectOne(queryWrapper);
        if (null != config) {
            return ResponseUtil.getBaseResponse(response, ResponseEnum.CONFIG_ALREADY_EXIT);
        }
        pushConfig.setMtime(new Date());
        pushConfig.setCtime(new Date());
        pushConfig.setDeleted(0);
        pushConfigMapper.insert(pushConfig);
        return ResponseUtil.getBaseResponse(response, ResponseEnum.SUCCESS);
    }
}
