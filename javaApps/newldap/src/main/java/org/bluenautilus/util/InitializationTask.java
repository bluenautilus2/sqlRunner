package org.bluenautilus.util;



import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.logging.Logger;


/**
 * User: bluenautilus2
 * Date: 5/27/13
 * Time: 5:02 PM
 */
public class InitializationTask implements ServletContextListener {
    static Logger logger = Logger.getLogger(InitializationTask.class.getName());

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        try {
            ServletContext context = servletContextEvent.getServletContext();
            ConfigUtil config = new ConfigUtil();
			config.getWebConfig();
            context.setAttribute(ConfigUtil.ATTRIBUTE_NAME, config);

            LoggerUtil loggerUtil = new LoggerUtil();
            loggerUtil.initialize();

        } catch (Exception ex) {
            System.out.print(ex.getMessage());
            throw new RuntimeException(ex);
        }

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
       logger.info("Shutting down server");

    }

}
