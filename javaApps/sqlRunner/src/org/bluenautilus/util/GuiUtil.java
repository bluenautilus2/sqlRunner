package org.bluenautilus.util;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: bstevens
 * Date: 8/1/13
 * Time: 9:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class GuiUtil {

    public static void showErrorModalDialog(Exception e, JPanel parent) {
        //custom title, error icon
        System.err.print(e);
        JOptionPane.showMessageDialog(parent,
                makeGoodString(e),
                "OH NOES",
                JOptionPane.ERROR_MESSAGE);

    }

    public static String makeGoodString(Exception e) {
        StringBuilder builder = new StringBuilder();
        builder.append(e.getMessage());
        builder.append("\n\n");
        StackTraceElement[] stack = e.getStackTrace();
        int count = 0;
        for (StackTraceElement s : stack) {
            builder.append(s.toString() + "\n");
            count++;
            if (count > 10) {
                break;
            }
        }
        return builder.toString();
    }
}
