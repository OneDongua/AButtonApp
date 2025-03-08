package com.onedongua.abutton.util;

import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {
    private static final String TAG = "MD5Utils";

    public static String md5(String input) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");

            messageDigest.update(input.getBytes());

            byte[] digestBytes = messageDigest.digest();

            StringBuilder stringBuilder = new StringBuilder();
            for (byte b : digestBytes) {
                stringBuilder.append(String.format("%02x", b)); // %02x 表示将字节转换为十六进制字符串，并保留两位
            }

            return stringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "md5: ", e);
            return null;
        }
    }
}