package org.bluenautilus.gui.dataStoreGroupConfiguration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bluenautilus.OldExecutable;
import org.bluenautilus.gui.PrettyButtonListener;
import org.bluenautilus.util.DataStoreGroupConfigUtil;

/**
 * Created by bstevens on 1/27/15.
 */
public class DataStorePanelManager implements PrettyButtonListener {

    private static Log log = LogFactory.getLog(OldExecutable.class);

    DataStoreGroupConfigUtil theConfigurationFromFile = null;

    public DataStorePanelManager(DataStoreGroupPanel dsgPanel) {
        dsgPanel.addListener(this);
        theConfigurationFromFile = new DataStoreGroupConfigUtil();
    }


    @Override
    public void buttonClicked(ButtonType type) {
        log.info("panel manager got: " + type);
    }
}
