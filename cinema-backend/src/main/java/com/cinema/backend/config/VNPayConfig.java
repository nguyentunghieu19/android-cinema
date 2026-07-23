package com.cinema.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VNPayConfig {

    @Value("${vnp.tmnCode}")
    private String tmnCode;

    @Value("${vnp.hashSecret}")
    private String hashSecret;

    @Value("${vnp.payUrl}")
    private String payUrl;

    @Value("${vnp.returnUrl}")
    private String returnUrl;

    @Value("${vnp.version}")
    private String version;

    @Value("${vnp.command}")
    private String command;

    @Value("${vnp.currCode}")
    private String currCode;

    @Value("${vnp.locale}")
    private String locale;

    public String getTmnCode() {
        return tmnCode;
    }

    public String getHashSecret() {
        return hashSecret;
    }

    public String getPayUrl() {
        return payUrl;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public String getVersion() {
        return version;
    }

    public String getCommand() {
        return command;
    }

    public String getCurrCode() {
        return currCode;
    }

    public String getLocale() {
        return locale;
    }
}