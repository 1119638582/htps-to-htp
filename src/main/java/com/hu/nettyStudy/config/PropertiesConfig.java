package com.hu.nettyStudy.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author HuNingHao
 * @version 1.0
 * @date 2024/4/12 15:13
 */
public class PropertiesConfig {

    public static final Logger logger = LoggerFactory.getLogger(PropertiesConfig.class);

    private static final Properties properties = new Properties();

    // 静态代码块，在类加载时执行
    static {
        load("htp.properties");
    }

    /**
     * 加载properties文件
     * @param filePaths 配置文件路径数组
     */
    private static void load(String... filePaths) {
        for (String filePath : filePaths) {
            try {
                InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
                properties.load(resourceAsStream);
                logger.info("Loaded properties from file: {}", filePath);
            } catch (IOException ex) {
                logger.error("Failed to load properties from file: {}", filePath);
                ex.printStackTrace();
            }
        }
    }

    /**
     * 获取所有配置文件中的属性值
     * @param key 属性键名
     * @return 属性值，如果属性不存在则返回null
     */
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    /**
     * 获取指定配置文件中的属性值
     * @param filePath 配置文件路径
     * @param key 属性键名
     * @return 属性值，如果属性不存在则返回null
     */
    public static String getPropertyFrom(String filePath, String key) {
        Properties specificProperties = new Properties();
        try (InputStream input = new FileInputStream(filePath)) {
            specificProperties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return specificProperties.getProperty(key);
    }

    /**
     * 获取属性值，如果属性不存在则返回默认值
     * @param key 属性键名
     * @param defaultValue 默认值
     * @return 属性值或默认值
     */
    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    /**
     * 设置属性值
     * @param key 属性键名
     * @param value 属性值
     */
    public static void setProperty(String key, String value) {
        properties.setProperty(key, value);
    }

    /**
     * 保存属性到文件
     * @param filePath 文件路径
     */
    public static void save(String filePath) {
        try {
            properties.store(new FileOutputStream(filePath), null);
        } catch (IOException ex) {
            logger.error("Failed to save properties to file: {}", filePath);
            ex.printStackTrace();
        }
    }
}
