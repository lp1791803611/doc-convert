package top.plgxs.common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {

    private static final String hexDigIts[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
                                                "a", "b", "c", "d", "e", "f" };

    public static String byteArrayToHexString(byte b[]) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }

    public static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n += 256;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigIts[d1] + hexDigIts[d2];
    }

    /**
     * md5加密字符串
     * @param str
     * @return
     */
    public static String digest(String str) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            return byteArrayToHexString(md5.digest(str.getBytes()));
        } catch (NoSuchAlgorithmException e) {
            System.out.println("【MD5加密异常】");
            e.printStackTrace();
            return "";
        }
    }

}
