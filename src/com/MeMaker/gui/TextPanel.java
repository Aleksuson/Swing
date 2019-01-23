package com.MeMaker.gui;

import javax.swing.*;
import java.awt.*;

public class TextPanel extends JPanel {

    private JTextArea jTextArea;

    public TextPanel() {
        jTextArea = new JTextArea();

        jTextArea.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        setLayout(new BorderLayout());

        add(new JScrollPane(jTextArea), BorderLayout.CENTER);

        jTextArea.setFont(new Font("Serif",Font.PLAIN,15));

    }

    public void setText(String text) {
        jTextArea.setText(text);
    }

    public void appendText(String text) {
        jTextArea.append(text + "\n");

    }
}
