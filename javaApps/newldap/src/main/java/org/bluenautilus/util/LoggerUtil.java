package org.bluenautilus.util;

// Import log4j classes.

import org.apache.commons.configuration.ConfigurationException;

import java.util.logging.Logger;

/**
 * User: bluenautilus2
 * Date: 6/16/13
 * Time: 6:37 PM
 */
public class LoggerUtil {


   public LoggerUtil() throws ConfigurationException {

    }

    public void initialize(){

        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.info("Logger configured.");

    }

}

