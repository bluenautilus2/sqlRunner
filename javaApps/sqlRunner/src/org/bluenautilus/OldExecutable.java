package org.bluenautilus;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bluenautilus.data.CassConfigItems;
import org.bluenautilus.data.SqlConfigItems;
import org.bluenautilus.gui.CassButtonPanel;
import org.bluenautilus.gui.CassPanelMgr;
import org.bluenautilus.gui.OutputPanel;
import org.bluenautilus.gui.PanelMgr;
import org.bluenautilus.gui.ScriptViewPanel;
import org.bluenautilus.gui.SqlButtonPanel;
import org.bluenautilus.gui.SqlScriptTablePanel;
import org.bluenautilus.util.DataStoreGroupConfigUtil;
import org.bluenautilus.util.GuiUtil;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;


/**
 * User: bluenautilus2
 * Date: 7/27/13
 * Time: 6:27 PM
 */
public class OldExecutable {

    private static Log log = LogFactory.getLog(OldExecutable.class);
    private static boolean cassandraUnopened = true;

    public static void main(String[] args) {

		JFrame frame = new JFrame("SQL Script Runner Ultimate Edition");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


        log.info("sqlRunner starting");

        JPanel outermostSqlPanel = new JPanel(new BorderLayout());

        SqlScriptTablePanel tableHolderPanel = new SqlScriptTablePanel();
        ScriptViewPanel scriptViewPanel = new ScriptViewPanel();
        OutputPanel outputPanel = new OutputPanel();
        SqlButtonPanel buttonPanel = new SqlButtonPanel(new SqlConfigItems());

		JSplitPane innerSplitPane = new JSplitPane();
		innerSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		innerSplitPane.setLeftComponent(scriptViewPanel);
        innerSplitPane.setRightComponent(outputPanel);
        innerSplitPane.setDividerLocation(0.5);

		JSplitPane outerSplitPane = new JSplitPane();
		outerSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		outerSplitPane.setLeftComponent(tableHolderPanel);
		outerSplitPane.setRightComponent(innerSplitPane);
		outerSplitPane.setDividerLocation(0.5);

        outermostSqlPanel.add(buttonPanel, BorderLayout.NORTH);
        outermostSqlPanel.add(outerSplitPane, BorderLayout.CENTER);

        log.info("init Cassandra panels...");

        JPanel outermostCassPanel = new JPanel(new BorderLayout());

        SqlScriptTablePanel tableHolderPanelCass = new SqlScriptTablePanel();
        ScriptViewPanel scriptViewPanelCass = new ScriptViewPanel();
        OutputPanel outputPanelCass = new OutputPanel();
        CassButtonPanel buttonPanelCass = new CassButtonPanel(new CassConfigItems());

        JSplitPane innerSplitPaneCass = new JSplitPane();
        innerSplitPaneCass.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        innerSplitPaneCass.setLeftComponent(scriptViewPanelCass);
        innerSplitPaneCass.setRightComponent(outputPanelCass);
        innerSplitPaneCass.setDividerLocation(0.5);

        JSplitPane outerSplitPaneCass = new JSplitPane();
        outerSplitPaneCass.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        outerSplitPaneCass.setLeftComponent(tableHolderPanelCass);
        outerSplitPaneCass.setRightComponent(innerSplitPaneCass);
        outerSplitPaneCass.setDividerLocation(0.5);

        outermostCassPanel.add(buttonPanelCass, BorderLayout.NORTH);
        outermostCassPanel.add(outerSplitPaneCass, BorderLayout.CENTER);

        //---------------------------------
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("SQL DB", outermostSqlPanel);
        tabbedPane.addTab("Cassandra", outermostCassPanel);



        frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);
        frame.pack();

		//make it a little bigger
		Dimension framesize = frame.getSize();

		int fwidth = Math.round(framesize.width * 1.3f);
		int fheight = Math.round(framesize.height * 1.2f);

		Dimension newframesize = new Dimension(fwidth,fheight);
		frame.setSize(newframesize);
        frame.setVisible(true);

        DataStoreGroupConfigUtil c;
        SqlConfigItems fields = new SqlConfigItems();
        CassConfigItems cassFields = new CassConfigItems();

        try {
            c = new DataStoreGroupConfigUtil();
            fields = new SqlConfigItems();
            cassFields = new CassConfigItems();
        } catch (Exception e) {
            GuiUtil.showErrorModalDialog(e, outermostSqlPanel);
        }

        buttonPanel.setFields(fields);
        buttonPanelCass.setFields(cassFields);

        final CassPanelMgr cassPanelMgr = new CassPanelMgr(outputPanelCass, scriptViewPanelCass, tableHolderPanelCass, buttonPanelCass, frame);
        //don't refresh here.. refreshes when the Cassandra panel is selected for the first time.
        //see the tabbed panel's change listener above.

        ChangeListener changeListener = new ChangeListener() {
            public void stateChanged(ChangeEvent changeEvent) {
                JTabbedPane sourceTabbedPane = (JTabbedPane) changeEvent.getSource();
                int index = sourceTabbedPane.getSelectedIndex();
                if(index==1 && cassandraUnopened){

                    cassandraUnopened = false;
                    cassPanelMgr.refreshAction();
                }
            }
        };
        tabbedPane.addChangeListener(changeListener);

        final PanelMgr sqlPanelMgr = new PanelMgr(outputPanel, scriptViewPanel, tableHolderPanel, buttonPanel, frame);
        sqlPanelMgr.refreshAction();


    }



}
