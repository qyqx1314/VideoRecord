package com.banger.videorecord.util;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Xuchaowen on 2016/6/15.
 */
public class MD5Utils {

        private static final String YAN = "isgu&*%^2345UTF23(*&ywtdfkxjf47";

        /**
         * 对输入的密码明文进行md5加密
         * @param password
         * @return
         */
        public static String md5Encrypt(String password) {

            try {

                StringBuffer sb = new StringBuffer();

                MessageDigest digest = MessageDigest.getInstance("md5");// algorithm

                byte[] bytes = digest.digest(password.getBytes()); // 参数是，明文字节数组，返回的就是加密后的结果，字节数组
                for (byte b : bytes) {
                    // 数byte 类型转换为无符号的整数
                    int n = b & 0XFF;

                    // 将整数转换为16进制
                    String s = Integer.toHexString(n);
                    // 如果16进制字符串是一位，那么前面补0
                    if (s.length() == 1) {
                        sb.append("0" + s);
                    } else {
                        sb.append(s);
                    }
                }

                return sb.toString();

            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            return null;
        }


        /**
         * 计算文件的md5值
         * @param file
         * @return
         */
        public static String getFileMd5(File file){

            try {

                StringBuffer sb = new StringBuffer();

                MessageDigest digest = MessageDigest.getInstance("md5");// algorithm

                FileInputStream fin = new FileInputStream(file);

                int len = -1;
                byte[] buffer = new byte[1024];
                // 将整个文件的字节都读入加密器
                while((len = fin.read(buffer))!=-1){
                    digest.update(buffer, 0, len);
                }

                byte[] bytes = digest.digest();  // 对读入的内容进行加密


                for (byte b : bytes) {
                    // 数byte 类型转换为无符号的整数
                    int n = b & 0XFF;

                    // 将整数转换为16进制
                    String s = Integer.toHexString(n);
                    // 如果16进制字符串是一位，那么前面补0
                    if(s.length() == 1){
                        sb.append("0"+s);
                    }else{
                        sb.append(s);
                    }
                }

                return sb.toString();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;

        }



    }

