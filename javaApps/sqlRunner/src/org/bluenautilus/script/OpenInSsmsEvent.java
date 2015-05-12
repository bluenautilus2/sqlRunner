package org.bluenautilus.script;

/**
 * Created by mgebreyes on 5/5/2015.
 */
public class OpenInSsmsEvent {

    private ScriptType type;

    public OpenInSsmsEvent(ScriptType type) {
        this.type = type;
    }

    public ScriptType getType() {
        return type;
    }

    public void setType(ScriptType type) {
        this.type = type;
    }
}
