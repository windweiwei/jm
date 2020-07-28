package com.jimang.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @Auther:wind
 * @Date:2020/7/28
 * @Version 1.0
 */
public class ChannelImages {
    @JsonProperty("channel_no")
    private Integer channelNo;


    @JsonProperty("image_url")
    private String imageUrl;

}