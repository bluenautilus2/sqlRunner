package org.bluenautilus.data;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.bluenautilus.util.ConfigUtil;

/**
 * User: bluenautilus2
 * Date: 7/30/13
 * Time: 8:31 PM
 */
public class FieldItems {

    private String dbNameField = "panswersClean";
    private String loginField = "common";
    private String passwordField = "xxxPA50";
    private String scriptFolderField = "~/repo/tools/SQL_Update_Scripts";
    private String ipAddressField = "10.10.10.53";

    public FieldItems(String dbNameField, String loginField, String passwordField, String scriptFolderField, String ipAddressField) {
        if (dbNameField != null) {
            this.dbNameField = dbNameField;
        }
        if (loginField != null) {
            this.loginField = loginField;
        }
        if (passwordField != null) {
            this.passwordField = passwordField;
        }
        if (scriptFolderField != null) {
            this.scriptFolderField = scriptFolderField;
        }
        if (ipAddressField != null) {
            this.ipAddressField = ipAddressField;
        }
    }

    public FieldItems() {
        //does nothing
    }

    public static FieldItems createFromConfig(ConfigUtil config) throws Exception {

        PropertiesConfiguration propConf = config.getDBConfig();
        FieldItems items = new FieldItems(propConf.getString(ConfigUtil.DB_NAME),
                propConf.getString(ConfigUtil.DB_USER),
                propConf.getString(ConfigUtil.DB_PASS),
                propConf.getString(ConfigUtil.SCRIPT_FOLDER),
                propConf.getString(ConfigUtil.DB_IP));

        return items;
    }

    public String getDbNameField() {
        return dbNameField;
    }

    public String getLoginField() {
        return loginField;
    }

    public String getPasswordField() {
        return passwordField;
    }

    public String getScriptFolderField() {
        return scriptFolderField;
    }

    public String getIpAddressField() {
        return ipAddressField;
    }


}