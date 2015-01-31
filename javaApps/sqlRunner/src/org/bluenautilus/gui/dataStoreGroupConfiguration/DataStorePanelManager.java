package org.bluenautilus.gui.dataStoreGroupConfiguration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bluenautilus.OldExecutable;
import org.bluenautilus.gui.PrettyButtonListener;

/**
 * Created by bstevens on 1/27/15.
 */
public class DataStorePanelManager implements PrettyButtonListener {

    private static Log log = LogFactory.getLog(OldExecutable.class);


    public DataStorePanelManager(DataStoreGroupPanel dsgPanel) {
        dsgPanel.addListener(this);
    }


    @Override
    public void prettyButtonClicked(ButtonType type) {
        log.info("panel manager got: " + type);
    }



}
