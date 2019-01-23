package com.MeMaker.gui;

import com.MeMaker.controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.prefs.Preferences;

import static java.awt.event.InputEvent.*;

public class MainFrame extends JFrame {

    private FormPanel formPanel;
    private JFileChooser fileChooser;
    private Controller controller; // added controller to connect between model and view'
    private TablePanel tablePanel; // added table panel
    private PrefsDialog prefsDialog; // windows to set view settings of program;
    private Preferences preferences; // preferences to store password and user name
    private JSplitPane splitPane;
    private JTabbedPane tabPane;
    private MessagePanel messagePanel;

    public MainFrame() {
        super("Person Database");
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, you can set the GUI to another look and feel.
        }

        setLayout(new BorderLayout());

        Toolbar toolbar = new Toolbar();
        formPanel = new FormPanel();
        controller = new Controller();
        tablePanel = new TablePanel();
        tablePanel.setData(controller.getPeople()); // populating tablePanel with people from DataBase getting data from model.Database
        prefsDialog = new PrefsDialog(this);
        tabPane = new JTabbedPane();
        messagePanel = new MessagePanel(this);
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,formPanel,tabPane);

        splitPane.setOneTouchExpandable(true);

        tabPane.addTab("Person Database", tablePanel);
        tabPane.addTab("Messages",messagePanel);




        preferences = Preferences.userRoot().node("db");


        // adding to main frame TableListener for informing when row was deleted -
        // it will than use controller and cont will pass this info to model database
        tablePanel.setPersonTableListener(row -> controller.removePerson(row));


        // set Listener for changing tabs in tabPane - takes action when tab is changed

        tabPane.addChangeListener(e -> {
            int tabIndex = tabPane.getSelectedIndex();
        if(tabIndex == 1) {
            messagePanel.refresh();
        }
        });



        // action Listener for checking
        prefsDialog.setPrefsListener((user, password, port) -> {
            System.out.println(user + " " + password);
            preferences.put("user",user);
            preferences.put("password",password); // saves user and password in preferences
            preferences.putInt("port",port);

            try {
                controller.configure(port,user,password);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(MainFrame.this,"Unable to re-connect.");
            }
        });


        // set default values for preferences.
        String user = preferences.get("user","");
        String password = preferences.get("password", "");
        int port = preferences.getInt("port", 3306);
        prefsDialog.setDefaults(user,password,port);

        try {
            controller.configure(port,password,user);
        } catch (Exception e) {
            System.err.println("Can't connect to database");
        }


        fileChooser = new JFileChooser();
        fileChooser.addChoosableFileFilter(new PersonFileFilter()); // add filter to fileChooser restricts selection of files.

        setJMenuBar(createMenuBar()); // setting Menu bar


       toolbar.setToolbarListener(new ToolbarListener() {
           @Override
           public void saveEventOccurred() {
               connect();

               try {
                   controller.save();
               } catch (SQLException e) {
                   JOptionPane.showMessageDialog(MainFrame.this,"Unable to save to database","Database Connection Problem", JOptionPane.ERROR_MESSAGE);
               }
           }

           @Override
           public void refreshEventOccurred() {
                refresh();
           }

           void connect(){
               try {
                   controller.connect();
               } catch (Exception e) {
                   JOptionPane.showMessageDialog(MainFrame.this,"Cannot connect to database.","Database Connection Problem", JOptionPane.ERROR_MESSAGE);
               }
           }

       });


        // Listener for Form panel
        formPanel.setFormListener(e -> {
            String name = e.getName();
            String occupation = e.getOccupation();
            int ageCat = e.getAgeCategory();
            String empCat = e.getEmpCat();
            String gender = e.getGender();
            String taxID = e.getTaxId();
            boolean usCitizen = e.isUsCitizen();

            controller.addPerson(name,occupation,ageCat,empCat,taxID,usCitizen,gender); // sends user input data to controller which communicates with model
            tablePanel.refresh(); // method used to refresh table after adding new Person to DB with Ok button

        });


        // windowsListener added to also disconnect from data base on quit
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                controller.disconnect();
                dispose(); // closes window when you click the x exit button in right window corner
            }
        });


        add(toolbar, BorderLayout.PAGE_START);
        //add(tablePanel, BorderLayout.CENTER);
        //add(formPanel, BorderLayout.WEST);
        add(splitPane, BorderLayout.CENTER);

        refresh();

        setSize(600, 500);
        setMinimumSize(new Dimension(600,500)); // setting minimum size of window - helps to avoid text hiding/bugs
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // this method tells what to do when you click x in the right corner of frame
        setVisible(true);

    }

    // Method to create menu bar at top of main window
    private JMenuBar createMenuBar(){
        JMenuBar menuBar = new JMenuBar();


        JMenu fileMenu = new JMenu("File");
        JMenuItem exportDataItem = new JMenuItem("Export Data...");
        JMenuItem importDataItem = new JMenuItem("Import Data...");
        JMenuItem exitItem = new JMenuItem("Exit");
        fileMenu.add(exportDataItem);
        fileMenu.add(importDataItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        JMenu windowMenu = new JMenu("Window"); /// You can add ActionListner?
        JMenu showMenu = new JMenu("Show");
        JMenuItem prefsItem = new JMenuItem("Preferences...");

        JCheckBoxMenuItem showFormItem = new JCheckBoxMenuItem("Person Form");
        showFormItem.setSelected(true);
        showMenu.add(showFormItem);

        windowMenu.add(showMenu);
        windowMenu.add(prefsItem);

        menuBar.add(fileMenu);
        menuBar.add(windowMenu);

        prefsItem.addActionListener(e -> {
            prefsDialog.setVisible(true); // after clicking on Preference button it sets prefsDialog visible
        });


        showFormItem.addActionListener(e -> {
           JCheckBoxMenuItem menuItem = (JCheckBoxMenuItem) e.getSource();
           if(menuItem.isSelected()){
               splitPane.setDividerLocation((int)formPanel.getMinimumSize().getWidth());
           }

           formPanel.setVisible(menuItem.isSelected());

        });

        //adding Mnemmonics to components - shourtcuts - exit filr

        fileMenu.setMnemonic(KeyEvent.VK_F);
        exitItem.setMnemonic(KeyEvent.VK_X);

        //adding Accelerator to componetns in this example CTRL + X exits the program
        //you can also add other keys than CTRL - used only for menu bar???
        exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, CTRL_DOWN_MASK));

        importDataItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, CTRL_DOWN_MASK));

        prefsItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, CTRL_DOWN_MASK));



        // action Listner added to importItem from File menu - when clicked FileChodser pops up - OPEN FILE
        importDataItem.addActionListener(e -> { // opens FileChooser from importItem
            if (fileChooser.showOpenDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION) {
                try {
                    controller.loadFromFile(fileChooser.getSelectedFile()); // loads selected item from file chooser
                    tablePanel.refresh(); // after loading table panel has to be refreshed so it shows loaded data
                } catch (IOException e1) {
                    JOptionPane.showMessageDialog(MainFrame.this,
                            "Could not load data from file,", "Error",
                            JOptionPane.ERROR_MESSAGE);

                }
            }

        });

        // action Listner added to exportFile from File menu - when clicked FileChodser pops up _ SAVE FILE
        exportDataItem.addActionListener(e -> { // opens FileChooser from importItem
            if(fileChooser.showSaveDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION){
                try {
                    controller.saveToFile(fileChooser.getSelectedFile()); // saves selected item from file chooser
                } catch (IOException e1) {
                    JOptionPane.showMessageDialog(MainFrame.this,
                            "Could save data to file,", "Error",
                            JOptionPane.ERROR_MESSAGE);

                }
            }

        });



        // action listener added to exitItem from file Menu on topBar
        exitItem.addActionListener(e -> {


            /*       String text = JOptionPane.showInputDialog(MainFrame.this, "Enter your user name.","Enter User Name",
                    JOptionPane.WARNING_MESSAGE); // he added | JOptionPane OK_Option??

            System.out.println(text);*/

            //JOptionPane is Pane which is poping out message box when you click a item
            //There are for main options = propmpt for input/ask for confirmation/show message/
            //showOptionDialog - GrandUnification of three others
            int action = JOptionPane.showConfirmDialog(MainFrame.this, "Do you want to Exit program?","Exit",
                    JOptionPane.YES_NO_OPTION);
            if(action == JOptionPane.OK_OPTION) {
                //System.exit(0);
                WindowListener[] listeners = getWindowListeners();

                for(WindowListener listener : listeners){
                    listener.windowClosing(new WindowEvent(MainFrame.this,0));
                }
            }
        });

        return menuBar;
    }


    private void refresh() {


        try {
            controller.connect();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(MainFrame.this,"Cannot connect to database." ,"Database Connection Problem", JOptionPane.ERROR_MESSAGE);
        }

        try {
            controller.load();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(MainFrame.this,"Unable to load from Database","Database Connection Problem", JOptionPane.ERROR_MESSAGE);
        }

        tablePanel.refresh();

    }
}
