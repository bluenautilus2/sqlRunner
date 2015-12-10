package org.bluenautilus.data;

import org.bluenautilus.cass.CassandraConnectionType;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.io.File;
import java.util.UUID;

/**
 * Created by bstevens on 8/24/14.
 */
public class CassConfigItems extends UuidConfigItem {

    private String scriptFolderField;
    private String hostField = "locahost";
    private String port = "9042";
    private String keyspace = "pa";
    private String connectionType = CassandraConnectionType.DOCKER_LOCAL.name();
    private String container = "dse47_cassandra_1";
    private String login = "docker";

    @JsonIgnore
    private String useCertificate;
    @JsonIgnore
    private String certificateFileField = "no longer used";


    public CassConfigItems(UUID myUuid, String scriptFolderField, String hostField, String port, String keyspace, String cassConnectionType, String container, String login) {
        this.uniqueId = myUuid;
        this.scriptFolderField = scriptFolderField;
        this.hostField = hostField;
        this.port = port;
        this.keyspace = keyspace;
        this.container = container;
        this.login = login;
    }

    public String getKeyspace() {
        return keyspace;
    }

    public void setKeyspace(String keyspace) {
        this.keyspace = keyspace;
    }

    public CassConfigItems() {
        //this is json pojo
    }

    public String getScriptFolderField() {
        return scriptFolderField;
    }

    public void setScriptFolderField(String scriptFolderField) {
        this.scriptFolderField = scriptFolderField;
    }

    public String getHostField() {
        return hostField;
    }

    public void setHostField(String hostField) {
        this.hostField = hostField;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getConnectionType() {
        return connectionType;
    }

    public void setConnectionType(String connectionType) {
        this.connectionType = connectionType;
    }

    public String getContainer() {
        return container;
    }

    public void setContainer(String container) {
        this.container = container;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * do not blow this away and regenerate
     *
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CassConfigItems)) return false;

        CassConfigItems that = (CassConfigItems) o;

        if (this.getUniqueId().equals(that.getUniqueId())) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (scriptFolderField != null ? scriptFolderField.hashCode() : 0);
        result = 31 * result + (hostField != null ? hostField.hashCode() : 0);
        result = 31 * result + (port != null ? port.hashCode() : 0);
        result = 31 * result + (keyspace != null ? keyspace.hashCode() : 0);
        return result;
    }

    @Override
    @JsonIgnore
    public String getTableDisplayString() {
        return keyspace + "@" + hostField;
    }

    public UuidConfigItem clone() {
        CassConfigItems cloned = new CassConfigItems();
        cloned.generateUniqueId();
        cloned.setHostField(this.getHostField());
        cloned.setScriptFolderField(this.getScriptFolderField());
        cloned.setPort(this.getPort());
        cloned.setKeyspace(this.getKeyspace());
        cloned.setConnectionType(this.getConnectionType());
        return cloned;
    }

    @JsonIgnore
    public String getUseCertificate() {
        return useCertificate;
    }

    @JsonIgnore
    public void setUseCertificate(String useCertificate) {
        this.useCertificate = useCertificate;
    }

    @JsonIgnore
    public String getCertificateFileField() {
        return certificateFileField;
    }

    @JsonIgnore
    public void setCertificateFileField(String certificateFileField) {
        this.certificateFileField = certificateFileField;
    }

}
