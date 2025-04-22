package cn.langya;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author LangYa466
 * @since 4/22/2025 2:20 PM
 */
public class TokenService {
    private final Duration validityDuration;
    private final BiFunction<String, SecretKey, String> encryptFunction;
    private final BiFunction<String, SecretKey, String> decryptFunction;
    private final SecretKey secretKey;

    private static final String PAYLOAD_FORMAT = "{\"sub\":\"%s\",\"iat\":%d,\"exp\":%d}";
    private static final Pattern SUB_PATTERN = Pattern.compile("\"sub\":\"(.*?)\"");
    private static final Pattern EXP_PATTERN = Pattern.compile("\"exp\":(\\d+)");

    public TokenService(SecretKey secretKey, Duration validityDuration,
                        BiFunction<String, SecretKey, String> encryptFunction,
                        BiFunction<String, SecretKey, String> decryptFunction) {
        this.secretKey = Objects.requireNonNull(secretKey, "SecretKey cannot be null");
        this.validityDuration = Objects.requireNonNull(validityDuration, "ValidityDuration cannot be null");
        this.encryptFunction = Objects.requireNonNull(encryptFunction, "Encrypt function cannot be null");
        this.decryptFunction = Objects.requireNonNull(decryptFunction, "Decrypt function cannot be null");
    }

    public String generateToken(String payload) {
        Objects.requireNonNull(payload, "Payload cannot be null");
        Instant now = Instant.now();
        Instant expiry = now.plus(this.validityDuration);

        String jsonPayload = String.format(PAYLOAD_FORMAT,
                escapeJson(payload),
                now.getEpochSecond(),
                expiry.getEpochSecond());

        return Optional.ofNullable(this.encryptFunction.apply(jsonPayload, this.secretKey))
                .orElseThrow(() -> new TokenGenerationException("Failed to encrypt token payload"));
    }

    public Optional<String> validateAndGetPayload(String token) {
        Objects.requireNonNull(token, "Token cannot be null");

        try {
            String decryptedJson = this.decryptFunction.apply(token, this.secretKey);

            // 提取/验证
            return extractExpiration(decryptedJson)
                    .filter(expiryTime -> Instant.now().isBefore(expiryTime))
                    .flatMap(expiryTime -> extractPayload(decryptedJson));

        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private Optional<Instant> extractExpiration(String decryptedJson) {
        Matcher expMatcher = EXP_PATTERN.matcher(decryptedJson);
        if (expMatcher.find()) {
            long expSeconds = Long.parseLong(expMatcher.group(1));
            return Optional.of(Instant.ofEpochSecond(expSeconds));
        }
        return Optional.empty();
    }

    private Optional<String> extractPayload(String decryptedJson) {
        Matcher subMatcher = SUB_PATTERN.matcher(decryptedJson);
        if (subMatcher.find()) {
            return Optional.of(unescapeJson(subMatcher.group(1)));
        }
        return Optional.empty();
    }

    private String escapeJson(String input) {
        return input.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private String unescapeJson(String input) {
        return input.replace("\\\"", "\"").replace("\\\\", "\\");
    }
}
