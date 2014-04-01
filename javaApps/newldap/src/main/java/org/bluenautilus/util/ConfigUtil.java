package org.bluenautilus.util;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import javax.servlet.ServletContext;
import java.io.InputStream;

/**
 * User: bluenautilus2
 * Date: 5/3/13
 * Time: 11:20 PM
 */
public class ConfigUtil {

    private static PropertiesConfiguration webConfig = null;

    private static final String WEB_FILENAME = "web.properties";

    public static final String ATTRIBUTE_NAME = ConfigUtil.class.getName();

    public ConfigUtil() throws ConfigurationException{

    }

    public static ConfigUtil getFromContext(ServletContext context){
         return (ConfigUtil)context.getAttribute(ATTRIBUTE_NAME);
    }

    public PropertiesConfiguration getWebConfig() throws ConfigurationException {
        if (ConfigUtil.webConfig == null) {

            InputStream input = getClass().getClassLoader().getResourceAsStream(WEB_FILENAME);

            webConfig = new PropertiesConfiguration();

            webConfig.load(input);
        }
        return webConfig;
    }



}


