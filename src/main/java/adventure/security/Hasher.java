package adventure.security;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;

public class Hasher {

	private Hasher() {
	}

	public static String digest(String message) {
		String hash = null;

		if (message != null) {
			try {
				MessageDigest md = MessageDigest.getInstance("SHA-256");
				md.update(message.getBytes("UTF-8"));
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
