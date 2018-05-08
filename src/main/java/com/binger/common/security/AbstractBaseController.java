package com.binger.common.security;

import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletResponse;

/**
 * 默认controller基础类
 *
 */
public class AbstractBaseController {

    private CompositePrincipal.BasicUserPrincipal getBasicPricipal(){
        return MySecurityContextHolder.getCompositePrincipal().getBasicUser();
    }

    /**获取登录对象账户id*/
    protected Integer getLoginUserId(){
        return getBasicPricipal()==null?null:getBasicPricipal().getUserId();
    }

    /**获取登录对象账户名*/
    protected String getLoginUserName(){
        return getBasicPricipal()==null?null:getBasicPricipal().getUserName();
    }

    /**获取登录对象人员表id*/
    protected Integer getLoginPersonId(){
        return getBasicPricipal()==null?null:getBasicPricipal().getPersonId();
    }

    /**获取登录对象人员姓名, e.g.张三*/
    protected String getLoginPersonName(){
        return getBasicPricipal()==null?null:getBasicPricipal().getPersonName();
    }

    /**获取复合授权对象*/
    protected CompositePrincipal getPrincipal(){
        return MySecurityContextHolder.getCompositePrincipal();
    }

    protected ThreadLocal<HttpServletResponse> responseThreadLocal = new ThreadLocal<>();

    @ModelAttribute
    protected void setResponse(HttpServletResponse response) {
        responseThreadLocal.set(response);
    }

    protected HttpServletResponse getResponse(){
        return responseThreadLocal.get();
    }

}
