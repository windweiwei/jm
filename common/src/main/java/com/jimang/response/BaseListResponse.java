package com.jimang.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Created by wind on 2020/7/8.
 */
@Data
@ApiModel
public class BaseListResponse<T> {
    @ApiModelProperty(value = "code")
    private Integer code;
    @ApiModelProperty(value = "结果msg")
    private String msg;
    @ApiModelProperty(value = "object 集合")
    private List<T> list;
    @ApiModelProperty("数量")
    private Integer count;
}
