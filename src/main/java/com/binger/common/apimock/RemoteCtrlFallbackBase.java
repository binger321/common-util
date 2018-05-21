package com.binger.common.apimock;

import com.binger.common.ServerResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.feign.FeignClient;

/**
 *
 */
public class RemoteCtrlFallbackBase {

    final static Logger logger = LoggerFactory.getLogger(RemoteCtrlFallbackBase.class);

    public static final String ERR_MESSAGE = "操作失败！";

    @Value("${spring.application.name}")
    private String currentSvcName;

    /**
     * 用于后端微服务接口调用Mock, 单个对象返回
     * @param dataClazz Data对象Clazz, VO class
     * @return
     */
    protected ServerResponse getFallbackResponseForSingle(Class dataClazz){
        return getFallbackResponse(dataClazz, true);
    }

    /**
     * 用于后端微服务接口调用Mock, 列表对象返回
     * @param dataClazz Data对象Clazz, VO class
     * @return
     */
    protected ServerResponse getFallbackResponseForList(Class dataClazz){
        return getFallbackResponse(dataClazz, false);
    }

    private ServerResponse getFallbackResponse(Class dataClazz, Boolean isDataSingle){
        StringBuilder sb = new StringBuilder(ERR_MESSAGE+"当前服务ID:"+currentSvcName);
        if(getClass().getInterfaces().length!=0) {
            Class implInterfaceWithFeign = getClass().getInterfaces()[0];
            if(implInterfaceWithFeign.isAnnotationPresent(FeignClient.class)){
                FeignClient feignClient = (FeignClient) implInterfaceWithFeign.getAnnotation(FeignClient.class);
                if(StringUtils.isNoneBlank(feignClient.name())){
                    sb.append(";失败子服务ID:"+feignClient.name());
                }
                if(StringUtils.isNoneBlank(feignClient.value())){
                    sb.append(";失败子服务ID:"+feignClient.value());
                }
            }
        }
        logger.warn(sb.toString());
        ServerResponse serverResponse = ServerResponse.createByError(sb.toString());
        serverResponse.setDataClazz(dataClazz);
        serverResponse.setDataSingle(isDataSingle);

        return serverResponse;
    }
}
