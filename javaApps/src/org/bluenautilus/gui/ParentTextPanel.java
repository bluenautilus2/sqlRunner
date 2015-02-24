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

        scroll.setPreferredSize(new Dimension(350, 500));
        scroll.setMaximumSize(new Dimension(350, 1000));
        scroll.setMinimumSize(new Dimension(200, 500));

        this.add(scroll,BorderLayout.CENTER);

    }

    public void clearText() {
        textArea.setText("");
    }


}
