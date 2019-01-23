package com.MeMaker.controller;

import com.MeMaker.model.*;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class Controller {

    Database db = new Database();

    public List<Person> getPeople() {
        return db.getPeople();
    }


    public void removePerson(int index) {
        db.removePerson(index);
    }

    //Method to save data to database
    public void save() throws SQLException {
        db.save();
    }

    public void configure(int port, String user, String password) throws Exception {
        db.configure(port,user,password);
    }



    //Method to disconnect from database
    public void disconnect() {
        db.disconnect();
    }

    //Method to connect to database
    public void connect() throws Exception {
        db.connect();
    }

    //Method to load database
    public void load() throws SQLException {
        db.load();
    }




    public void addPerson(String name, String occupation, int ageCategory,
                          String empCat, String taxId,
                          boolean usCitizen, String gender){

        AgeCategory ageCat = null;

        switch (ageCategory){
            case 0: ageCat = AgeCategory.CHILD;
                    break;
            case 1: ageCat = AgeCategory.ADULT;
                    break;
            case 2: ageCat = AgeCategory.SENIOR;
                    break;
        }

        EmploymentCategory employmentCategory = null;

        switch (empCat) {
            case "employed": employmentCategory = EmploymentCategory.EMPLOYED;
                             break;
            case "unemployed": employmentCategory = EmploymentCategory.UNEMPLOYED;
                               break;
            case "self-employed": employmentCategory = EmploymentCategory.SELFEMPLOYED;
        }

        Gender personsGender = null;

        switch (gender) {
            case "male": personsGender = Gender.MALE;
                         break;
            case "female": personsGender = Gender.FEMALE;
                           break;
        }



        Person person = new Person(name,occupation, ageCat,employmentCategory,taxId,usCitizen,personsGender);

        db.addPerson(person);


    }

    // method to save file - all communication between model and view are done through controller this method will be used in view.
    public void saveToFile(File file) throws IOException {
        db.saveToFile(file);
    }

    // same as save although this method is used to load file from previously saved one.
    public void loadFromFile(File file) throws IOException {
        db.loadFromFile(file);
    }







}
