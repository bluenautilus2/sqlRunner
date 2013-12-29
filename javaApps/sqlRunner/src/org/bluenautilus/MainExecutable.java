package org.bluenautilus;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bluenautilus.data.FieldItems;
import org.bluenautilus.gui.OutputPanel;
import org.bluenautilus.gui.PanelMgr;
import org.bluenautilus.gui.ScriptViewPanel;
import org.bluenautilus.gui.SqlButtonPanel;
import org.bluenautilus.gui.SqlScriptTablePanel;
import org.bluenautilus.util.ConfigUtil;
import org.bluenautilus.util.GuiUtil;

import javax.swing.*;
import java.awt.*;


/**
 * User: bluenautilus2
 * Date: 7/27/13
 * Time: 6:27 PM
 */
public class MainExecutable {

    private static Log log = LogFactory.getLog(MainExecutable.class);

    public static void main(String[] args) {
        JFrame frame = new JFrame("SQL Script Runner Enterprise Edition");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        log.info("sqlRunner starting");

        //  JPanel outermostPanel = new JPanel(new GridBagLayout());
        JPanel outermostPanel = new JPanel(new BorderLayout());

        SqlScriptTablePanel tableHolderPanel = new SqlScriptTablePanel();
        ScriptViewPanel scriptViewPanel = new ScriptViewPanel();
        OutputPanel outputPanel = new OutputPanel();
        SqlButtonPanel buttonPanel = new SqlButtonPanel(new FieldItems());

		JSplitPane innerSplitPane = new JSplitPane();
		innerSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		innerSplitPane.setLeftComponent(scriptViewPanel);
        innerSplitPane.setRightComponent(outputPanel);
        innerSplitPane.setDividerLocation(0.5);

		JSplitPane outerSplitPane = new JSplitPane();
		outerSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		outerSplitPane.setLeftComponent(tableHolderPanel);
		outerSplitPane.setRightComponent(innerSplitPane);
		outerSplitPane.setDividerLocation(0.3);

        outermostPanel.add(buttonPanel, BorderLayout.NORTH);
        outermostPanel.add(outerSplitPane, BorderLayout.CENTER);

        frame.getContentPane().add(outermostPanel, BorderLayout.CENTER);

        frame.pack();
        frame.setVisible(true);

        ConfigUtil c;
        FieldItems fields = new FieldItems();
        try {
            c = new ConfigUtil();
            fields = FieldItems.createFromConfig(c);
        } catch (Exception e) {
            GuiUtil.showErrorModalDialog(e, outermostPanel);
        }

        buttonPanel.setFields(fields);

        PanelMgr panelMgr = new PanelMgr(outputPanel, scriptViewPanel, tableHolderPanel, buttonPanel);

        panelMgr.refreshAction();

    }


}
