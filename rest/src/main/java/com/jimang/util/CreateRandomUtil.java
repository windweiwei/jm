package com.jimang.util;

/**
 * @Auther:wind
 * @Date:2020/7/19
 * @Version 1.0
 */
public class CreateRandomUtil {
    public static Integer getSixRandom() {

        Integer result = (int) (Math.random() * (999999 - 100000 + 1) + 100000);

        return result;

    }
}
