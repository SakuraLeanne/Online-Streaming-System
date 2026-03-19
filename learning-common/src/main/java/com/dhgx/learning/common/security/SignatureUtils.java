package com.dhgx.learning.common.security;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * 简化版签名与验签工具。
 */
public final class SignatureUtils {

    private static final String HMAC_SHA256 = "HmacSHA256";

    private SignatureUtils() {
    }

    public static String sign(String content, String secret) {
        try {
            Mac mac = Mac.getInstance(HMAC_SHA256);
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), HMAC_SHA256));
            byte[] digest = mac.doFinal(content.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(digest);
        } catch (Exception ex) {
            throw new IllegalStateException("Sign failed", ex);
        }
    }

    public static boolean verify(String content, String secret, String signature) {
        return sign(content, secret).equals(signature);
    }
}
