package com.MeMaker.gui;

import com.MeMaker.model.EmploymentCategory;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class EmploymentCategoryRenderer implements TableCellRenderer {

    private JComboBox<EmploymentCategory> combo;

    EmploymentCategoryRenderer() {
        combo = new JComboBox<>(EmploymentCategory.values());

    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        combo.setSelectedItem(value);

        return combo;
    }
}
