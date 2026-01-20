package com.dating.util;

import java.security.SecureRandom;
import java.util.Base64;

/**
 * Utility class for generating verification tokens
 */
public class TokenUtil {

    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder();

    private TokenUtil() {
        // Private constructor to prevent instantiation
    }

    /**
     * Generates a random verification token
     * @return A secure random token string
     */
    public static String generateVerificationToken() {
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }
}
