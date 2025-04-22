package langya;

import cn.langya.CryptoUtils;
import cn.langya.TokenService;
import cn.langya.TokenServiceFactory;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Base64;
import java.util.Optional;

/**
 * @author LangYa466
 * @since 4/22/2025 2:21 PM
 */
public class Main {
    public static void main(String[] args) {
        try {
            // 用于测试 令牌有效期3秒
            Duration tokenValidity = Duration.ofSeconds(3);

            SecretKey aesGCMKey = CryptoUtils.generateAESKey();
            test("使用 AES-GCM 来生成令牌", TokenServiceFactory.createWithAES_GCM(aesGCMKey, tokenValidity), aesGCMKey, tokenValidity);

            SecretKey aesCBCKey = CryptoUtils.generateAESKey();
            test("使用 AES-CBC 来生成令牌", TokenServiceFactory.createWithAES_CBC(aesCBCKey, tokenValidity), aesCBCKey, tokenValidity);
        } catch (Exception e) {
            System.err.println("\n发生错误: !!!");
            e.printStackTrace();
        }
    }

    private static int index = 0;
    private static void test(String print, TokenService tokenService, SecretKey secretKey, Duration tokenValidity) throws InterruptedException {
        index++;
        System.out.printf("第%s次测试%n", index);
        System.out.println();
        System.out.println("---");
        System.out.println(print);
        System.out.println("生成的密钥 (Base64): " + Base64.getEncoder().encodeToString(secretKey.getEncoded()));
        System.out.println("令牌有效期: " + tokenValidity);
        System.out.println("---");

        String userId = "user-langya-furry";
        System.out.println("生成令牌 用户ID: " + userId);

        String token = tokenService.generateToken(userId);
        System.out.println("令牌: " + token);
        System.out.println("---");

        System.out.println("马上验证令牌...");
        Optional<String> validatedPayload = tokenService.validateAndGetPayload(token);

        if (validatedPayload.isPresent()) {
            System.out.println("验证通过!");
            System.out.println("恢复的用户ID: " + validatedPayload.get());
            System.out.println("验证结果是否匹配? " + userId.equals(validatedPayload.get()));
        } else {
            System.out.println("验证失败!");
        }
        System.out.println("---");

        System.out.println("测试无效令牌...");
        String tamperedToken = token + "tamper";
        Optional<String> tamperedPayload = tokenService.validateAndGetPayload(tamperedToken);
        System.out.println("篡改过的令牌验证通过吗? " + tamperedPayload.isPresent());
        System.out.println("---");

        System.out.println("测试过期令牌 (等待 " + (tokenValidity.getSeconds() + 1) + " 秒)...");
        tokenValidity = Duration.ofSeconds(2);
        token = tokenService.generateToken(userId);
        System.out.println("短期令牌: " + token);
        Thread.sleep((tokenValidity.getSeconds() + 1) * 1000);
        Optional<String> expiredPayload = tokenService.validateAndGetPayload(token);
        System.out.println("过期令牌验证通过吗? " + expiredPayload.isPresent());
        System.out.println("---");
        System.out.println();
    }
}