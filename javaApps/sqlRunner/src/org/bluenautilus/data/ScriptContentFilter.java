package org.bluenautilus.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by bstevens on 8/7/15.
 */
public class ScriptContentFilter {

    private Pattern regexForFindingLine;
    private Pattern regexForLineContent;
    private Integer linesToCheckStart = 0;
    private Integer linesToCheckEnd = 30;

    public ScriptContentFilter(Pattern regexForFindingLine, Pattern regexForLineContent) {
        this.regexForFindingLine = regexForFindingLine;
        this.regexForLineContent = regexForLineContent;
    }

    //returns true only if line is found and regex matches.
    public boolean filterScript(File fileToCheck) throws IOException {
        FileInputStream fis = new FileInputStream(fileToCheck);
        BufferedReader reader = new BufferedReader(new InputStreamReader(fis, Charset.forName("UTF-8")));
        String line = null;
        Integer count = linesToCheckStart;
        String lineInQuestion = null;

        while (((line = reader.readLine()) != null) && (++count < linesToCheckEnd)) {
            line = line.toLowerCase();
            Matcher lineMatch = regexForFindingLine.matcher(line);
            if (lineMatch.find()) {
                lineInQuestion = line;
            }
        }
        if (lineInQuestion == null) {
            return false;
        } else {
            Matcher contentMatch = regexForLineContent.matcher(lineInQuestion);
            return contentMatch.find();
        }

    }

}
