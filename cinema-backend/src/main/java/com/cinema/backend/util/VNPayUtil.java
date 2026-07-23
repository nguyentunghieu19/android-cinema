package com.cinema.backend.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

public class VNPayUtil {

    /**
     * Tạo HMAC SHA512
     */
    public static String hmacSHA512(String key, String data) {

        try {

            Mac hmac512 = Mac.getInstance("HmacSHA512");

            SecretKeySpec secretKey =
                    new SecretKeySpec(
                            key.getBytes(StandardCharsets.UTF_8),
                            "HmacSHA512"
                    );

            hmac512.init(secretKey);

            byte[] bytes =
                    hmac512.doFinal(data.getBytes(StandardCharsets.UTF_8));

            StringBuilder hash = new StringBuilder();

            for (byte b : bytes) {

                hash.append(String.format("%02x", b));

            }

            return hash.toString();

        } catch (Exception e) {

            throw new RuntimeException(e);

        }
    }

    /**
     * Encode URL
     */
    public static String encode(String value) {

        return URLEncoder.encode(value, StandardCharsets.UTF_8);

    }

    /**
     * Chuyển Map thành Query String
     */
    public static String buildQuery(Map<String, String> params) {

        return params.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry ->
                        encode(entry.getKey())
                                + "="
                                + encode(entry.getValue())
                )
                .collect(Collectors.joining("&"));

    }

}