package org.bluenautilus;

import org.bluenautilus.data.FieldItems;
import org.bluenautilus.db.RefreshAction;
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

    public static void main(String[] args) {
        JFrame frame = new JFrame("SQL Script Runner 3000");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //  JPanel outermostPanel = new JPanel(new GridBagLayout());
        JPanel outermostPanel = new JPanel(new BorderLayout());

        SqlScriptTablePanel tableHolderPanel = new SqlScriptTablePanel();
        ScriptViewPanel scriptViewPanel = new ScriptViewPanel();
        OutputPanel outputPanel = new OutputPanel();
        SqlButtonPanel buttonPanel = new SqlButtonPanel(new FieldItems());

        JPanel centerPanel = new JPanel();
        centerPanel.add(scriptViewPanel);
        centerPanel.add(outputPanel);

        outermostPanel.add(buttonPanel, BorderLayout.NORTH);
        outermostPanel.add(tableHolderPanel, BorderLayout.WEST);
        outermostPanel.add(centerPanel, BorderLayout.CENTER);

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
