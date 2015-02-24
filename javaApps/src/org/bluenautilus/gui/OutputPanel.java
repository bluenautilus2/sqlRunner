package org.bluenautilus.gui;

import java.io.IOException;

/**
 * User: bluenautilus2
 * Date: 7/28/13
 * Time: 10:44 PM
 */
public class OutputPanel extends ParentTextPanel {

    public OutputPanel() {
        super();
        textArea.setLineWrap(true);
        clearText();
    }

    public void setText(String theString) throws IOException {
        textArea.setText("");
        textArea.append(theString);
        textArea.setCaretPosition(0);
    }

    public void appendText(String s){
        textArea.append(s);
        textArea.setCaretPosition(0);
    }

    public void clearText() {
        textArea.setText("Run a script to see output");
    }


}

