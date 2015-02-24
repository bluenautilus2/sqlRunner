package org.bluenautilus.data;

import org.bluenautilus.db.DBConnectionType;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.util.UUID;


/**
 * User: bluenautilus2
 * Date: 7/30/13
 * Time: 8:31 PM
 */
public class SqlConfigItems extends UuidConfigItem {

    //These are just defaults if the config file isn't found.
    private String dbNameField = "panswersClean";
    private String loginField = "common";
    private String passwordField = "xxxPA50";
    private String scriptFolderField = "C:/repos/App/tools/SQL_Update_Scripts";
    private String ipAddressField = "10.10.10.53";
    private String port = "1433";
    private String dbConnectionType = DBConnectionType.getDefaultForThisOS().toString();

    public SqlConfigItems(UUID uuid, String dbNameField, String loginField, String passwordField, String scriptFolderField, String ipAddressField, String port, String dbConnectionType) {
        this.dbNameField = dbNameField;
        this.loginField = loginField;
        this.passwordField = passwordField;
        this.scriptFolderField = scriptFolderField;
        this.ipAddressField = ipAddressField;
        this.port = port;
        this.dbConnectionType = dbConnectionType;
        this.uniqueId = uuid;
    }

    public SqlConfigItems() {
        //do nothing.. remember this is json pojo
    }


    public String getDbNameField() {
        return dbNameField;
    }

    public void setDbNameField(String dbNameField) {
        this.dbNameField = dbNameField;
    }

    public String getLoginField() {
        return loginField;
    }

    public void setLoginField(String loginField) {
        this.loginField = loginField;
    }

    public String getPasswordField() {
        return passwordField;
    }

    public void setPasswordField(String passwordField) {
        this.passwordField = passwordField;
    }

    public String getScriptFolderField() {
        return scriptFolderField;
    }

    public void setScriptFolderField(String scriptFolderField) {
        this.scriptFolderField = scriptFolderField;
    }

    public String getIpAddressField() {
        return ipAddressField;
    }

    public void setIpAddressField(String ipAddressField) {
        this.ipAddressField = ipAddressField;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getDbConnectionType() {
        return dbConnectionType;
    }

    public void setDbConnectionType(String dbConnectionType) {
        this.dbConnectionType = dbConnectionType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SqlConfigItems)) return false;

        SqlConfigItems that = (SqlConfigItems) o;

        if (this.getUniqueId().equals(that.getUniqueId())) {
            return true;
        }
        return false;

    }

    @Override
    public int hashCode() {
        int result = dbNameField != null ? dbNameField.hashCode() : 0;
        result = 31 * result + (loginField != null ? loginField.hashCode() : 0);
        result = 31 * result + (passwordField != null ? passwordField.hashCode() : 0);
        result = 31 * result + (scriptFolderField != null ? scriptFolderField.hashCode() : 0);
        result = 31 * result + (ipAddressField != null ? ipAddressField.hashCode() : 0);
        result = 31 * result + (port != null ? port.hashCode() : 0);
        result = 31 * result + (dbConnectionType != null ? dbConnectionType.hashCode() : 0);
        return result;
    }

    @Override
    @JsonIgnore
    public String getTableDisplayString() {
        return dbNameField + "@" + ipAddressField;
    }

    @Override
    @JsonIgnore
    public UuidConfigItem clone() {
        SqlConfigItems cloned = new SqlConfigItems();
        cloned.generateUniqueId();
        cloned.setPasswordField(this.getPasswordField());
        cloned.setScriptFolderField(this.getScriptFolderField());
        cloned.setPort(this.getPort());
        cloned.setLoginField(this.getLoginField());
        cloned.setIpAddressField(this.getIpAddressField());
        cloned.setDbNameField(this.getDbNameField());
        return cloned;

    }
}
