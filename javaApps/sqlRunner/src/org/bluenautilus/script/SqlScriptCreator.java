package org.bluenautilus.script;

import org.bluenautilus.db.DataLanguage;
import org.bluenautilus.db.ExecutionPlan;
import org.bluenautilus.db.SqlTarget;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.file.FileAlreadyExistsException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bstevens on 5/15/16.
 */
public class SqlScriptCreator {

    private static final String TYPE = "TYPE: ";
    private static final String TARGET = "TARGET: ";
    private static final String EXECUTION_PLAN = "EXECUTION_PLAN: ";
    private static final String AUTHOR = "AUTHOR: ";

    private String jiraDescription;
    private ExecutionPlan executionPlan;
    private SqlTarget sqlTarget;
    private DataLanguage dataLanguage;
    private String authorName;
    private String authorEmail;
    private String parentFolderString;
    private Long epic = 0L;
    private String fileNameToUse;
    private String rollbackFilenameToUse;

    public SqlScriptCreator(final String deliveryDateString, final String jiraDescription,
                            final DataLanguage dataLanguage, final SqlTarget sqlTarget,
                            final ExecutionPlan executionPlan, final String authorName, final String authorEmail,
                            final String parentFolderString) {
        this.jiraDescription = jiraDescription;
        this.dataLanguage = dataLanguage;
        this.sqlTarget = sqlTarget;
        this.executionPlan = executionPlan;
        this.authorName = authorName;
        this.authorEmail = authorEmail;
        this.parentFolderString = parentFolderString;
        this.epic = System.currentTimeMillis() / 1000;
        this.fileNameToUse = deliveryDateString + "_" + Long.toString(epic);
        this.rollbackFilenameToUse = fileNameToUse + "_rollback";
    }


    public static List<String> theNextTenThursdays() {
        List<String> tenThursdays = new ArrayList<>();
        DateTime today = new DateTime();
        Integer dayOfWeek = today.getDayOfWeek();
        Integer daysAhead = DateTimeConstants.THURSDAY - dayOfWeek;
        if (daysAhead < 0) {  //Thursday already happened this week
            daysAhead += 14;
        } else {  //It's before Thursday
            daysAhead += 7;
        }

        DateTime nextDeliveryDate = today.plusDays(daysAhead);
        for (int i = 0; i < 10; i++) {
            String dateString = getDeliveryDateString(nextDeliveryDate);
            tenThursdays.add(dateString);
            nextDeliveryDate = nextDeliveryDate.plusDays(7);
        }

        return tenThursdays;
    }

    private static String getDeliveryDateString(DateTime deliveryDate) {
        // YYYY-MM-DD
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd");
        return simpleDateFormat.format(deliveryDate.toDate());
    }

    public void outputScript() throws IOException {
        outputFile(false);
    }

    public void outputRollback() throws IOException {
        outputFile(true);
    }

    private void outputFile(boolean isRollback) throws IOException {
        OutputStream outputStream;

        checkParentFolder(this.parentFolderString);
        if (isRollback) {
            this.parentFolderString = this.parentFolderString + File.separator + "rollback";
        }
        checkParentFolder(this.parentFolderString);
        String rollbackSuffix = isRollback ? "_rollback" : "";
        File newScript = new File(this.parentFolderString + File.separator + this.fileNameToUse + rollbackSuffix + ".sql");
        checkNewFile(newScript);

        outputStream = new FileOutputStream(newScript);
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, Charset.forName("UTF-8")));
        writer.write("--" + this.fileNameToUse + rollbackSuffix + ".sql" + "\n");
        writer.write("--\n");
        writer.write("-- " + this.jiraDescription + "\n");
        writer.write("--\n");
        writer.write("-- " + TYPE + this.dataLanguage.getDisplayName() + "\n");
        writer.write("--\n");
        writer.write("-- " + TARGET + this.sqlTarget + "\n");
        writer.write("--\n");
        writer.write("-- " + EXECUTION_PLAN + this.executionPlan.getDisplayName() + "\n");
        writer.write("--\n");
        writer.write("-- " + AUTHOR + this.authorName + " (" + this.authorEmail + ")\n");
        writer.write("--\n");
        writer.write("\n\n\n\n");
        writer.write("EXEC addDBUpdate N'" + this.fileNameToUse + rollbackSuffix + "'\n");
        writer.flush();
        writer.close();

    }

    private void checkParentFolder(final String parentFolderString) throws IOException {
        File parentFolder = new File(this.parentFolderString);

        if (!parentFolder.exists()) {
            throw new FileNotFoundException("Cannot find parent folder " + parentFolderString);
        }
        if (!parentFolder.isDirectory()) {
            throw new IOException("Parent folder is not directory: " + parentFolderString);
        }
    }

    private void checkNewFile(File newFile) throws IOException {
        if (newFile.exists()) {
            throw new FileAlreadyExistsException("File already exists: " + newFile.getPath());
        }
    }


}
