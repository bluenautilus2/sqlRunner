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
        super(new BorderLayout());

        textArea = new JTextArea();

        textArea.setEditable(false);

        textArea.setLineWrap(false);

        JScrollPane scroll = new JScrollPane(textArea);

        this.add(scroll,BorderLayout.CENTER);

    }

    public void clearText() {
        textArea.setText("");
    }


}
