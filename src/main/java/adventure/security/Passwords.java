package adventure.security;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.RandomStringUtils;

public class Passwords {

	private Passwords() {
	}

	public static String randomToken() {
		return RandomStringUtils.random(64, true, true);
	}

	public static String hash(String password) {
		String hash = null;

		if (password != null) {
			try {
				MessageDigest md = MessageDigest.getInstance("SHA-256");
				md.update(password.getBytes("UTF-8"));
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
