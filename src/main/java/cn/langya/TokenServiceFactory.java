package cn.langya;

import javax.crypto.SecretKey;
import java.time.Duration;

/**
 * @author LangYa466
 * @since 4/22/2025 2:20 PM
 */
public class TokenServiceFactory {
    public static TokenService createWithAES_GCM(SecretKey key, Duration validity) {
        return new TokenService(key, validity,
                (plaintext, secret) -> {
                    try {
                        return CryptoUtils.encryptAES_GCM(plaintext, secret);
                    } catch (Exception e) {
                        throw new TokenEncryptionException("AES-GCM Encryption failed", e);
                    }
                },
                (ciphertext, secret) -> {
                    try {
                        return CryptoUtils.decryptAES_GCM(ciphertext, secret);
                    } catch (Exception e) {
                        throw new TokenEncryptionException("AES-GCM Decryption failed", e);
                    }
                }
        );
    }

    public static TokenService createWithAES_CBC(SecretKey key, Duration validity) {
        return new TokenService(key, validity,
                (plaintext, secret) -> {
                    try {
                        return CryptoUtils.encryptAES_CBC(plaintext, secret);
                    } catch (Exception e) {
                        throw new TokenEncryptionException("AES-CBC Encryption failed", e);
                    }
                },
                (ciphertext, secret) -> {
                    try {
                        return CryptoUtils.decryptAES_CBC(ciphertext, secret);
                    } catch (Exception e) {
                        throw new TokenEncryptionException("AES-CBC Decryption failed", e);
                    }
                }
        );
    }
}