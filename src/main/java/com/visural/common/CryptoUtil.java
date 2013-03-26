package com.visural.common;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.visural.common.StringUtil.emptyToNull;

/**
 * Various utilities for passwords and encryption.
 */
public class CryptoUtil {

    private static final String PASSWORD_HASH_ALGORITHM = "PBKDF2WithHmacSHA1";
    private static final String SALT_RANDOM_ALGORITHM = "SHA1PRNG";
    private static final int PASSWORD_HASH_KEY_LENGTH = 160;

    /**
     * Generate a hash of password using PBKDF2WithHmacSHA1 algorithm and salt
     * and PBKDF2 #iterations requested.
     *
     * @param password
     * @param salt
     * @param iterations
     * @param storedHash
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static boolean passwordHashMatches(String password, byte[] salt, int iterations, byte[] storedHash) {
        byte[] passwordHash = passwordHash(password, salt, iterations);
        return Arrays.equals(storedHash, passwordHash);
    }

    /**
     * Generate a hash of password using PBKDF2WithHmacSHA1 algorithm and salt
     * and PBKDF2 #iterations requested.
     *
     * @param password
     * @param salt
     * @param iterations
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static byte[] passwordHash(String password, byte[] salt, int iterations) {
        checkNotNull(emptyToNull(password));
        checkNotNull(salt);

        if (salt.length < 8) {
            throw new IllegalArgumentException("For secure hashes, a salt >= 64 bits (8 bytes) in length should be used.");
        }

        if (iterations < 1000) {
            throw new IllegalArgumentException("For secure hashes, an iteration count of at least 1000 should be used.");
        }

        try {
            KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, iterations, PASSWORD_HASH_KEY_LENGTH);
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(PASSWORD_HASH_ALGORITHM);
            return secretKeyFactory.generateSecret(keySpec).getEncoded();
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("Algorithm " + PASSWORD_HASH_ALGORITHM + " expected as standard not available on this JVM.", ex);
        } catch (InvalidKeySpecException ex) {
            throw new IllegalArgumentException("Key specification was not valid.", ex);
        }
    }

    /**
     * Generate a secure 64-bit salt value as a byte array.
     *
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static byte[] generateSalt() {
        SecureRandom random = secureRandom.get();
        byte[] salt = new byte[8];
        random.nextBytes(salt);
        return salt;
    }
    private static final ThreadLocal<SecureRandom> secureRandom = new ThreadLocal<SecureRandom>() {
        @Override
        protected SecureRandom initialValue() {
            try {
                return SecureRandom.getInstance(SALT_RANDOM_ALGORITHM);
            } catch (NoSuchAlgorithmException ex) {
                throw new IllegalStateException("Algorithm " + SALT_RANDOM_ALGORITHM + " expected as standard not available on this JVM.", ex);
            }
        }
    };
}
