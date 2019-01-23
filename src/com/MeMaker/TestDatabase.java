package com.MeMaker;

import com.MeMaker.model.*;

import java.sql.SQLException;

public class TestDatabase {

    public static void main(String[] args) {
        System.out.println("Running database test");

        Database db = new Database();
        try {
            db.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }


        db.addPerson(new Person("Joe","Java Developer", AgeCategory.ADULT, EmploymentCategory.UNEMPLOYED,null,false, Gender.MALE));
        db.addPerson(new Person("Tim","Builder", AgeCategory.SENIOR, EmploymentCategory.SELFEMPLOYED,"123",false, Gender.FEMALE));
        db.addPerson(new Person("Sue","Developer", AgeCategory.SENIOR, EmploymentCategory.EMPLOYED,"4234234",true, Gender.FEMALE));


        try {
            db.save();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            db.load();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        db.disconnect();
    }
}
