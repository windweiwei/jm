package com.tt.sns.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Auther:wind
 * @Date:2020/7/18
 * @Version 1.0
 */
@Data
@ApiModel
public class PushTokenSaveListParam {
    /**
     * token param 注册
     */
    @ApiModelProperty(value = "注册token的list集合，批量注册")
    List<PushTokenSaveParam> params;

}
