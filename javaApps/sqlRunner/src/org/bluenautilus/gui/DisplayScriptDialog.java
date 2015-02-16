package org.bluenautilus.gui;


import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * Created by bstevens on 3/8/14.
 */
public class DisplayScriptDialog extends JFrame {

	File fileToDisplay;
	JTextArea textArea;


	public DisplayScriptDialog(String title, File file, JPanel parentPanel) throws IOException{
	    super(title);
		this.fileToDisplay = file;

		textArea = new JTextArea(40, 60);

		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setVerticalScrollBarPolicy(
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		textArea.setEditable(true);
		setText(file);

		this.setContentPane(scrollPane);

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
			reader = null;
			fis = null;
		}
		textArea.setCaretPosition(0);

	}






}
