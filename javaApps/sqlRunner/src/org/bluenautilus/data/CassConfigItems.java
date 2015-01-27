package org.bluenautilus.data;

import java.io.File;

/**
 * Created by bstevens on 8/24/14.
 */
public class CassConfigItems extends ConfigItems {


    private String scriptFolderField = "/home/bstevens/repos/App/tools/CQL_Update_Scripts";
    private String hostField = "nucleus";
    private String useCertificate = "false";
    private String certificateFileField= "/home/bstevens/.ssh/stratum-west.pem";

    public CassConfigItems(String scriptFolderField, String hostField, String useCertificate, String certificateFileField) {
        this.scriptFolderField = scriptFolderField;
        this.hostField = hostField;
        this.useCertificate = useCertificate;
        this.certificateFileField = certificateFileField;
    }


    public CassConfigItems(){
        //does nothing
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

    public boolean useCertificate(){
        return new Boolean(this.getUseCertificate());
    }

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
        result = 31 * result + (certificateFileField != null ? certificateFileField.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CassConfigItems{" +
                "scriptFolderField='" + scriptFolderField + '\'' +
                ", hostField='" + hostField + '\'' +
                ", useCertificate='" + useCertificate + '\'' +
                ", certificateFileField='" + certificateFileField + '\'' +
                '}';
    }
}
