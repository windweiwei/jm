package com.tt.sns.model;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

/**
 * Created by wind on 2020/7/8.
 */
@Data
@TableName("push_tokens")
public class PushToken {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @TableField("user_id")
    private Long userId;

    /**
     * 设备id
     */
    @TableField("uid")
    private String uid;

    @TableField("token")
    private String token;

    /**
     * 推送
     */
    @TableField("push_type")
    @JsonProperty("push_type")
    private Integer pushType;

    /**
     * 上一次推送时间
     */
    @TableField("last_push_time")
    private Date lastPushTime;


    @TableField("deleted")
    @TableLogic  //逻辑删除注解
    @JsonIgnore  //忽略
    private Integer deleted;

    @TableField("ctime")
    private Date ctime;

    @TableField("mtime")
    private Date mtime;

}
