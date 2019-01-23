package com.MeMaker.gui;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

class FormPanel extends JPanel {

    private JLabel nameLabel;
    private JLabel occupationLabel;
    private JTextField nameFiled;
    private JTextField occupationFiled;
    private JButton okBtn;
    private FormListener formListener;
    private JList ageList;
    private JComboBox empCombo;
    private JLabel empLabel;
    private JCheckBox citizenCheck;
    private JTextField taxField;
    private JLabel taxLabel;

    private JRadioButton maleRadio;
    private JRadioButton femaleRadio;
    private ButtonGroup genderGroup;



    public FormPanel() {
        Dimension dim = getPreferredSize();
        dim.width = 250;
        setPreferredSize(dim);
        setMinimumSize(dim);

        Border etchedBorder = BorderFactory.createEtchedBorder();
        Border innerBorder = BorderFactory.createTitledBorder(etchedBorder, "Add Person");
        Border outerBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        setBorder(BorderFactory.createCompoundBorder(outerBorder,innerBorder));

        nameLabel = new JLabel("Name: ");
        occupationLabel = new JLabel("Occupation: ");
        nameFiled = new JTextField(10);
        occupationFiled = new JTextField(10);
        ageList = new JList();
        empCombo = new JComboBox();
        empLabel = new JLabel("Employment: ");
        citizenCheck = new JCheckBox();
        taxField = new JTextField(10);
        taxLabel = new JLabel("Tax ID: ");
        okBtn = new JButton("OK");

        //Set up mnemonic

        okBtn.setMnemonic(KeyEvent.VK_O);

        //Set up mnemonic for textFiled - need Label for it
        nameLabel.setDisplayedMnemonic(KeyEvent.VK_N);
        nameLabel.setLabelFor(nameFiled);


        // Set radio button
        maleRadio = new JRadioButton("male");
        femaleRadio = new JRadioButton("female");

        maleRadio.setActionCommand("male");
        femaleRadio.setActionCommand("female");

        genderGroup = new ButtonGroup();

        maleRadio.setSelected(true);


        genderGroup.add(maleRadio);
        genderGroup.add(femaleRadio);



        // Set up tax ID
        taxLabel.setEnabled(false); // false makes boxes gray so it can not be checked
        taxField.setEnabled(false);


        citizenCheck.addActionListener(new ActionListener() {
            // When citizenCheck is ticked it enables ID Tax Fileds
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean isTicked = citizenCheck.isSelected();
                taxLabel.setEnabled(isTicked);
                taxField.setEnabled(true);
            }
        });

        // Setup list box model
        DefaultListModel ageModel = new DefaultListModel();
        ageModel.addElement(new AgeCategory(0,"Under 18"));
        ageModel.addElement(new AgeCategory(1,"18 to 65"));
        ageModel.addElement(new AgeCategory(2 ,"65 or over"));

        ageList.setModel(ageModel);

        ageList.setPreferredSize(new Dimension(124,70));
        ageList.setBorder(BorderFactory.createEtchedBorder());
        ageList.setSelectedIndex(1);


        // Setup ComboBox model

        DefaultComboBoxModel empModel = new DefaultComboBoxModel();
        empModel.addElement("employed");
        empModel.addElement("self-employed");
        empModel.addElement("unemployed");
        empCombo.setModel(empModel);
        empCombo.setSelectedIndex(1);


        // Action Listner for okBtn
        okBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameFiled.getText();
                String occupation = occupationFiled.getText();
                AgeCategory ageCat = (AgeCategory) ageList.getSelectedValue();
                String empStatus = (String) empCombo.getSelectedItem();
                String taxId = taxField.getText();
                boolean citizenUs = citizenCheck.isSelected();

                String genderCommand = genderGroup.getSelection().getActionCommand(); // pass radio buttons setting

                FormEvent formEvent = new FormEvent(this, name, occupation,ageCat.getId(), empStatus,taxId,citizenUs, genderCommand);

                if(formListener != null) {
                    formListener.formEventOccurred(formEvent);

                }

                // actions performed to reset all fields to default state after adding person to database
                nameFiled.setText("");  // clear text filed after adding person to dataBase/ clicking ok Button
                occupationFiled.setText("");
                ageList.setSelectedIndex(1);
                empCombo.setSelectedIndex(1);
                citizenCheck.setSelected(false);
                maleRadio.setSelected(true);
                taxField.setText("");
                taxLabel.setEnabled(false);
                taxField.setEnabled(false);


            }
        });

        layoutComponets();

    }

    public void layoutComponets() {
        setLayout(new GridBagLayout());

        GridBagConstraints gc = new GridBagConstraints();


        ///////////////////// First Row ////////////////////////
        gc.gridy = 0;

        gc.weightx = 1;
        gc.weighty = 0.2;

        gc.gridx = 0;

        gc.fill = GridBagConstraints.NONE;
        gc.anchor = GridBagConstraints.LINE_END;
        gc.insets = new Insets(0, 0, 0, 5);
        add(nameLabel, gc);

        gc.gridx = 1;
        gc.gridy = 0;
        gc.insets = new Insets(0, 0, 0, 0);
        gc.anchor = GridBagConstraints.LINE_START;
        add(nameFiled, gc);

        ///////////////////// Second Row ////////////////////////
        gc.gridy++;

        gc.weightx = 1;
        gc.weighty = 0.2;

        gc.gridx = 0;

        gc.insets = new Insets(0, 0, 0, 5);
        gc.anchor = GridBagConstraints.LINE_END;
        add(occupationLabel,gc);

        gc.gridx = 1;
        gc.insets = new Insets(0, 0, 0, 0);
        gc.anchor = GridBagConstraints.LINE_START;
        add(occupationFiled, gc);



        ///////////////////// Third Row ////////////////////////
        gc.gridy++;

        gc.weightx = 1;
        gc.weighty = 0.2;

        gc.gridx =1;

        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        gc.insets = new Insets(0, 0, 0, 0);
        add(ageList,gc);

        /////////////////////  Fourth Row ////////////////////////
        gc.gridy++;

        gc.gridx = 0;
        gc.weightx = 1;
        gc.weighty = 0.2;

        gc.anchor = GridBagConstraints.FIRST_LINE_END;
        gc.insets = new Insets(0, 0, 0, 5);
        add(empLabel, gc);

        gc.weightx = 1;
        gc.weighty = 0.2;

        gc.gridx =1;
        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        gc.insets = new Insets(0, 0, 0, 0);
        add(empCombo,gc);
        ///////////////////// fifth Row ////////////////////////

        gc.gridy++;

        gc.gridx = 0;
        gc.weightx = 1;
        gc.weighty = 0.2;

        gc.anchor = GridBagConstraints.FIRST_LINE_END;
        gc.insets = new Insets(0, 0, 0, 5);
        add(new JLabel("US Citizen:"), gc);

        gc.weightx = 1;
        gc.weighty = 0.2;

        gc.gridx =1;
        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        gc.insets = new Insets(0, 0, 0, 0);
        add(citizenCheck,gc);

        ///////////////////// sixth Row ////////////////////////

        gc.gridy++;

        gc.gridx = 0;
        gc.weightx = 1;
        gc.weighty = 0.2;

        gc.anchor = GridBagConstraints.FIRST_LINE_END;
        gc.insets = new Insets(0, 0, 0, 5);
        add(taxLabel, gc);

        gc.weightx = 1;
        gc.weighty = 0.2;

        gc.gridx =1;
        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        gc.insets = new Insets(0, 0, 0, 0);
        add(taxField,gc);


        ///////////////////// seventh Row ////////////////////////

        gc.gridy++;

        gc.gridx = 0;
        gc.weightx = 1;
        gc.weighty = 0.05;

        gc.anchor = GridBagConstraints.LINE_END;
        gc.insets = new Insets(0, 0, 5, 5);
        add(new JLabel("Gender:"), gc);

        gc.gridx =1;
        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        gc.insets = new Insets(0, 0, 0, 0);
        add(maleRadio,gc);


        /////////////////// eight Row ////////////////////////

        gc.gridy++;


        gc.weightx = 1;
        gc.weighty = 0.2;

        gc.gridx =1;
        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        gc.insets = new Insets(0, 0, 0, 0);
        add(femaleRadio,gc);

        ///////////////////// last Row ////////////////////////
        gc.gridy++;

        gc.weightx = 1;
        gc.weighty = 2;

        gc.gridx =1;

        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        gc.insets = new Insets(0, 0, 0, 0);
        add(okBtn,gc);
    }

    public void setFormListener(FormListener listener) {
        this.formListener = listener;
    }
}

class AgeCategory {

    private int id;
    private String text;

    public AgeCategory(int id, String text){
        this.id = id;
        this.text = text;

    }

    @Override
    public String toString() {
        return text;
    }

    public int getId() {
        return id;
    }
}
