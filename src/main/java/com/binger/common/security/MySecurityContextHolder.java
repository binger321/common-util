package com.binger.common.security;

import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

/**
 * 授权对象上下文持有者
 */
public final class MySecurityContextHolder {
    private static ThreadLocal<CompositePrincipal> compositePrincipalThreadLocal = new ThreadLocal<CompositePrincipal>();
    private static ThreadLocal<OAuth2AccessToken> oauth2AccessTokenThreadLocal = new ThreadLocal<>();
    private static ThreadLocal<OAuth2Authentication> oauth2AuthenticationThreadLocal = new ThreadLocal<>();

    public static CompositePrincipal getCompositePrincipal() {
        return compositePrincipalThreadLocal.get();
    }

    public static void setCompositePrincipal(CompositePrincipal compositePrincipal) {
        compositePrincipalThreadLocal.set(compositePrincipal);
    }

    public static OAuth2AccessToken getOauth2AccessToken() {
        return oauth2AccessTokenThreadLocal.get();
    }

    public static void setOauth2AccessToken(OAuth2AccessToken oauth2AccessToken) {
        oauth2AccessTokenThreadLocal.set(oauth2AccessToken);
    }

    public static OAuth2Authentication getOauth2Authentication() {
        return oauth2AuthenticationThreadLocal.get();
    }

    public static void setOauth2Authentication(OAuth2Authentication oauth2Authentication) {
        oauth2AuthenticationThreadLocal.set(oauth2Authentication);
    }
}
