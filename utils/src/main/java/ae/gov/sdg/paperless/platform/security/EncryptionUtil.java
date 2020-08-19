package ae.gov.sdg.paperless.platform.security;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import ae.gov.sdg.paperless.platform.exceptions.DNRuntimeException;

public final class EncryptionUtil {


    private EncryptionUtil() {
    }

    /***
     * Utility to encrypt string to a SHA512 encrypted string
     *
     * @param input string
     * @return SHA512 encrypted string
     */
    public static String hash(final String input) {
        try {
            final MessageDigest md = MessageDigest.getInstance("SHA-512");
            final byte[] messageDigest = md.digest(input.getBytes());
            final BigInteger number = new BigInteger(1, messageDigest);
            final StringBuilder hashtext = new StringBuilder(number.toString(16));
            while (hashtext.length() < 32) {
                hashtext.append("0");
            }
            return hashtext.toString();
        } catch (final NoSuchAlgorithmException e) {
            throw new DNRuntimeException(e);
        }
    }

}
