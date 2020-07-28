package com.jimang.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.fashionbrot.validated.annotation.NotEmpty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Auther:wind
 * @Date:2020/7/25
 * @Version 1.0
 */
@Data
@ApiModel
public class UserInfoUpdateParam {
    @ApiModelProperty("昵称")
    @JsonProperty("nick_name")
    @NotEmpty
    private String nickName;

}
