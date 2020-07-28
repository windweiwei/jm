package com.jimang.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @Auther:wind
 * @Date:2020/7/25
 * @Version 1.0
 */
@ApiModel
@Data
public class CreateUserParam {
    @JsonProperty("username")
    private String userName;
    private String password;
    @JsonProperty("user_type")
    private Integer userType;
    @JsonProperty("firm_id")
    private String firmId;
}
