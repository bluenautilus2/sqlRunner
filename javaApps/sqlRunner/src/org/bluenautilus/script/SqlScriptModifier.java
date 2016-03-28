package org.bluenautilus.script;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
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
public class SqlScriptModifier implements ScriptCompletionListener {

	public static final String OUTPUTFILENAME = "tempdata.txt";
	private static final String PREFIX = "BEGIN TRANSACTION TEMP;\n\n";
	private static final String POSTFIX = "COMMIT TRANSACTION TEMP;\n\n";
	//Use this string to determine whether the sql script contains a
	//stored procedure. We parse differently if so.
	private static final String SP_FLAG = "CREATE PROCEDURE";
	public File inputFile;
	public File outputFile;


	public SqlScriptModifier(File inputFile) {
		this.inputFile = inputFile;

	}

	@Override
	public void scriptComplete(ScriptResultsEvent e) {
		if (this.outputFile.exists()) {
			//maybe later when I trust this class more
			//   this.outputFile.delete();
		}
	}

	public File createModifiedCopy() throws IOException, NoRunException {
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

			ParseResults results = this.parseFile(fis);
			boolean isStoredProc = results.isStoredProcedureFound();

			//have to reset the input stream back to beginning
			fis.getChannel().position(0);

			reader = new BufferedReader(new InputStreamReader(fis, Charset.forName("UTF-8")));

			if (!isStoredProc) {
				writer.write(PREFIX);
			}


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
	 * within the first 250 lines of the script.
	 *
	 * @param fis
	 * @return
	 */
	private ParseResults parseFile(FileInputStream fis) throws IOException, NoRunException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(fis, Charset.forName("UTF-8")));
		String line;
		int count = 0;
		ParseResults answer = new ParseResults();

		while (((line = reader.readLine()) != null) && (count < 250)) {

			if (line != null) {
				count++;
				line = line.toLowerCase();

				boolean isStoreProc = this.parseForStoredProcedures(line);
				if (isStoreProc) {
					answer.setStoredProcedureFound(true);
				}
				CommandFlag f = this.parseLineForCommand(line);
				if (f.equals(CommandFlag.NORUN)) {
					throw new NoRunException();
				}
				//later on if we add more commands, set it here
				//in the answer. for now, no need to set it.
			}
		}

		return answer;
	}

	/**
	 * Returns true if a stored procedure command is found
	 * in the line passed in
	 *
	 * @param input
	 * @return
	 */
	private boolean parseForStoredProcedures(String input) throws IOException {
		if (input != null && (!input.equals(""))) {
			String flag = SP_FLAG.toLowerCase();
			if (input.indexOf(flag) != -1) {
				return true;

			}
		}

		return false;
	}

	private CommandFlag parseLineForCommand(String input) {
		if (input != null || (!input.equals(""))) {

			if (input.indexOf(CommandFlag.SQLRUNNER_CMD) != -1) {
				//then search for a command
				CommandFlag[] flags = CommandFlag.values();
				for (CommandFlag flag : flags) {
					if (input.indexOf(flag.getFlag()) != -1) {
						return flag;
					}
				}
			}
		}
		return CommandFlag.NOCMD;

	}

	public class ParseResults {

		boolean storedProcedureFound = false;
		CommandFlag commandFlag = CommandFlag.NOCMD;


		public ParseResults() {
			//nothin'
		}

		public boolean isStoredProcedureFound() {
			return storedProcedureFound;
		}

		public void setStoredProcedureFound(boolean storedProcedureFound) {
			this.storedProcedureFound = storedProcedureFound;
		}

		public CommandFlag getCommandFlag() {
			return commandFlag;
		}

		public void setCommandFlag(CommandFlag commandFlag) {
			this.commandFlag = commandFlag;
		}

	}

}
