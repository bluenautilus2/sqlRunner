package org.bluenautilus.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: bstevens
 * Date: 8/10/13
 * Time: 5:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class ParentTextPanel extends JPanel {
    protected JTextArea textArea;

    public ParentTextPanel() {
        super();
        textArea = new JTextArea(35, 35);
        textArea.setEditable(false);

        textArea.setLineWrap(false);

        JScrollPane scroll = new JScrollPane(textArea);

        //int gridx, int gridy,int gridwidth, int gridheight,
        //double weightx, double weighty,
        // int anchor, int fill,
        //Insets insets, int ipadx, int ipady

        this.add(scroll, new GridBagConstraints(0, 0, 1, 2, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(2, 2, 2, 2), 2, 2));

    }

    public void clearText() {
        textArea.setText("");
    }


}
