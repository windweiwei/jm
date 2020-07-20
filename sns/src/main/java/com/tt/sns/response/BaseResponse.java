package com.tt.sns.response;

import com.jimang.enums.ResponseEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by wind on 2020/7/8.
 */
@Data
@ApiModel
public class BaseResponse<T> {
    @ApiModelProperty(value = "返回值code 2000：成功，其他具体看错误返回码说明")
    private Integer code;
    @ApiModelProperty(value = "返回信息描述 成功：OK, 具体看场景")
    private String msg;

    private T data;


}
