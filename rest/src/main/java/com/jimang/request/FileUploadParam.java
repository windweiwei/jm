package com.jimang.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Auther:wind
 * @Date:2020/7/22
 * @Version 1.0
 */
@Data
public class FileUploadParam {
    /**
     * 文件形式
     */
    private MultipartFile file;
    /**
     * url
     */
    private String url;
}
