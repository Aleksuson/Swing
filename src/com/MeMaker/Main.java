package com.MeMaker;

import com.MeMaker.gui.MainFrame;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainFrame();

            }
        });


    }
}
