package com.tt.sns.util;


import com.jimang.enums.ResponseEnum;
import com.tt.sns.response.BaseResponse;

/**
 * Created by wind on 2020/7/9.
 */
public class ResponseUtil {

    public static BaseResponse getBaseResponse(BaseResponse response, ResponseEnum responseEnum) {
        response.setCode(responseEnum.getCode());
        response.setMsg(responseEnum.getMsg());
        return response;
    }
}
