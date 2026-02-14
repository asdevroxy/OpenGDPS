package com.fractalmines;

import com.fractalmines.util.SecretGenerator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

public class OpenGDPS {
    @Getter private static OpenGDPS instance;
    @Getter @Setter(AccessLevel.PROTECTED) private static SecretGenerator secretGenerator = new SecretGenerator();
    @Getter @Setter(AccessLevel.PROTECTED) private static LaunchOptions launchOptions;

    public static void main(String[] args) {
        Initializr.start(args);
    }
}