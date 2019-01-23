package com.MeMaker.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.Parameter;

public class ProgressDialog extends JDialog {

    private JButton cancelButton;
    private JProgressBar progressBar;
    private ProgressDialogListner progressDialogListner;

    public ProgressDialog(Window parent, String title) {
        super(parent,title, ModalityType.APPLICATION_MODAL);

        cancelButton = new JButton("Cancel");
        progressBar = new JProgressBar();
        progressBar.setStringPainted(true); // when on shows only percentage change

        progressBar.setMaximum(10);

        progressBar.setString("Retrieving messages...");

        // progressBar.setIndeterminate(true); // when you do not know max value for progress bar

        setLayout(new FlowLayout());

        Dimension size = cancelButton.getPreferredSize();
        size.width = 400;
        progressBar.setPreferredSize(size);

        add(progressBar);
        add(cancelButton);

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(progressDialogListner != null) {
                    progressDialogListner.progressDialogCanceled();
                }
            }
        });

        // set cross to close on exit
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (progressDialogListner != null) {
                    progressDialogListner.progressDialogCanceled();
                }
            }

        });

        pack();

        setLocationRelativeTo(parent); // windows pops up in the middle
    }

    public void setProgressDialogListener(ProgressDialogListner progressDialogListner) {
        this.progressDialogListner = progressDialogListner;
    }

    public void setMaximum(int count) {
        progressBar.setMaximum(count);
    }

    public void setValue(int value) {

        int progress = 100*value/progressBar.getMaximum();

        progressBar.setString(String.format("%d%% complete",progress));

        progressBar.setValue(value);
    }


    @Override
    public void setVisible(boolean visible) {


        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                if(visible == false) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    progressBar.setValue(0); // resets after each start of new bar
                    progressBar.setString("Retrieving messages...");
                }
                // set cursor to waiting when progress bar is on
                if(visible) {
                    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    //getParent().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                }else {
                    setCursor(Cursor.getDefaultCursor());
                    //getParent().setCursor(Cursor.getDefaultCursor());
                }

                ProgressDialog.super.setVisible(visible);

            }
        });

    }
}
