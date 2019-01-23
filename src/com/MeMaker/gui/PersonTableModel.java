package com.MeMaker.gui;

import com.MeMaker.model.EmploymentCategory;
import com.MeMaker.model.Person;
import lombok.Setter;

import javax.swing.table.AbstractTableModel;
import javax.swing.text.StyledEditorKit;
import java.util.List;


// Wrapper for a data
public class PersonTableModel extends AbstractTableModel {

    @Setter private List<Person> db;

    private String[] colName = {"ID", "Name", "Occupation", "Age Category", "Employment Category", "US Citizen", "Tax ID "}; // names for columns used in getColumnName method

    public PersonTableModel(){
    }



    // sets name for columns at top of the table
    @Override
    public String getColumnName(int column) {
        return colName[column];
    }

    // allows to edit column
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {

        switch (columnIndex) {
            case 1:
                return true;
            case 4:
                return true;
            case 5:
                return true;
            default:
                return false;
        }
    }


    // sets column checkbox for table
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return Integer.class;
            case 1:
                return String.class;
            case 2:
                return String.class;
            case 3:
                return String.class;
            case 4:
                return EmploymentCategory.class;
            case 5:
                return Boolean.class;
            case 6:
                return String.class;
                default:
                    return null;
        }
    }

    // method for saving data into model when it is changed in tabel view
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

        if(db == null) return;
        Person person = db.get(rowIndex);

        switch (columnIndex) {
            case 1:
                person.setName((String)aValue);
                break;
            case 4:
                person.setEmpCat((EmploymentCategory) aValue);
                break;
            case 5:
                person.setUsCitizen((Boolean) aValue);
            default:
                return;
        }
    }

    @Override
    public int getRowCount() {
        return db.size();
    }

    @Override
    public int getColumnCount() {
        return 7;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Person person = db.get(rowIndex);

        switch (columnIndex) {
            case 0:
                return person.getId();
            case 1:
                return person.getName();
            case 2:
                return person.getOccupation();
            case 3:
                return person.getAgeCategory();
            case 4:
                return person.getEmpCat();
            case 5:
                return person.isUsCitizen();
            case 6:
                return person.getTaxId();
        }
        return null;
    }
}
