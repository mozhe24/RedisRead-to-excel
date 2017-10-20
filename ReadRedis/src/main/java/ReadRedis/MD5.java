package ReadRedis;

import java.security.MessageDigest;

/**
 * MD5 加密类
 *
 * @author Administrator
 */
public class MD5 {
    public static String Md5(String plainText) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes("UTF-8"));
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)   i += 256;
                if (i < 16)  buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            return buf.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return " ";
        }
    }

    public static void main(String[] args) {
        System.out.println(MD5.Md5("A0:ED:CD:42:8C:AD"));
        System.out.println(MD5.Md5(""));

    }
}
