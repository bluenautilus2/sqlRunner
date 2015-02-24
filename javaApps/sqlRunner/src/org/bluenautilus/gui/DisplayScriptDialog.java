package org.bluenautilus.gui;


import org.bluenautilus.util.GuiUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;

/**
 * Created by bstevens on 3/8/14.
 */
public class DisplayScriptDialog extends JFrame {

    File fileToDisplay;
    JTextArea textArea;
    JPanel parentPanel;
    JButton saveButton = new JButton("Save");

    public DisplayScriptDialog(String title, File file, JPanel parentPanel) throws IOException {
        super(title);
        this.fileToDisplay = file;
        this.parentPanel = parentPanel;

        textArea = new JTextArea(40, 60);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        textArea.setEditable(true);
        setText(file);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveFile(textArea.getText());
            }
        });

        JPanel outerPanel = new JPanel(new GridBagLayout());
        outerPanel.add(scrollPane, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(10,10,10,10), 2, 2));


        outerPanel.add(saveButton,new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(4,4,4,4), 2, 2));
        this.setContentPane(outerPanel);

        this.setDefaultCloseOperation(
                JDialog.DISPOSE_ON_CLOSE);
    }

    public void setText(File file) throws IOException {
        textArea.setText("");

        InputStream fis;
        BufferedReader reader = null;
        String line;

        try {
            fis = new FileInputStream(file);
            reader = new BufferedReader(new InputStreamReader(fis, Charset.forName("UTF-8")));

            while (((line = reader.readLine()) != null)) {
                textArea.append(line + "\n");
            }

        } finally {
            if (null != reader) {
                reader.close();
            }
        }
        textArea.setCaretPosition(0);

    }

    private void saveFile(String newContent) {
        try {
            String filename = this.fileToDisplay.getAbsolutePath();

            this.fileToDisplay.delete();
            File newFile = new File(filename);

            OutputStream outputStream = new FileOutputStream(newFile);
            Writer writer = new BufferedWriter(new OutputStreamWriter(outputStream, Charset.forName("UTF-8")));

            writer.write(newContent);
            writer.close();

        } catch (Exception e) {
            GuiUtil.showErrorModalDialog(e, parentPanel);
        }


    }


}
