package org.bluenautilus.gui;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bluenautilus.OldExecutable;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bstevens on 1/26/15.
 */
public class ParentPlusMinusPanel extends JPanel {
    private static Log log = LogFactory.getLog(OldExecutable.class);

    public static final String PLUS_IMAGE = "green_plus_box.png";
    public static final String MINUS_IMAGE = "red-minus-box.png";
    public static final String GEAR_IMAGE = "grey-gear.png";
    public static final Integer IMAGE_SIZE_IN_PIXELS = 20;

    private final List<PrettyButtonListener> listeners = new ArrayList<>();

    private JButton minusButton;
    private JButton plusButton;
    private JButton gearButton;

    protected JPanel buttonPanel = new JPanel( new BorderLayout());

    public ParentPlusMinusPanel() {
        super();
    }


    protected void loadImages() {
        Image image1 = null;
        Image image2 = null;
        Image image3 = null;

        try {
            image1 = ImageIO.read(new File(MINUS_IMAGE));
            image2 = ImageIO.read(new File(PLUS_IMAGE));
            image3 = ImageIO.read(new File(GEAR_IMAGE));
        } catch (IOException ioe) {
            log.error(ioe);
        }


        image1 = image1.getScaledInstance(IMAGE_SIZE_IN_PIXELS, IMAGE_SIZE_IN_PIXELS, 0);
        ImageIcon minusIcon = new ImageIcon(image1);


        image2 = image2.getScaledInstance(IMAGE_SIZE_IN_PIXELS, IMAGE_SIZE_IN_PIXELS, 0);
        ImageIcon plusIcon = new ImageIcon(image2);


        image3 = image3.getScaledInstance(IMAGE_SIZE_IN_PIXELS, IMAGE_SIZE_IN_PIXELS, 0);
        ImageIcon gearIcon = new ImageIcon(image3);


        minusButton = new JButton(minusIcon);
        minusButton.addActionListener(new PrettyListener(PrettyButtonListener.ButtonType.MINUS));
        plusButton = new JButton(plusIcon);
        plusButton.addActionListener(new PrettyListener(PrettyButtonListener.ButtonType.PLUS));
        gearButton = new JButton(gearIcon);
        gearButton.addActionListener(new PrettyListener(PrettyButtonListener.ButtonType.GEAR));

        buttonPanel.add(minusButton, BorderLayout.CENTER);
        buttonPanel.add(plusButton, BorderLayout.WEST);
        buttonPanel.add(gearButton, BorderLayout.EAST);
    }


    public void addListener(PrettyButtonListener listener) {
        this.listeners.add(listener);
    }

    public class PrettyListener implements ActionListener {
        PrettyButtonListener.ButtonType type;

        public PrettyListener(PrettyButtonListener.ButtonType theType) {
            super();
            this.type = theType;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            for (PrettyButtonListener p : listeners) {
                p.prettyButtonClicked(type);
            }
        }
    }


}
