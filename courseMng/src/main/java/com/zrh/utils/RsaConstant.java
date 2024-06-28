package com.zrh.utils;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;

public class RsaConstant {
    public static final String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCc9WzebY/Wd1AdC9wAUxUWQrekyvUS5h7aNDaKt19YnaSTuPZ20HB2Z21EjzU5iJsoH7mEajvqqRzvbq39OBrQQsFmQsz6faa6SG1+j0+ngK2UgeaHJxdFptnB2BmQRL6B0zIBPveYw0dJjsEBCpEBu4onvvqXnXbPS0dPJl37dwIDAQAB";

    public static final String PRIVATE_KEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJz1bN5tj9Z3UB0L3ABTFRZCt6TK9RLmHto0Noq3X1idpJO49nbQcHZnbUSPNTmImygfuYRqO+qpHO9urf04GtBCwWZCzPp9prpIbX6PT6eArZSB5ocnF0Wm2cHYGZBEvoHTMgE+95jDR0mOwQEKkQG7iie++pedds9LR08mXft3AgMBAAECgYBtAbnhZD/wF57o/VKGWy0a76zw4w9/V5h3vKlgf2fVeG5TNbLd43cnlD77zWWOloQDQr0p7Em+eEKjOr/VS8C6ZH4ZYBpj3kIvJh0cHSP19XOJbuD2N3ee2aASNib8qJuj7YWiBpSrACTgdkGw2aG64hxcoRE4ELIgsdPrQSlCIQJBAPUZQHQlNPihTFR6n1YbwND9XeHWDE8nqfjZXOGprxqVQvAmYlljKUVbKEbpsmggc1Z1IRUNCMWKdMi8h1yongcCQQCj8Jec1a3WNInGl1Tl8rwg/GGbAXY8DxenamlwQeOQIcjWrIYbvAMRr1B1VkCBARecjL1hJzVkPiBRo5tJSVsRAkEAvtkt1gKOz7cRb4qX/X3Y0yMm8k4xAYW6FXpzl4gAQCvPlp3c4QuUvFux7h3U/L2f8cd8vh6LIP6h1xjuMhleCwJAIkkMzCX8BxJRxhCcFsMfCR7/IrW5jnHxlnewuWNuPhRytkK6gU5erfImN4PkVZZu47SjXpAeoGpFxA0dSsRmoQJAOwb2LU/cNjpz/kq9ifjr19cQm+6pJlbJp12aNJflovyIC7Ntp2+WfQX0wLzbdQyCzvH3nH8ozUH/P6Mcf+xNmA==";

    public static final RSA rsa = new RSA(PRIVATE_KEY, PUBLIC_KEY);

    public static void main(String[] args) {
//公钥加密，私钥解密
        byte[] encrypt = rsa.encrypt(StrUtil.bytes("ROOT", CharsetUtil.CHARSET_UTF_8), KeyType.PublicKey);
        byte[] decrypt = rsa.decrypt(encrypt, KeyType.PrivateKey);
        System.out.println(new String(encrypt));
        System.out.println(new String(decrypt));

//Junit单元测试
//Assert.assertEquals("我是一段测试aaaa", StrUtil.str(decrypt, CharsetUtil.CHARSET_UTF_8));

//私钥加密，公钥解密
        byte[] encrypt2 = rsa.encrypt(StrUtil.bytes("我是一段测试aaaa", CharsetUtil.CHARSET_UTF_8), KeyType.PrivateKey);
        byte[] decrypt2 = rsa.decrypt(encrypt2, KeyType.PublicKey);
        System.out.println(new String(decrypt2));
    }
}
