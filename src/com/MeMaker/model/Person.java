package com.MeMaker.model;


import lombok.Data;

import java.io.Serializable;


@Data
public class Person implements Serializable { // because we are saving it to stream and load it from a stream we have to mark it with Serializable interface.

    private static final long serialVersionUID = 6275474715899205090L;

    private static int count = 1;

    private int id;
    private String name;
    private String occupation;
    private AgeCategory ageCategory;
    private EmploymentCategory empCat;
    private String taxId;
    private boolean usCitizen;
    private Gender gender;


    public Person(String name, String occupation, AgeCategory ageCategory,
                  EmploymentCategory empCat, String taxId,
                  boolean usCitizen, Gender gender) {
        this.name = name;
        this.occupation = occupation;
        this.ageCategory = ageCategory;
        this.empCat = empCat;
        this.taxId = taxId;
        this.usCitizen = usCitizen;
        this.gender = gender;

        this.id = count;
        count++;
    }

    public Person(int id, String name, String occupation, AgeCategory ageCategory,
                  EmploymentCategory empCat, String taxId,
                  boolean usCitizen, Gender gender) {
        this(name, occupation, ageCategory, empCat, taxId, usCitizen, gender);

        this.id = id;


    }

}
