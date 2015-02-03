package org.bluenautilus.data;

import org.bluenautilus.db.DBConnectionType;
import org.codehaus.jackson.annotate.JsonIgnore;


/**
 * User: bluenautilus2
 * Date: 7/30/13
 * Time: 8:31 PM
 */
public class SqlConfigItems extends UuidItem {

    //These are just defaults if the config file isn't found.
    private String dbNameField = "panswersClean";
    private String loginField = "common";
    private String passwordField = "xxxPA50";
    private String scriptFolderField = "~/repo/tools/SQL_Update_Scripts";
    private String ipAddressField = "10.10.10.53";
    private String port = "1433";
    private String dbConnectionType = DBConnectionType.getDefaultForThisOS().toString();

    public SqlConfigItems(String dbNameField, String loginField, String passwordField, String scriptFolderField, String ipAddressField, String port, String dbConnectionType) {
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
        if (port != null) {
            this.port = port;
        }
        if (dbConnectionType != null) {
            this.dbConnectionType = dbConnectionType;
        }
    }


    public SqlConfigItems() {
        //does nothing
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

        if (dbConnectionType != null ? !dbConnectionType.equals(that.dbConnectionType) : that.dbConnectionType != null)
            return false;
        if (dbNameField != null ? !dbNameField.equals(that.dbNameField) : that.dbNameField != null) return false;
        if (ipAddressField != null ? !ipAddressField.equals(that.ipAddressField) : that.ipAddressField != null)
            return false;
        if (loginField != null ? !loginField.equals(that.loginField) : that.loginField != null) return false;
        if (passwordField != null ? !passwordField.equals(that.passwordField) : that.passwordField != null)
            return false;
        if (port != null ? !port.equals(that.port) : that.port != null) return false;
        if (scriptFolderField != null ? !scriptFolderField.equals(that.scriptFolderField) : that.scriptFolderField != null)
            return false;

        return true;
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
        return dbNameField+"@"+ipAddressField+","+port;
    }
}
