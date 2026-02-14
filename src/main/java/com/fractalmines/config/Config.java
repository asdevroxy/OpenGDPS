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
public class Config {
    private static Config instance;

    @Comment("The number of bytes to use for account secrets. You should ideally have this be a minimum of 16.")
    private int accountSecretNumBytes = 32;

    @Comment("Change the ip/port for the reverse proxy here.")
    private Networking networking = new Networking();

    @Comment("These options are mostly for running this software behind Cloudflare Access. DO NOT TOUCH UNLESS YOU KNOW WHAT YOU'RE DOING")
    private WebSecurity webSecurity = new WebSecurity();




    @Configuration
    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @SuppressWarnings("FieldMayBeFinal")
    public static class Networking {
        private String ip = "0.0.0.0";
        private int port = 80;

        private int capacity = 256;
    }
    @Configuration
    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @SuppressWarnings("FieldMayBeFinal")
    public static class WebSecurity {
        private String accountsUrl = "http://localhost:80/";
        private String customContentUrl = "http://localhost:80/";

        @Comment("Cloudflare Access uses X-XSFR-TOKEN. DO NOT add extra headers here unless you know FOR SURE what you are doing.")
        private String[] extraXsrfHeaders = new String[] { "X-XSRF-TOKEN" };
    }

    public static Config getInstance() {
        return  instance == null
                ? instance = YamlConfigurations.update(new File("config.yml").toPath(), Config.class, HEADER)
                : instance;
    }

    private static final String HEADER_STR = """
            !!! NOTE !!!
            This software is designed to be ran behind a reverse proxy with something like Caddy or Nginx (usually behind Cloudflare).
            While you *can* run this software without the use of a reverse proxy, it's recommended you use one.
            
            Support will not be given for users not running this software behind a reverse proxy.
            Join the Discord here: https://discord.gg/WghcewasVQ
            
            For Caddy, you can modify and add this to your Caddyfile:
            
            gdps.example.com {
                reverse_proxy ip:port {
                    header_up X-Forwarded-For {remote_host}
                    header_up X-Forwarded-For {remote_host}
                    header_up X-Real-IP {remote_host}
                }
            }
            """;
    private static YamlConfigurationProperties HEADER = YamlConfigurationProperties.newBuilder().header(HEADER_STR).setNameFormatter(NameFormatters.LOWER_KEBAB_CASE).build();
}
