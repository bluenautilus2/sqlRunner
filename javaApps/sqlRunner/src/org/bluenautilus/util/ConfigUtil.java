package org.bluenautilus.util;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.bluenautilus.data.CassFieldItems;
import org.bluenautilus.data.FieldItems;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


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
    public static final String PORT = "port";
    public static final String DB_CONNECT_TYPE = "db.connection.type";
    public static final String CASS_HOST_NAME = "cassandra_host";
    public static final String CASS_SCRIPT_FOLDER= "cassandra_script_folder";
    public static final String USE_CERT = "cass.usecert";  //true or false
    public static final String CERT_FILE = "cass.cert.filename";


    private static final String CONFIG_FILENAME = "db.properties";
    private static final String BAK_FILENAME = "db.properties.old";
    private static final String CASSANDRA_CONFIG_FILENAME= "cass.properties";
    private static final String CASSANDRA_BAK_FILENAME="cass.properties.old";



    private static PropertiesConfiguration dbConfig = null;
    private static PropertiesConfiguration cassConfig = null;

    public ConfigUtil() throws ConfigurationException, IOException {
        this.getDBConfig();
        this.getCassConfig();
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
            newProp.addProperty(PORT, items.getPort());
            newProp.addProperty(DB_CONNECT_TYPE, items.getDbConnectionType());

            newProp.save(newFile);

        } catch (Exception ex) {
            GuiUtil.showErrorModalDialog(ex, parent);
        }


    }

    public static void saveOffCurrent(CassFieldItems cassItems, JPanel parent) {
        try {
            File backup = new File(CASSANDRA_BAK_FILENAME);
            if (backup.exists()) {
                boolean success = backup.delete();
                if (!success) {
                    throw new Exception("Could not delete " + backup.getAbsolutePath());
                }
            }

            File configFile = new File(CASSANDRA_CONFIG_FILENAME);

            if (configFile.exists()) {
                boolean success = configFile.renameTo(backup);
                if (!success) {
                    throw new Exception("Could not rename " + configFile.getAbsolutePath() + " to " + backup.getAbsolutePath());
                }
            }


            File newFile = new File(CASSANDRA_CONFIG_FILENAME);
            newFile.createNewFile();

            PropertiesConfiguration newProp = new PropertiesConfiguration();

            newProp.addProperty(CASS_HOST_NAME, cassItems.getHostField());
            newProp.addProperty(CASS_SCRIPT_FOLDER, cassItems.getScriptFolderField());
            newProp.addProperty(USE_CERT, cassItems.getUseCertificate());
            newProp.addProperty(CERT_FILE, cassItems.getCertificateFileField());
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

    public PropertiesConfiguration getCassConfig() throws ConfigurationException, IOException {
        if (ConfigUtil.cassConfig == null) {
            InputStream stream = null;
            try {
                File file = new File(CASSANDRA_CONFIG_FILENAME);
                stream = new FileInputStream(file);

                cassConfig = new PropertiesConfiguration();
                cassConfig.load(stream);
            } finally {
                if (stream != null) {
                    stream.close();
                }
            }
        }
        return cassConfig;
    }

}



