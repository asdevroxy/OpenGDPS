package com.fractalmines;

import com.fractalmines.config.Config;
import com.fractalmines.config.DatabaseConfig;
import com.fractalmines.database.Database;
import com.fractalmines.database.providers.MongoProvider;
import com.fractalmines.util.SecretGenerator;
import com.fractalmines.web.Web;

public class Initializr {
    protected static void start(String[] args) {
        OpenGDPS.setLaunchOptions(LaunchOptions.of(args));

        Config.getInstance();
        DatabaseConfig.getInstance();

        Database.setActiveProvider(switch(DatabaseConfig.getInstance().getProvider())
            {
                case MONGODB -> new MongoProvider();
                case MARIADB -> throw new RuntimeException("MariaDB is not available yet.");
            }
        );
        Database.getActiveProvider().connect();

        //initSecretGenerator();
        new Web().init();
    }

    private static void initSecretGenerator() {
        int numBytes = Config.getInstance().getAccountSecretNumBytes();
        OpenGDPS.setSecretGenerator(new SecretGenerator(Math.max(numBytes, 8)));

        if (numBytes < 16) {
            System.err.printf("SEVERE: Insufficient account security. You should be using at least 16 bytes (128 bits), not %d (%d bits). Continue with caution!%n", numBytes, numBytes * 8);
            return;
        }

        System.out.printf("Account security numBytes is sufficient at %d bytes (%d bits)%n", numBytes, numBytes * 8);
    }
}
