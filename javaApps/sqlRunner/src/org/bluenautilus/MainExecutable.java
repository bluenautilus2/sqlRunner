package org.bluenautilus;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bluenautilus.gui.LaunchingTabManager;
import org.bluenautilus.gui.RunButtonPanel;
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

        JFrame frame = new JFrame("SQL Script Runner: Docker Edition");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        log.info("sqlRunner starting: Ludicrous Edition");

        CassConfigUtil.readInConfiguration();
        SqlConfigUtil.readInConfiguration();
        DataStoreGroupConfigUtil.readInConfiguration();

        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel topPanel = new JPanel(new BorderLayout());

        final DataStoreGroupPanel dataStoreGroupPanel = new DataStoreGroupPanel();
        dataStoreGroupPanel.init();
        topPanel.add(dataStoreGroupPanel, BorderLayout.EAST);
        final RunButtonPanel runButtonPanel = new RunButtonPanel();
        topPanel.add(runButtonPanel, BorderLayout.WEST);

        DataStorePanelManager mgr = new DataStorePanelManager(dataStoreGroupPanel);

        LaunchingTabManager tabManager = new LaunchingTabManager(runButtonPanel, mainPanel);
        dataStoreGroupPanel.addLaunchButtonListener(tabManager);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(tabManager.getTabbedPane(), BorderLayout.CENTER);

        frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
        frame.pack();

        //make it a little bigger
        //   Dimension framesize = frame.getSize();
        //   int fwidth = Math.round(framesize.width * 1.3f);
        //   int fheight = Math.round(framesize.height * 1.2f);
        //   Dimension newframesize = new Dimension(fwidth,fheight);
        //   frame.setSize(newframesize);

        frame.setVisible(true);


    }
}
