package org.bluenautilus;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bluenautilus.gui.dataStoreGroupConfiguration.DataStoreGroupPanel;
import org.bluenautilus.gui.dataStoreGroupConfiguration.DataStorePanelManager;
import org.bluenautilus.util.CassConfigUtil;
import org.bluenautilus.util.DataStoreGroupConfigUtil;
import org.bluenautilus.util.SqlConfigUtil;

import javax.swing.*;
import java.awt.*;

/**
 * Created by bstevens on 1/26/15.
 */
public class MainExecutable {

    private static Log log = LogFactory.getLog(MainExecutable.class);

    public static void main(String[] args) {

        JFrame frame = new JFrame("SQL Script Runner Ludicrous Edition");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        log.info("sqlRunner starting: Ludicrous Edition");

        CassConfigUtil.readInConfiguration();
        SqlConfigUtil.readInConfiguration();
        DataStoreGroupConfigUtil.readInConfiguration();

        final DataStoreGroupPanel dataStoreGroupPanel = new DataStoreGroupPanel();
        dataStoreGroupPanel.init();

        DataStorePanelManager mgr = new DataStorePanelManager(dataStoreGroupPanel);





        frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);
        frame.pack();

        //make it a little bigger
        Dimension framesize = frame.getSize();

        int fwidth = Math.round(framesize.width * 1.3f);
        int fheight = Math.round(framesize.height * 1.2f);

        Dimension newframesize = new Dimension(fwidth,fheight);
        frame.setSize(newframesize);
        frame.setVisible(true);



    }
}
