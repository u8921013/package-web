package net.ubn.td.package_web.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

/** Utility methods for CEK generation and encryption. */
public final class CekUtils {

    private CekUtils() {}

    /**
     * Generates a random 32 byte CEK and encrypts it with the given master key.
     * This is a simplified example; real projects should use a proper encryption mode.
     */
    public static String generateEncryptedCek(String masterKey) {
        try {
            byte[] cek = new byte[32];
            new SecureRandom().nextBytes(cek);

            // TODO use stronger encryption with IV etc.
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            SecretKeySpec keySpec = new SecretKeySpec(masterKey.getBytes(StandardCharsets.UTF_8), "AES");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            byte[] encrypted = cipher.doFinal(cek);
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to encrypt CEK", e);
        }
    }
}
