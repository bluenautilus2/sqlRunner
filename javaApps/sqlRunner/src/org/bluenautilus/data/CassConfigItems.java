package org.bluenautilus.data;

import org.codehaus.jackson.annotate.JsonIgnore;

import java.io.File;
import java.util.UUID;

/**
 * Created by bstevens on 8/24/14.
 */
public class CassConfigItems extends UuidItem {

    private String scriptFolderField = "/home/bstevens/repos/App/tools/CQL_Update_Scripts";
    private String hostField = "nucleus";
    private String useCertificate = "false";
    private String namespace = "pa";
    private String certificateFileField= "/home/bstevens/.ssh/stratum-west.pem";

    public CassConfigItems(UUID uuid,String scriptFolderField, String hostField, String useCertificate, String namespace, String certificateFileField) {
        this.scriptFolderField = scriptFolderField;
        this.hostField = hostField;
        this.useCertificate = useCertificate;
        this.namespace = namespace;
        this.certificateFileField = certificateFileField;
        this.uniqueId = uuid;
    }

    public CassConfigItems(){
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

    public String getUseCertificate() {
        return useCertificate;
    }

    public void setUseCertificate(String useCertificate) {
        this.useCertificate = useCertificate;
    }

    public String getCertificateFileField() {
        return certificateFileField;
    }

    public void setCertificateFileField(String certificateFileField) {
        this.certificateFileField = certificateFileField;
    }



    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public boolean useCertificate(){
        return new Boolean(this.getUseCertificate());
    }

    @JsonIgnore
    public boolean certFileExists(){
        File file = new File(this.certificateFileField);
        return file.exists();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CassConfigItems)) return false;

        CassConfigItems that = (CassConfigItems) o;

        if (certificateFileField != null ? !certificateFileField.equals(that.certificateFileField) : that.certificateFileField != null)
            return false;
        if (hostField != null ? !hostField.equals(that.hostField) : that.hostField != null) return false;
        if (namespace != null ? !namespace.equals(that.namespace) : that.namespace != null) return false;
        if (scriptFolderField != null ? !scriptFolderField.equals(that.scriptFolderField) : that.scriptFolderField != null)
            return false;
        if (useCertificate != null ? !useCertificate.equals(that.useCertificate) : that.useCertificate != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = scriptFolderField != null ? scriptFolderField.hashCode() : 0;
        result = 31 * result + (hostField != null ? hostField.hashCode() : 0);
        result = 31 * result + (useCertificate != null ? useCertificate.hashCode() : 0);
        result = 31 * result + (namespace != null ? namespace.hashCode() : 0);
        result = 31 * result + (certificateFileField != null ? certificateFileField.hashCode() : 0);
        return result;
    }


    @Override
    @JsonIgnore
    public String getTableDisplayString() {
        return namespace+"@"+hostField;
    }

    public UuidItem clone(){
        CassConfigItems cloned = new CassConfigItems();
        cloned.generateUniqueId();
        cloned.setHostField(this.getHostField());
        cloned.setUseCertificate(this.getUseCertificate());
        cloned.setCertificateFileField(this.getCertificateFileField());
        cloned.setScriptFolderField(this.getScriptFolderField());
        cloned.setNamespace(this.getNamespace());

        return cloned;
    }


}
