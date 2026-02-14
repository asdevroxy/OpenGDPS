package com.fractalmines.util;

import java.nio.charset.StandardCharsets;

public class XOR {
    public static byte[] cipher(byte[] plaintext, byte[] key) {
        byte[] output = new byte[plaintext.length];

        for (int i = 0; i < plaintext.length; i++) {
            output[i] = (byte) (plaintext[i] ^ key[i % key.length]);
        }

        return output;
    }

    public static byte[] cipher(String plaintext, String key) {
        byte[] plainBytes = plaintext.getBytes(StandardCharsets.ISO_8859_1);
        byte[] keyBytes = key.getBytes(StandardCharsets.ISO_8859_1);

        return cipher(plainBytes, keyBytes);
    }
}
