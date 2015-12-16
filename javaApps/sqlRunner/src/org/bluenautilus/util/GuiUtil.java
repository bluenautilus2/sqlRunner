package org.bluenautilus.util;

import javax.swing.*;
import java.awt.*;

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
        String msg = makeGoodString(e);

        JTextArea wrapped = new JTextArea(msg);
        JScrollPane scrollPane = new JScrollPane(wrapped);
        scrollPane.setPreferredSize(new Dimension(500,250));

        JOptionPane.showMessageDialog(parent,
                scrollPane,
                "WHAT HAVE YOU DONE??",
                JOptionPane.ERROR_MESSAGE);
    }

    public static String makeGoodString(Exception e) {
        StringBuilder builder = new StringBuilder();

        builder.append(e.getMessage()+"\n\n");
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
