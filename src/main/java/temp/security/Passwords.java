package temp.security;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.RandomStringUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Passwords {

    private Passwords() {
    }

    public static String randomToken() {
        return RandomStringUtils.random(64, true, true);
    }

    public static String hash(String password, String salt) {
        String hash = null;

        if (password != null) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                md.update((password + salt).getBytes("UTF-8"));
                hash = Hex.encodeHexString(md.digest());

            } catch (NoSuchAlgorithmException cause) {
                throw new RuntimeException(cause);

            } catch (UnsupportedEncodingException cause) {
                throw new RuntimeException(cause);
            }
        }

        return hash;
    }
}
