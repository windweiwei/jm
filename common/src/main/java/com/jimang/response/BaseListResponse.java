package com.jimang.response;

import java.util.List;

/**
 * Created by wind on 2020/7/8.
 */
public class BaseListResponse<T> {
    private Integer code;
    private String msg;
    private List<T> list;
    private Integer count;
}
