package com.binger.common.redis;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * User: zhuyubin
 * Date: 2018/5/16
 * Time: 下午3:36
 * To change this template use File | Settings | File Templates.
 * Description: 分布式锁
 */
@Data
public class Lock {

    private String name;

    private String value;

    public Lock() {
    }

    public Lock(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
