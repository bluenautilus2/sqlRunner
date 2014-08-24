package org.bluenautilus.data;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.bluenautilus.util.ConfigUtil;

/**
 * Created by bstevens on 8/24/14.
 */
public class CassFieldItems {


    private String scriptFolderField = "~/repo/tools/CQL_Update_Scripts";
    private String hostField = "nucleus";

    public CassFieldItems(String scriptFolderField, String hostField) {
        this.scriptFolderField = scriptFolderField;
        this.hostField = hostField;
    }

    public static CassFieldItems createFromConfig(ConfigUtil config) throws Exception {

        PropertiesConfiguration propConf = config.getDBConfig();
        CassFieldItems items =
                new CassFieldItems(propConf.getString(ConfigUtil.CASS_SCRIPT_FOLDER), propConf.getString(ConfigUtil.CASS_HOST_NAME));

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


}
