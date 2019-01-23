package com.MeMaker.gui;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PrefsDialog extends JDialog {

    private JButton okButton;
    private JButton cancelButton;
    private JSpinner portSpinner;
    private SpinnerNumberModel spinnerNumberModel;
    private JTextField userFiled;
    private JPasswordField passwordField;
    private PrefsListener prefsListener;

    public PrefsDialog(JFrame parent){
        super(parent, "Preferences", true);


        okButton = new JButton("Ok");
        cancelButton = new JButton("Cancel");

        spinnerNumberModel = new SpinnerNumberModel(3306, 0, 9999, 1);
        portSpinner = new JSpinner(spinnerNumberModel);

        userFiled = new JTextField(10);
        passwordField = new JPasswordField(10);

        passwordField.setEchoChar('*'); // changes echoChar which is char hiding your input

        layoutControls();


        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Integer port= (Integer) portSpinner.getValue();
                String user = userFiled.getText();
                char[] password = passwordField.getPassword();

                if(prefsListener != null) {
                    prefsListener.preferencesSet(user, new String(password),port);
                }

                setVisible(false);



            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });


        setSize(340,250);
        setLocationRelativeTo(parent); // because it is opening in top left corner this setup makes it to appear in the middle of main frame

    }

    public void setDefaults(String user, String password, int port) {
        userFiled.setText(user);
        passwordField.setText(password);
        portSpinner.setValue(port);
    }

    public void setPrefsListener(PrefsListener prefsListener) {
        this.prefsListener = prefsListener;
    }

    public void layoutControls(){

        JPanel controlsPanel = new JPanel();
        JPanel buttonsPanel = new JPanel();

        Border titleBorder = BorderFactory.createTitledBorder("Database Preferences");

        int space = 15;
        Border spaceBorder = BorderFactory.createEmptyBorder(space,space,space,space);
        controlsPanel.setBorder(BorderFactory.createCompoundBorder(spaceBorder,titleBorder));



        controlsPanel.setLayout(new GridBagLayout());

        GridBagConstraints gc = new GridBagConstraints();

        gc.gridy = 0;

        Insets rightPadding = new Insets(0,0,0,15);
        Insets noPadding = new Insets(0,0,0,0);


        /////////////// First row ////////////////////////

        gc.weightx = 1;
        gc.weighty = 1;
        gc.fill = GridBagConstraints.NONE;


        gc.gridx = 0;
        gc.anchor = GridBagConstraints.EAST;
        gc.insets = rightPadding;
        controlsPanel.add(new JLabel("User: "), gc);

        gc.gridx++;
        gc.anchor = GridBagConstraints.WEST;
        gc.insets = noPadding;
        controlsPanel.add(userFiled, gc);

        /////////////// Next row ////////////////////////

        gc.gridy++;


        gc.weightx = 1;
        gc.weighty = 1;
        gc.fill = GridBagConstraints.NONE;


        gc.gridx = 0;
        gc.anchor = GridBagConstraints.EAST;
        gc.insets = rightPadding;
        controlsPanel.add(new JLabel("Password: "), gc);

        gc.gridx++;
        gc.anchor = GridBagConstraints.WEST;
        gc.insets = noPadding;
        controlsPanel.add(passwordField, gc);


        /////////////// Next row ////////////////////////

        gc.gridy++;


        gc.weightx = 1;
        gc.weighty = 1;
        gc.fill = GridBagConstraints.NONE;


        gc.gridx = 0;
        gc.anchor = GridBagConstraints.EAST;
        gc.insets = rightPadding;
        controlsPanel.add(new JLabel("Port: "), gc);

        gc.gridx++;
        gc.anchor = GridBagConstraints.WEST;
        gc.insets = noPadding;
        controlsPanel.add(portSpinner, gc);

        /////////////////// Buttons Panel////////////////////

        buttonsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonsPanel.add(okButton);
        buttonsPanel.add(cancelButton);

        // set same size to buttons
        Dimension btnSize = cancelButton.getPreferredSize();
        okButton.setPreferredSize(btnSize);

        // Add sub panels to dialog

        setLayout(new BorderLayout());
        add(controlsPanel, BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.SOUTH);
    }

}
