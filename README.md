# SimpleToken

简单鉴权库 (AES-GCM / AES-CBC / 自定义) 加密算法生成/验证和管理令牌 令牌可以用于用户身份验证和权限管理

## 特性
- 支持使用 (AES-GCM / AES-CBC / 自定义) 加密算法生成安全令牌
- 自动处理令牌验证 包括篡改检测/过期检测
- 方便简洁 可以直接集成到任何基于令牌认证的应用中

## 环境要求
- Java版本需要 >= 8
  
## 介绍
- `cn.langya.TokenService` - 令牌生成/验证
- `cn.langya.TokenServiceFactory` - 令牌加密
- `cn.langya.TokenGenerationException` - 令牌生成异常
- `cn.langya.TokenEncryptionException` - 令牌加密异常
- https://github.com/LangYa466/CryptoUtils - 加密处理

## 示例
代码示例在 `src/test/java/langya/Main.java`

```text
第1次测试

---
使用 AES-GCM 来生成令牌
生成的密钥 (Base64): 4SbZ2hD36JSuIKrxSqDMocIV8cFiMbSkBu3Hb4Jz0rg=
令牌有效期: PT3S
---
生成令牌 用户ID: user-langya-furry
令牌: D0gYBjiXeu378V+DlltcSf3h7ApQacmzwunjlMLaCTsozi+JLorZwL7HzNGdZ0WQJpwKIxs6+b9FSiLQ/El61ibtIklj5JGnpROg7PCxmXqVgQW19lln6t0=
---
马上验证令牌...
验证通过!
恢复的用户ID: user-langya-furry
验证结果是否匹配? true
---
测试无效令牌...
篡改过的令牌验证通过吗? false
---
测试过期令牌 (等待 4 秒)...
短期令牌: k0MghU3yQbe1W8Ju4XCcjHHDSQfH/AmSaKG2c3vcfgMFXjUq6qDN2+iQjy4yaTUPbO91R7l7YSd4zXLJ4elOxmJY9O1Hs/rWhXysFKAlBpsO3NComPGTgac=
过期令牌验证通过吗? false
---

第2次测试

---
使用 AES-CBC 来生成令牌
生成的密钥 (Base64): TR4TKvDY+xZfKgmymX3Au1MNycPfwQrZyNCIUn72yvc=
令牌有效期: PT3S
---
生成令牌 用户ID: user-langya-furry
令牌: V7YKQ2oouANbUtOoZF10CCADvX5D6JoF+XUPO/mM7ihuvT4Fo5s6Cg7sUCZ9f4R33VsPLwH0t2hlgNVCIMK+9+Z/a21I7xhzsz6qP7b0ssc=
---
马上验证令牌...
验证通过!
恢复的用户ID: user-langya-furry
验证结果是否匹配? true
---
测试无效令牌...
篡改过的令牌验证通过吗? false
---
测试过期令牌 (等待 4 秒)...
短期令牌: 9OFbwC/Cj0noZN9DPfShj6KVTD3bn/fTHflfo5gDQInyUHyxHbXOoPdNTElMOcJITA9BjlUGsLQ7/b8WtbPjhvJCPlKooKzMeRf0TEietWuZI3mEvN/hbEU=
过期令牌验证通过吗? false
---