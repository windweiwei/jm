package com.tt.sns.response;

import lombok.Data;

/**
 * Created by wind on 2020/7/8.
 */
@Data
public class BaseResponse<T> {

    private Integer code;

    private String msg;

    private T data;

}
