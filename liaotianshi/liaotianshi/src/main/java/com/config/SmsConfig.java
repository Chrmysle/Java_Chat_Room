package com.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class SmsConfig {
    private static Properties properties = new Properties();

    static {
        try {
            InputStream inputStream = SmsConfig.class.getClassLoader()
                    .getResourceAsStream("sms.properties");
            if (inputStream != null) {
                properties.load(inputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 阿里云访问密钥
    public static String getAccessKeyId() {
        return properties.getProperty("aliyun.accessKeyId", "");
    }

    public static String getAccessKeySecret() {
        return properties.getProperty("aliyun.accessKeySecret", "");
    }

    // 短信签名
    public static String getSignName() {
        return properties.getProperty("aliyun.signName", "");
    }

    // 短信模板CODE
    public static String getTemplateCode() {
        return properties.getProperty("aliyun.templateCode", "");
    }

    // 短信模板参数（验证码变量名）
    public static String getTemplateParam() {
        return properties.getProperty("aliyun.templateParam", "{\"code\":\"%s\"}");
    }

    // 是否启用真实短信发送（默认false，测试时使用）
    public static boolean isEnabled() {
        return Boolean.parseBoolean(properties.getProperty("sms.enabled", "false"));
    }
}