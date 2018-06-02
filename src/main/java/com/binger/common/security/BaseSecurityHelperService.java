package com.binger.common.security;

import com.binger.common.util.MyEasyJsonUtil;
import org.apache.commons.codec.binary.Base64;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.security.oauth2.provider.token.store.LocalTokenServices;
import org.springframework.security.oauth2.provider.token.store.MySecurityBreaker;

import java.security.KeyPair;

/**
 * 用于服务层获取当前用户信息
 *
 */
public class BaseSecurityHelperService extends MySecurityBreaker {

    private static final LocalTokenServices tokenServices = new LocalTokenServices();

    static {
        KeyPair keyPair = new KeyStoreKeyFactory(new ClassPathResource("keystore.jks"), "foobar".toCharArray())
                .getKeyPair("test");
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setKeyPair(keyPair);
        tokenServices.setJwtTokenEnhancer(converter);
    }

    private CompositePrincipal.BasicUserPrincipal getBasicPrincipal(){
        return MySecurityContextHolder.getCompositePrincipal().getBasicUser();
    }

    /** 重新设置当前线程安全机制部分 */
    protected void rewriteBasicUserPrincipal(String token){
        Authentication authentication =  tokenServices.loadAuthentication(token);
        if(authentication instanceof OAuth2Authentication) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            OAuth2Authentication oAuth2Authentication = (OAuth2Authentication)authentication;
            for(GrantedAuthority grantedAuthority : oAuth2Authentication.getUserAuthentication().getAuthorities()){
                if(grantedAuthority.getAuthority().startsWith("ErpPrincipal")){
                    int idx = grantedAuthority.getAuthority().indexOf(':');
                    String compositePrincipalJson = new String(Base64.decodeBase64(grantedAuthority.getAuthority().substring(idx+1)));
                    CompositePrincipal compositePrincipal = MyEasyJsonUtil.string2json(compositePrincipalJson, CompositePrincipal.class);
                    MySecurityContextHolder.setCompositePrincipal(compositePrincipal);
                    MySecurityContextHolder.setOauth2Authentication(oAuth2Authentication);
                    MySecurityContextHolder.setOauth2AccessToken(tokenServices.readAccessToken(token));
                }
            }
        }
    }

    /**获取登录对象账户id*/
    protected Integer getLoginUserId(){
        return getBasicPrincipal()==null?null:getBasicPrincipal().getUserId();
    }

    /**获取登录对象账户名*/
    protected String getLoginUserName(){
        return getBasicPrincipal()==null?null:getBasicPrincipal().getUserName();
    }

    /**获取登录对象人员表id*/
    protected Integer getLoginPersonId(){
        return getBasicPrincipal()==null?null:getBasicPrincipal().getPersonId();
    }

    /**获取登录对象人员姓名, e.g.张三*/
    protected String getLoginPersonName(){
        return getBasicPrincipal()==null?null:getBasicPrincipal().getPersonName();
    }

    /**获取复合授权对象*/
    protected CompositePrincipal getPrincipal(){
        return MySecurityContextHolder.getCompositePrincipal();
    }


}
