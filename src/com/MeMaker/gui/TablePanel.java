package com.MeMaker.gui;

import com.MeMaker.model.EmploymentCategory;
import com.MeMaker.model.Person;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

// it is a

public class TablePanel extends JPanel {

    private JTable table;
    private PersonTableModel tableModel;

    private JPopupMenu popup;

    private PersonTableListener personTableListener; // person table listener for deleting
    // rows

    public TablePanel(){

        tableModel = new PersonTableModel();
        table = new JTable(tableModel);
        popup = new JPopupMenu();

        table.setDefaultRenderer(EmploymentCategory.class, new EmploymentCategoryRenderer());
        table.setDefaultEditor(EmploymentCategory.class,new EmploymentCategoryEditor());
        table.setRowHeight(25);

        // adding button to popupMenu
        JMenuItem removeItem = new JMenuItem("Delete row");
        popup.add(removeItem);

        // adding mouse listener to table so when right button is clicked the popup menu opens -
        // mouse adapter is used so you can override only method that you require not everyone.
        table.addMouseListener(new MouseAdapter() {
                                   @Override
                                   public void mousePressed(MouseEvent e) {
                                       // selects row at point
                                       int row = table.rowAtPoint(e.getPoint());

                                       table.getSelectionModel().setSelectionInterval(row,row); // highlights row
                                       //

                                       if(e.getButton() == MouseEvent.BUTTON3) {
                                           popup.show(table,e.getX(),e.getY());
                                       }
                                   }
                               });


        // action listener for popUp menu remove Item

        removeItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = table.getSelectedRow();

                if(personTableListener != null) {
                    personTableListener.rowDeleted(row);
                    tableModel.fireTableRowsDeleted(row,row); // update view model to show without deleted rows
                }


            }
        });


                setLayout(new BorderLayout());
        add(new JScrollPane(table), BorderLayout.CENTER); // new JScrollPane adds scroll on both sides
    }

    public void setData(List<Person> db) {
        tableModel.setDb(db);

    }

    // updates table after adding new Person
    public void refresh() {
        tableModel.fireTableDataChanged();
    }


    // method for setting person table listner
    public void  setPersonTableListener(PersonTableListener listener) {
        this.personTableListener = listener;
    }


}
