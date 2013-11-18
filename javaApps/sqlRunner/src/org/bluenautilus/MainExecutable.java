package org.bluenautilus;

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

    public static void main(String[] args) {
        JFrame frame = new JFrame("SQL Script Runner 3000");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //  JPanel outermostPanel = new JPanel(new GridBagLayout());
        JPanel outermostPanel = new JPanel(new BorderLayout());

        SqlScriptTablePanel tableHolderPanel = new SqlScriptTablePanel();
        ScriptViewPanel scriptViewPanel = new ScriptViewPanel();
        OutputPanel outputPanel = new OutputPanel();
        SqlButtonPanel buttonPanel = new SqlButtonPanel(new FieldItems());

        JPanel centerPanel = new JPanel(new GridBagLayout());

        //int gridx, int gridy,int gridwidth, int gridheight,
        //double weightx, double weighty,
        // int anchor, int fill,
        //Insets insets, int ipadx, int ipady

        centerPanel.add(tableHolderPanel, new GridBagConstraints(0, 0, 1, 2, 0.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
                new Insets(2, 2, 2, 2), 2, 2));
        centerPanel.add(scriptViewPanel, new GridBagConstraints(1, 0, 1, 2, 0.5, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH,
                new Insets(2, 2, 2, 2), 2, 2));
        centerPanel.add(outputPanel, new GridBagConstraints(2, 0, 1, 2, 0.3, 1.0,
                GridBagConstraints.EAST, GridBagConstraints.BOTH,
                new Insets(2, 2, 2, 2), 2, 2));


        outermostPanel.add(buttonPanel, BorderLayout.NORTH);
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
