package com.fractalmines.util;

import lombok.experimental.UtilityClass;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Formatter;
import java.util.Map;

@UtilityClass
public class GDUtil {
    public boolean isMissingAnyKeys(Map<String,String> haystack, String... needles) {
        for (String needle : needles) {
            if (!haystack.containsKey(needle)) return true;
        }

        return false;
    }

    public static String timeAgo(Instant past) {
        Instant now = Instant.now();
        long seconds = ChronoUnit.SECONDS.between(past, now);

        if (seconds < 60) {
            return seconds + " second" + (seconds != 1 ? "s" : "");
        }

        long minutes = ChronoUnit.MINUTES.between(past, now);
        if (minutes < 60) {
            return minutes + " minute" + (minutes != 1 ? "s" : "");
        }

        long hours = ChronoUnit.HOURS.between(past, now);
        if (hours < 24) {
            return hours + " hour" + (hours != 1 ? "s" : "");
        }

        long days = ChronoUnit.DAYS.between(past, now);
        if (days < 7) {
            return days + " day" + (days != 1 ? "s" : "");
        }

        long weeks = days / 7;
        if (weeks < 4) {
            return weeks + " week" + (weeks != 1 ? "s" : "");
        }

        long months = days / 30;
        if (months < 12) {
            return months + " month" + (months != 1 ? "s" : "");
        }

        long years = days / 365;
        return years + " year" + (years != 1 ? "s" : "");
    }
}
