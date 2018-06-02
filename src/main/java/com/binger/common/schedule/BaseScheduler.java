package com.binger.common.schedule;

import com.binger.common.hystrix.HystrixCredentialsContext;
import com.binger.common.redis.DistributedLockHandler;
import com.binger.common.redis.Lock;
import com.binger.common.security.BaseSecurityHelperService;
import com.binger.common.util.MyEasyJsonUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.LocalTokenServices;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * 定时任务base
 */
public class BaseScheduler extends BaseSecurityHelperService {

    public static LocalTokenServices localTokenServices = new LocalTokenServices();

    @Autowired
    protected JwtAccessTokenConverter jwtAccessTokenConverter;

    @Autowired
    protected HystrixCredentialsContext hystrixCredentialsContext;

    @Autowired
    protected DistributedLockHandler distributedLockHandler;

    protected String token;

    @Value("${spring.application.name}")
    protected String applicationName;

//    @Value("${security.oauth2.client.access-token-uri}")
//    protected String accessTokenUri;

    @Autowired
    private LoadBalancerClient loadBalancer;

    @Value("${eureka.instance.ip-address:}")
    private String hostIpAddress;

    @Value("${server.port:}")
    private String hostServerPort;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    protected String getServerIdentity(){
        return hostIpAddress+":"+hostServerPort;
    }

    public void tryReleaseLockByServerIdentity(Lock lock) {
        if (!StringUtils.isEmpty(lock.getName())) {
            String lockByServerIdentity = redisTemplate.boundValueOps(lock.getName()).get();
            if(getServerIdentity().equals(lockByServerIdentity)) {
                redisTemplate.delete(lock.getName());
            }
        }
    }

    /**
     * 登录获取token
     */
    protected void doLogin() {
        final RestTemplate restTemplate = new RestTemplate();
        final String plainCredit = "erp-cloud:erp-cloud";
        final byte[] plainCreditBytes = plainCredit.getBytes();
        final byte[] base64CreditBytes = Base64.encodeBase64(plainCreditBytes);
        final String base64Credit = new String(base64CreditBytes);
        final HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + base64Credit);
        final HttpEntity<String> request = new HttpEntity<String>(headers);
//        ServiceInstance instance = loadBalancer.choose("erp-svc-uaa");
//        String accessTokenUri = String.format("http://%s:%s/oauth/token", instance.getHost(), instance.getPort());
        String accessTokenUri = String.format("http://%s:%s/oauth/token", "111.231.137.44", "20002");
        String authorUrl = accessTokenUri + "?grant_type=password&client_id=erp-cloud&client_secret=erp-cloud&username=system&password=123456";

        try {
            final ResponseEntity<String> response = restTemplate.exchange(authorUrl, HttpMethod.POST, request, String.class);
            Map resMap = MyEasyJsonUtil.string2json(response.getBody(), Map.class);
            token = (String) resMap.get("access_token");
        } catch (Exception e) {
            throw e;
        }
    }
}
