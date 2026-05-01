package dev.nlu.moduleloginj2ee.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.channels.NoConnectionPendingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HexFormat;

public class TokenUtil {
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final Logger log = LoggerFactory.getLogger(TokenUtil.class);

    // Tạo token 64 bit
    public static String generateSecureToken(){
        byte[] bytes = new byte[32];
        SECURE_RANDOM.nextBytes(bytes);
        return HexFormat.of().formatHex(bytes);
    }

    // Hash token
    public String hash(String token){
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(token.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest);
        } catch (NoSuchAlgorithmException e) {
            log.error("Lỗi không tìm thấy thuật toán hash", e);
            throw new NoConnectionPendingException();
        }
    }
}
