package org.springframework.security.oauth2.provider.token.store;

import com.binger.common.security.MySecurityContextHolder;
import com.binger.common.util.AssertUtil;
import com.binger.common.util.CloneUtil;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;

import java.security.KeyPair;
import java.util.Date;

public class MySecurityBreaker {

    private static JwtAccessTokenConverter converter = new JwtAccessTokenConverter();

    /** 2999年12月31日23点59分59秒 */
    private static Date eternalDate = new Date(32503658399000L);

    static {
        KeyPair keyPair = new KeyStoreKeyFactory(new ClassPathResource("keystore.jks"), "foobar".toCharArray())
                .getKeyPair("test");
        converter.setKeyPair(keyPair);
    }

    /** 强制获取永不过时Token, 用于批量处理方式, 非主线程调用其他接口 */
    protected String exchangeEternalToken(){
        AssertUtil.notNull(MySecurityContextHolder.getOauth2AccessToken(),
                "Oauth2AccessToken is null in current Thread"+Thread.currentThread().getName());
        AssertUtil.notNull(MySecurityContextHolder.getOauth2Authentication(),
                "Oauth2Authentication is null in current Thread"+Thread.currentThread().getName());

        if(MySecurityContextHolder.getOauth2AccessToken() instanceof DefaultOAuth2AccessToken) {
            DefaultOAuth2AccessToken auth2AccessToken = (DefaultOAuth2AccessToken) MySecurityContextHolder.getOauth2AccessToken();
            DefaultOAuth2AccessToken auth2AccessTokenClone = CloneUtil.deepClone(auth2AccessToken);
            auth2AccessTokenClone.setExpiration(eternalDate);
            return converter.encode(auth2AccessTokenClone,
                    MySecurityContextHolder.getOauth2Authentication());
        }

        return null;
    }
}