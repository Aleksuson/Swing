package com.MeMaker.gui;

import com.MeMaker.model.EmploymentCategory;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EmploymentCategoryEditor extends AbstractCellEditor implements TableCellEditor {

    private JComboBox comboBox;


    public EmploymentCategoryEditor() {
        comboBox = new JComboBox(EmploymentCategory.values());
    }


    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {

        comboBox.setSelectedItem(value);

        comboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fireEditingStopped();
            }
        });

        return comboBox;
    }

    @Override
    public Object getCellEditorValue() {
        return comboBox.getSelectedItem();
    }
}
