package net.hj2eplatform.core.utils;

import java.security.MessageDigest;
import org.apache.log4j.Logger;

/**
 * huyhoang for java platform. (h2j-platform)
 * @author HuyHoang
 * @Email: huyhoang85_tb@yahoo.com
 * @Tel: 0966298666
 */
public class HashData {

    final static Logger logger = Logger.getLogger(HashData.class);

    public static String hashDocument(String s) {
        try {
            MessageDigest hash = MessageDigest.getInstance("SHA1"); //MD5
            hash.update(s.getBytes());

            byte[] msgDigest = hash.digest();
            return convertToHex(msgDigest);
        } catch (Exception ex) {
            logger.debug(ex);
            return "";
        }
    }

    private static String convertToHex(byte[] data) {
        StringBuffer buf = new StringBuffer("");

        for (int i = 0; i < data.length; i++) {
            int halfbyte = (data[i] >>> 4) & 0x0F;
            int two_halfs = 0;

            do {
                if ((0 <= halfbyte) && (halfbyte <= 9)) {
                    buf.append((char) ('0' + halfbyte));
                } else {
                    buf.append((char) ('a' + (halfbyte - 10)));
                }

                halfbyte = data[i] & 0x0F;
            } while (two_halfs++ < 1);
        }

        return buf.toString();
    }

    public static void main(String[] agrs) {
        System.out.println(hashDocument("h2jtravel"));
    }
}
