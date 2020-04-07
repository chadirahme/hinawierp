package config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DevelopmentConfig {

    public static String getLogFilesPath(){
        String path="";
        try (InputStream input = DevelopmentConfig.class.getClassLoader().getResourceAsStream("config.properties")) {

            Properties prop = new Properties();

            // load a properties file
            prop.load(input);

            // get the property value and print it out
            path=prop.getProperty("logFilePath");

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return path;
    }
}
