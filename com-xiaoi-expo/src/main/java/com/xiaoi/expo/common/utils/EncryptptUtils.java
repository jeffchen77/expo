package com.xiaoi.expo.common.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * @author lzm
 * @Description: 加密、解密公共类
 * @date 2018/3/1216:21
 */
public class EncryptptUtils {
    /**
     * MD5加密
     * @param str 需要加密的字符串
     * @return String
     * @throws NoSuchAlgorithmException
     */
    public static String md5Encrypt(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update((str).getBytes("UTF-8"));
        byte b[] = md5.digest();

        int i;
        StringBuffer buf = new StringBuffer("");

        for(int offset=0; offset<b.length; offset++){
            i = b[offset];
            if(i<0){
                i+=256;
            }
            if(i<16){
                buf.append("0");
            }
            buf.append(Integer.toHexString(i));
        }
        return  buf.toString().toUpperCase();
    }

    public static void main(String[] args) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        System.out.println(md5Encrypt("123456"));
        System.out.println(UUID.randomUUID());
    }


}
