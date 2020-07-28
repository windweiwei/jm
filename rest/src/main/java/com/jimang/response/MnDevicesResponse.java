package com.jimang.response;

import com.jimang.model.Device;
import lombok.Data;

import java.util.List;

/**
 * @Auther:wind
 * @Date:2020/7/27
 * @Version 1.0
 */
@Data
public class MnDevicesResponse {
    private Integer code;
    private String msg;
    private List<Device> devices;
}
