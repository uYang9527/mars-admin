package com.mars.admin.framework.anno;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 代码领取微信公众号【程序员Mars】
 *
 * @className: Main
 * @author: Mars
 * @date: 2025/7/8 22:21
 */
public class Main {


    public static void main(String[] args) {

        List<String> list = Arrays.asList("1", "2", "3");

        // todo 采用lambda stream的方式获取大于1的元素

        List<String> result = list.stream()
                .filter(x -> Integer.parseInt(x) > 1)
                .collect(Collectors.toList());
        System.out.println(result);


        HashMap<String, String> map = new HashMap<>();

        map.put("name", "lisi");

        map.forEach((k, v) -> {
            System.out.println(k);
        });


    }

}
