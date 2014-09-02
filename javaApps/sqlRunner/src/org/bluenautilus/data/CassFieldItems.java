package org.bluenautilus.data;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.bluenautilus.util.ConfigUtil;

import java.io.File;

/**
 * Created by bstevens on 8/24/14.
 */
public class CassFieldItems {


    private String scriptFolderField = "/home/bstevens/repos/App/tools/CQL_Update_Scripts";
    private String hostField = "nucleus";
    private String useCertificate = "false";
    private String certificateFileField= "/home/bstevens/.ssh/stratum-west.pem";

    public CassFieldItems(String scriptFolderField, String hostField, String useCertificate, String certificateFileField) {
        this.scriptFolderField = scriptFolderField;
        this.hostField = hostField;
        this.useCertificate = useCertificate;
        this.certificateFileField = certificateFileField;
    }

    public static CassFieldItems createFromConfig(ConfigUtil config) throws Exception {

        PropertiesConfiguration propConf = config.getCassConfig();
        CassFieldItems items =
                new CassFieldItems(propConf.getString(ConfigUtil.CASS_SCRIPT_FOLDER), propConf.getString(ConfigUtil.CASS_HOST_NAME),
                propConf.getString(ConfigUtil.USE_CERT), propConf.getString(ConfigUtil.CERT_FILE));

        return items;
    }

    public CassFieldItems(){
        //does nothing
    }

    public String getScriptFolderField() {
        return scriptFolderField;
    }


    public String getHostField() {
        return hostField;
    }

    public String getUseCertificate() {
        return useCertificate;
    }

    public String getCertificateFileField() {
        return certificateFileField;
    }

    public boolean useCertificate(){
        return new Boolean(this.getUseCertificate());
    }

    public boolean certFileExists(){
        File file = new File(this.certificateFileField);
        return file.exists();
    }
}
