package com.jimang.util;

import com.github.fashionbrot.validated.annotation.Email;
import com.github.fashionbrot.validated.annotation.Phone;

import java.util.regex.Pattern;

/**
 * @Auther:wind
 * @Date:2020/7/25
 * @Version 1.0
 */
public class ValidationUtil {

    public static Boolean isPhone(String phone) {
        String regexp = "^(((13[0-9])|(14[579])|(15[^4,\\D])|(18[0-9])|(17[0-9]))|(19[8,9])|166)\\d{8}$";
        Pattern pattern = Pattern.compile(regexp);
        return pattern.matcher(phone).matches();
    }

    public static Boolean isEmail(String email) {
        String regexp = "^[A-Za-z0-9\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
        Pattern pattern = Pattern.compile(regexp);
        return pattern.matcher(email).matches();
    }


}
