package org.bluenautilus.util;

import org.bluenautilus.data.FieldItems;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import javax.swing.*;
import java.io.*;


/**
 * User: bluenautilus2
 * Date: 5/3/13
 * Time: 11:20 PM
 */
public class ConfigUtil {

    public static final String DB_NAME = "db";
    public static final String DB_IP = "ip";
    public static final String DB_USER = "user";
    public static final String DB_PASS = "password";
    public static final String SCRIPT_FOLDER = "script_folder";
    private static final String CONFIG_FILENAME = "db.properties";
    private static final String BAK_FILENAME = "db.properties.old";
    private static PropertiesConfiguration dbConfig = null;

    public ConfigUtil() throws ConfigurationException, IOException {
        this.getDBConfig();
    }

    public static void saveOffCurrent(FieldItems items, JPanel parent) {
        try {
            File backup = new File(BAK_FILENAME);
            if (backup.exists()) {
                boolean success = backup.delete();
                if (!success) {
                    throw new Exception("Could not delete " + backup.getAbsolutePath());
                }
            }

            File configFile = new File(CONFIG_FILENAME);

            if (configFile.exists()) {
                boolean success = configFile.renameTo(backup);
                if (!success) {
                    throw new Exception("Could not rename " + configFile.getAbsolutePath() + " to " + backup.getAbsolutePath());
                }
            }


            File newFile = new File(CONFIG_FILENAME);
            newFile.createNewFile();

            PropertiesConfiguration newProp = new PropertiesConfiguration();

            newProp.addProperty(SCRIPT_FOLDER, items.getScriptFolderField());
            newProp.addProperty(DB_IP, items.getIpAddressField());
            newProp.addProperty(DB_NAME, items.getDbNameField());
            newProp.addProperty(DB_PASS, items.getPasswordField());
            newProp.addProperty(DB_USER, items.getLoginField());

            newProp.save(newFile);

        } catch (Exception ex) {
            GuiUtil.showErrorModalDialog(ex, parent);
        }


    }

    public PropertiesConfiguration getDBConfig() throws ConfigurationException, IOException {
        if (ConfigUtil.dbConfig == null) {
            InputStream stream = null;
            try {
                File file = new File(CONFIG_FILENAME);
                stream = new FileInputStream(file);

                dbConfig = new PropertiesConfiguration();
                dbConfig.load(stream);
            } finally {
                if (stream != null) {
                    stream.close();
                }
            }
        }

        return dbConfig;
    }


}



