package com.jimang.util;

import com.jimang.enums.ResponseEnum;
import com.jimang.response.BaseListResponse;
import com.jimang.response.BaseResponse;

public class ResponseUtil {

    public static BaseResponse getBaseResponse(BaseResponse response, ResponseEnum responseEnum) {
        response.setCode(responseEnum.getCode());
        response.setMsg(responseEnum.getMsg());
        return response;
    }

    public static BaseListResponse getListBaseResponse(BaseListResponse response, ResponseEnum responseEnum) {
        response.setCode(responseEnum.getCode());
        response.setMsg(responseEnum.getMsg());
        return response;
    }
}