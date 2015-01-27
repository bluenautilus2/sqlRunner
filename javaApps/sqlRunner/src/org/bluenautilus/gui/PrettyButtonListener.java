package org.bluenautilus.gui;

/**
 * Created by bstevens on 1/27/15.
 */
public interface PrettyButtonListener
{

    public void buttonClicked(ButtonType type);

    public enum ButtonType{
        PLUS,
        MINUS,
        GEAR;
    }

}
