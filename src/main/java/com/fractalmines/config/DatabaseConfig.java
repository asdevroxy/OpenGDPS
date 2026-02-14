package com.fractalmines.config;

import de.exlll.configlib.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.File;

@Configuration
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@SuppressWarnings("FieldMayBeFinal")
public class DatabaseConfig {
    private static DatabaseConfig instance;

    @Comment("Supported providers: MONGODB, MARIADB")
    private Provider provider = Provider.MONGODB;

    private MongoDB mongo = new MongoDB();
    private MariaDB maria = new MariaDB();


    @Configuration
    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @SuppressWarnings("FieldMayBeFinal")
    public static class MongoDB {
        private String connectionString = "mongodb+srv://username:password@ip:port/opengdps";
        private String database = "opengdps";
    }

    @Configuration
    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @SuppressWarnings("FieldMayBeFinal")
    public static class MariaDB {
        private String hostname = "127.0.0.1";
        private String username = "opengdps";
        private String password = "opengdps_db_password";
        private String database = "opengdps";

        @Comment("Enable to ignore the options above and use the connection string below.")
        private boolean useCustomConnectionString = false;
        private String connectionString = "connection_string_here";
    }


    public enum Provider {
        MONGODB,
        MARIADB;
    }

    public static DatabaseConfig getInstance() {
        return instance == null
               ? instance = YamlConfigurations.update(new File("database.yml").toPath(), DatabaseConfig.class, HEADER)
               : instance;
    }

    private static YamlConfigurationProperties HEADER = YamlConfigurationProperties.newBuilder().setNameFormatter(NameFormatters.LOWER_KEBAB_CASE).build();
}
