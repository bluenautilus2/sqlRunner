package org.bluenautilus.gui;

import org.bluenautilus.data.SqlScriptFile;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * User: bluenautilus2
 * Date: 7/28/13
 * Time: 10:44 PM
 */
public class ScriptViewPanel extends ParentTextPanel {

    public ScriptViewPanel() {
        super();
        clearText();
    }

    public void setText(SqlScriptFile theFile) throws IOException {
        textArea.setText("");

        InputStream fis;
        BufferedReader reader = null;
        String line;

        try {
            fis = new FileInputStream(theFile.getTheFile());
            reader = new BufferedReader(new InputStreamReader(fis, Charset.forName("UTF-8")));

            int count = 0;
            while (((line = reader.readLine()) != null) && count < 100) {
                textArea.append(line + "\n");
                count++;
            }
            if(count==100){
                textArea.append("\n*Script Truncated at 100 lines for GUI performance* --Beth\n");
            }
        } finally {
            if (null != reader) {
                reader.close();
            }
            reader = null;
            fis = null;
        }
        textArea.setCaretPosition(0);

    }

    public void clearText() {
        textArea.setText("Click a DB row to see the script               ");
    }


}
