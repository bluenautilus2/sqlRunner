package org.bluenautilus.script;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;


/**
 * Created with IntelliJ IDEA.
 * User: bstevens
 * Date: 8/31/13
 * Time: 6:24 PM
 * <p/>
 * This class modifies the original sql script and makes a copy with extra
 * instructions.
 * <p/>
 * Currently we use the same name for every file because we run one script at a time.
 * I never ever want this program to run more than one script at once.
 */
public class ScriptModifier implements ScriptCompletionListener {

	public static final String OUTPUTFILENAME = "tempdata.txt";
	private static final String PREFIX = "BEGIN TRANSACTION TEMP;\n\n";
	private static final String POSTFIX = "COMMIT TRANSACTION TEMP;\n\n";
	//Use this string to determine whether the sql script contains a
	//stored procedure. We parse differently if so.
	private static final String SP_FLAG = "CREATE PROCEDURE";
	public File inputFile;
	public File outputFile;


	public ScriptModifier(File inputFile) {
		this.inputFile = inputFile;

	}


	@Override
	public void scriptComplete(ScriptResultsEvent e) {
		if (this.outputFile.exists()) {
			//maybe later when I trust this class more
			//   this.outputFile.delete();
		}
	}


	public File createModifiedCopy() throws FileNotFoundException, IOException {
		FileInputStream fis;
		OutputStream outputStream;
		BufferedReader reader = null;
		BufferedWriter writer = null;
		String line;

		outputFile = new File(OUTPUTFILENAME);

		try {
			if (outputFile.exists()) {
				outputFile.delete();
			}

			outputStream = new FileOutputStream(outputFile);
			writer = new BufferedWriter(new OutputStreamWriter(outputStream, Charset.forName("UTF-8")));

			fis = new FileInputStream(this.inputFile);

            boolean isStoredProc = this.parseFileForStoreProcedures(fis);

            //have to reset the input stream back to beginning
            fis.getChannel().position(0);

            reader = new BufferedReader(new InputStreamReader(fis, Charset.forName("UTF-8")));

			if (!isStoredProc) {
				writer.write(PREFIX);
			}

			int count = 0;

			boolean munchingComments = false;

			while ((line = reader.readLine()) != null) {

				StringBuilder newLine = new StringBuilder();
				int totalChars = line.length();
				int i = 0;

				while (i < totalChars) {
					char char1 = line.charAt(i);
					char char2 = char1;
					if ((i + 1) < totalChars) {
						char2 = line.charAt(i + 1);
					}
					if (char1 == '/' && char2 == '*') {
						munchingComments = true;
					}
					if (char1 == '*' && char2 == '/') {
						munchingComments = false;

						if ((i + 2) >= totalChars) {
							i = totalChars;
							char1 = ' ';
						} else {
							i = i + 2;
							char1 = line.charAt(i);
						}
					}
					if (!munchingComments) {
						newLine.append(char1);
					}
					i++;
				} //end of processing string

				writer.write(newLine.toString() + "\n");
			}
			if (!isStoredProc) {
				writer.write(POSTFIX);
			}

		} finally {
			if (null != reader) {
				reader.close();
			}
			if (null != writer) {
				writer.close();
			}
		}
		return outputFile;
	}


	private boolean clearofMarkers(String input) {
		if (input.indexOf(PREFIX) == -1 && input.indexOf(POSTFIX) == -1) {
			return true;
		}
		return false;
	}


	/**
	 * Returns true if a stored procedure command is found
	 * within the first 200 lines of the script.
	 * @param fis
	 * @return
	 */
	private boolean parseFileForStoreProcedures(FileInputStream fis) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(fis, Charset.forName("UTF-8")));
		String line;
		int count = 0;

		while (((line = reader.readLine()) != null) && (count < 200)) {

			if (line != null) {
				count++;
				line = line.toLowerCase();
				String flag = SP_FLAG.toLowerCase();
                System.out.println("Looking for: " + flag + " in " + line+"\n");
				if (line.indexOf(flag) != -1) {
                    System.out.println("\nfound it\n\n");
						return true;

				}
			}
		}

		return false;
	}

}
