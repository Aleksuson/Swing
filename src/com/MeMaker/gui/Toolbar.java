package com.MeMaker.gui;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Toolbar extends JToolBar implements ActionListener {

    private JButton saveButton;
    private JButton refreshButton;
    private ToolbarListener toolbarListener;



    Toolbar(){
        saveButton = new JButton();
        saveButton.setIcon(Utils.createIcon("/com/MeMaker/images/Save16.gif"));// set icon for button, you can also use it for labels
        saveButton.setToolTipText("Save"); // when you mouse over icon it shows name of button

        refreshButton = new JButton();
        refreshButton.setIcon(Utils.createIcon("/com/MeMaker/images/Refresh16.gif"));
        refreshButton.setToolTipText("Refresh");


        saveButton.addActionListener(this);
        refreshButton.addActionListener(this);


        add(saveButton);
        addSeparator();
        add(refreshButton);

       // setFloatable(false); // toolbar is not more draggable same if you set border layout


        Border etchedBorder = BorderFactory.createEtchedBorder();
        Border innerBorder = BorderFactory.createTitledBorder(etchedBorder, "Options");
        Border outerBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        setBorder(BorderFactory.createCompoundBorder(outerBorder,innerBorder));


    }


    void setToolbarListener(ToolbarListener listener) {
        this.toolbarListener = listener;

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton clicked = (JButton) e.getSource();

        if(clicked == saveButton) {
            if(toolbarListener != null){
                toolbarListener.saveEventOccurred();
            }
        } else if (clicked == refreshButton) {
            if (toolbarListener != null) {
                toolbarListener.refreshEventOccurred();
            }
        }
    }
}
