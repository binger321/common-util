package com.binger.common.security;

import lombok.Data;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: zhuyubin
 * Date: 2018/5/7
 * Time: 上午11:32
 * To change this template use File | Settings | File Templates.
 * Description:
 */
@Data
public class CompositePrincipal implements Serializable{

    public CompositePrincipal(){}

    /**基础数据对象*/
    private BasicUserPrincipal basicUser = new BasicUserPrincipal();

    /**基础账户数据*/
    @Data
    public static class BasicUserPrincipal implements Serializable{
        public BasicUserPrincipal(){}
        /**
         * ID
         */
        private Integer userId;

        /**
         * 用户代码
         */
        private String userCode;

        /**
         * 用户名称
         */
        private String userName;

        /**
         * 对应人员(冗余)
         */
        private String personName;

        /**
         * 人员表id
         */
        private Integer personId;

    }


}
