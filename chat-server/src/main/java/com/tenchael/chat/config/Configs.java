package com.tenchael.chat.config;

import com.tenchael.chat.ChatServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Properties;

public class Configs {

    private static final Logger LOGGER = LoggerFactory.getLogger(Configs.class);
    private static final Properties properties = new Properties();
    public static String defaultWebAppBase;

    static {
        init();
    }


    private static void init() {
        String pathProp = System.getProperty(Constants.PROPS_FILE);
        if (pathProp == null || pathProp.trim().length() == 0) {
            LOGGER.info("do not config properties file, use default config");
            try (InputStream input = ChatServer.class.getClassLoader()
                    .getResourceAsStream("app.properties")) {
                properties.load(input);
            } catch (IOException ex) {
                LOGGER.error("read properties error", ex);
            }
        } else {
            LOGGER.info("config file: {}", pathProp);
            try (InputStream input = new FileInputStream(pathProp)) {
                properties.load(input);
            } catch (IOException ex) {
                LOGGER.error("read properties error", ex);
            }
        }


        // setting default webApp base
        try {
            defaultWebAppBase = Configs.class.getProtectionDomain()
                    .getCodeSource().getLocation().toURI().toString() + "webapp/";
            defaultWebAppBase = !defaultWebAppBase.contains("file:") ? defaultWebAppBase : defaultWebAppBase.substring(5);
            LOGGER.info("default webAppBase: {}", defaultWebAppBase);
        } catch (URISyntaxException e) {
            LOGGER.error("uri syntax error", e);
        }
    }


    public static String get(String key, String defaultVal) {
        String val = get(key);
        if (val == null) {
            return defaultVal;
        }
        return val;
    }

    public static String get(String key) {
        Object value = properties.get(key);
        if (value != null) {
            return value.toString();
        } else {
            return null;
        }
    }

    public static Integer getInt(String key) {
        String val = get(key);
        if (val == null || val.trim().length() == 0) {
            return null;
        } else {
            return Integer.valueOf(val);
        }
    }

    public static Integer getInt(String key, Integer defaultVal) {
        Integer val = getInt(key);
        if (val == null) {
            return defaultVal;
        }
        return Integer.valueOf(val);
    }


}
