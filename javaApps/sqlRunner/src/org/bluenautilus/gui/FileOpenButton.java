package org.bluenautilus.gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Created by bstevens on 8/26/14.
 */
public class FileOpenButton extends JButton {


    public FileOpenButton(final JPanel parent, final JTextField field){
        super("File...");

        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final JFileChooser fc = new JFileChooser();
                fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int returnVal = fc.showOpenDialog(parent);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    field.setText(file.getAbsolutePath());
                }
            }
        };

        this.addActionListener(actionListener);

    }
}
