package com.fractalmines;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class LaunchOptions {
    private boolean debug = false;

    public static LaunchOptions of(String[] args) {
        LaunchOptions options = new LaunchOptions();

        for (int i = 0; i < args.length; i++) {
            String opt = args[i];

            switch (opt.toLowerCase()) {
                case "debug", "-debug", "--debug" -> options.debug = true;
            }
        }
        return options;
    }
}
