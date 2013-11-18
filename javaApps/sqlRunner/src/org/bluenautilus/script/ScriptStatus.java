package org.bluenautilus.script;

import java.awt.*;

/**
 * User: bluenautilus2
 * Date: 7/28/13
 * Time: 11:49 AM
 */
public enum ScriptStatus {
    TOO_OLD("Too Old/Not Needed", Color.DARK_GRAY, Color.white),
    ALREADY_RUN("Already Run", new Color(0, 100, 0), Color.white),
    RUNNING("Running", new Color(255, 255, 255), new Color(102, 0, 51)),
    NEED_TO_RUN("Need to Run", new Color(0, 0, 139), new Color(152, 238, 255)),
    FILE_ERROR("Issue With File", new Color(176, 23, 31), Color.white),
    RUN_ERROR("Script Returned Error", new Color(176, 23, 31), Color.white),
    RECENTLY_RUN("Recently Run", new Color(0, 102, 51), new Color(150, 220, 150)),
    ROLLED_BACK("Rolled Back", new Color(102,0,204), new Color(200,190,255)),
    EXAMINE_OUTPUT("Examine Output", new Color(204,0,204), new Color(255,204,204));
    public static final ScriptStatus DEFAULT = TOO_OLD;
    private String displayName;
    private Color textColor;
    private Color bgColor;

    ScriptStatus(String name, Color textColor, Color bgColor) {
        this.displayName = name;
        this.textColor = textColor;
        this.bgColor = bgColor;
    }

    public Color getBgColor() {
        return bgColor;
    }

    @Override
    public String toString() {
        return displayName;
    }

    public Color getTextColor() {
        return textColor;
    }


}
