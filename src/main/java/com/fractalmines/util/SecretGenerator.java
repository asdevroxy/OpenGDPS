package com.fractalmines.util;

import java.security.SecureRandom;
import java.util.Base64;

@Deprecated
public class SecretGenerator {
    private static final String VALID_SECRET_CHARACTERS = "qwertyuiopasdfghjklzxcvbnm=-_MNBVCXZLKJHGFDSAPOIUYTREWQ";
    private final SecureRandom random = new SecureRandom();

    private final int numBytes;

    public SecretGenerator() {
        this(32);
    }
    public SecretGenerator(int numBytes) {
        this.numBytes = numBytes;
    }

    public String generate(int numBytes) {
        byte[] bytes = new byte[numBytes];
        random.nextBytes(bytes);

        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    public String generate() {
        return generate(numBytes);
    }
}
