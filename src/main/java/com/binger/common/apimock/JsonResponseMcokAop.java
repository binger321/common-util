package com.binger.common.apimock;

import com.binger.common.ServerResponse;
import com.binger.common.util.MyEasyJsonUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Date;

/** Json数据本地Mock, 自动注入
 *
 */

@Aspect
@Component
public class JsonResponseMcokAop {

    Logger logger = LoggerFactory.getLogger(JsonResponseMcokAop.class);

    @Pointcut("execution(public * com.binger..*Fallbak.*(..))")
    public void pointcut(){}

    @AfterReturning(pointcut = "pointcut()", returning="serverResponse")
    public void afterReturning(JoinPoint joinPoint, ServerResponse serverResponse) throws Throwable {
        if(serverResponse!=null) {
            Method method = ((MethodSignature)joinPoint.getSignature()).getMethod();
            if(method.isAnnotationPresent(JsonResponseMock.class)){
                JsonResponseMock mockAnno = method.getAnnotation(JsonResponseMock.class);
                try{
                    InputStream inputStream = joinPoint.getTarget().getClass().getResourceAsStream("/"+mockAnno.value());
                    StringBuilder sb = new StringBuilder();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String line = "";
                    while((line=bufferedReader.readLine())!=null){
                        sb.append(line);
                    }
                    ServerResponse mockResponse = MyEasyJsonUtil.string2json(sb.toString(), ServerResponse.class);
                    serverResponse.setMsg(mockResponse.getMsg());
                    //解决Data部分是linkedHashMap问题
                    if(mockResponse.getData()!=null && serverResponse.getDataClazz()!=null){
                        if(serverResponse.getDataClazz().equals(String.class)||
                                serverResponse.getDataClazz().equals(Date.class)||
                                serverResponse.getDataClazz().equals(Integer.class)||
                                serverResponse.getDataClazz().equals(Long.class)||
                                serverResponse.getDataClazz().equals(Double.class)||
                                serverResponse.getDataClazz().equals(BigDecimal.class)||
                                serverResponse.getDataClazz().equals(Boolean.class)
                                ){
                            logger.debug("DataClazz is simple class");
                        }
                        else {
                            String dataJson = MyEasyJsonUtil.json2string(mockResponse.getData());
                            if(serverResponse.getDataSingle()) {
                                mockResponse.setData(MyEasyJsonUtil.string2json(dataJson, serverResponse.getDataClazz()));
                            } else {
                                mockResponse.setData(MyEasyJsonUtil.string2jsonList(dataJson, serverResponse.getDataClazz()));
                            }
                        }
                    }
                    serverResponse.setData(mockResponse.getData());
                    serverResponse.setStatus(mockResponse.getStatus());
                    serverResponse.setPage(mockResponse.getPage());
                    bufferedReader.close();
                }catch (IOException e){
                }catch (Exception e){
                    logger.debug("JsonResponseMcokAop error", e);
                }
            }
        }
    }
}
