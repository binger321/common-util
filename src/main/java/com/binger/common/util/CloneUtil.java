package com.binger.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * 复制工具类
 */
public class CloneUtil {

    private static Logger logger = LoggerFactory.getLogger(CloneUtil.class);

    /** 利用字节流 进行对象深复制 */
    public static <T extends Serializable> T deepClone(T serializableObj){
        if(serializableObj==null) return null;

        try {
            //将对象写到流里
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream oo = new ObjectOutputStream(bo);
            oo.writeObject(serializableObj);
            //从流里读出来
            ByteArrayInputStream bi = new ByteArrayInputStream(bo.toByteArray());
            ObjectInputStream oi = new ObjectInputStream(bi);
            return (T) oi.readObject();
        }catch (IOException|ClassNotFoundException e){
            logger.warn(serializableObj.getClass().getSimpleName()+" clone error", e);
            return null;
        }
    }
}
