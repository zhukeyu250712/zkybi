package com.yupi.springbootinit.utils;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

/**
 * @program: zkybi-backend
 * @description:
 * @author: ZKYAAA
 * @create: 2024-08-17 15:45
 **/

public class Download {
    public static void download(String netUrl, String localFilename) {
        try (InputStream inStream = new URL(netUrl).openStream();
             OutputStream outStream = new FileOutputStream(localFilename)) {
            final byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String networkFileUrl = "https://learn.lianglianglee.com/PDF/%E9%9D%A2%E8%AF%95%E8%B5%84%E6%96%99.pdf";
        String localFilename = "D:\\Users\\ZKY\\Desktop\\aaa.pdf";
        download(networkFileUrl, localFilename);
    }
}
