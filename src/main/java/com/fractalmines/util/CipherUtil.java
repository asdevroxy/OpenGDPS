package com.fractalmines.util;

import lombok.experimental.UtilityClass;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

@UtilityClass
public class CipherUtil {
    public static final String
            SHA1_PASSWORD = "mI29fmAnxgTs",
            SHA1_SOLO2 = "xI25fpAapCQg",
            SHA1_SOLO3 = "oC36fpYaPtdg",
            SHA1_SOLO4 = "pC26fpYaQCtg",
            SHA1_PACK  = "xI25fpAapCQg";

    public String sha1Hash(String string) {
        return sha1Hash(string, "");
    }

    public String sha1Hash(String string, String suffixSha1Key) {
        try {
            String stringToUse = string + suffixSha1Key;

            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.reset();
            digest.update(stringToUse.getBytes(StandardCharsets.UTF_8));

            return hexFromBytes(digest.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private static String hexFromBytes(final byte[] hash)
    {
        String result;

        try (Formatter formatter = new Formatter()) {
            for (byte by : hash) {
                formatter.format("%02x", by);
            }

            result = formatter.toString();
        }

        return result;
    }
}
