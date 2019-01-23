package com.MeMaker.model;

import java.io.*;
import java.sql.*;
import java.util.*;

public class Database {

    private List<Person> people; // changed ArrayList to LinkedList so u can delete in optimal way rows from any position
    private Connection con;

    private int port;
    private String user;
    private String password;

    public Database (){
        people = new LinkedList<>();
    }

    public void configure(int port, String user, String password) throws Exception {
        this.port = port;
        this.user = user;
        this.password = password;

        if(con != null) {
            disconnect();
            connect();
        }
    }



    // methods for connecting to database
    public void connect() throws Exception {

        if(con != null) return;


        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new Exception("Driver not found");
        }


        String connectionUrl = "jdbc:mysql://localhost:3306/swingtest";
        con = DriverManager.getConnection(connectionUrl,"root","9903cwks");

    }

    // methods for connecting to database
    public void disconnect() {

        if(con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println("Can't close the connection");
            }
        }

    }

    //save entered data to database
    public void save() throws SQLException {

        String checkSql = "select count(*) as count from people where id =?";
        String insertSql = "insert into people (id, name, age, employment_status, tax_id, us_citizen, gender, occupation) values(?,?,?,?,?,?,?,?)";
        String updateSql= "update people set name=?, age=?, employment_status=?, tax_id=?, us_citizen=?, gender=?, occupation=? where id=?";



        try(PreparedStatement checkStmt = con.prepareStatement(checkSql);
            PreparedStatement insertStatement = con.prepareStatement(insertSql);
            PreparedStatement updateStatement = con.prepareStatement(updateSql)) {


            for (Person person : people) {
                int id = person.getId();

                String name = person.getName();
                String occupation = person.getOccupation();
                AgeCategory age = person.getAgeCategory();
                EmploymentCategory emp = person.getEmpCat();
                String taxId = person.getTaxId();
                boolean isUS = person.isUsCitizen();
                Gender gender = person.getGender();


                checkStmt.setInt(1, id);

                ResultSet checkResult = checkStmt.executeQuery();
                checkResult.next();

                int count = checkResult.getInt(1);

                if(count == 0) {
                    System.out.println("Inserting person with ID " + id);

                    int col =1;
                    insertStatement.setInt(col++,id);
                    insertStatement.setString(col++,name);
                    insertStatement.setString(col++,age.name());
                    insertStatement.setString(col++,emp.name());
                    insertStatement.setString(col++,taxId);
                    insertStatement.setBoolean(col++,isUS);
                    insertStatement.setString(col++,gender.name());
                    insertStatement.setString(col,occupation);



                    insertStatement.executeUpdate();

                }  else {
                    System.out.println("Updating person with ID " + id);

                    int col =1;
                    updateStatement.setString(col++,name);
                    updateStatement.setString(col++,age.name());
                    updateStatement.setString(col++,emp.name());
                    updateStatement.setString(col++,taxId);
                    updateStatement.setBoolean(col++,isUS);
                    updateStatement.setString(col++,gender.name());
                    updateStatement.setString(col++,occupation);
                    updateStatement.setInt(col,id);

                    updateStatement.executeUpdate();

                }


            }
        }
    }

    public void load() throws SQLException {
        people.clear();

        String sql = "select id, name, age, employment_status, tax_id, us_citizen, gender, occupation from people order by name";
        try (Statement selectStatement = con.createStatement();
             ResultSet resultSet = selectStatement.executeQuery(sql)) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String age = resultSet.getString("age");
                String emp = resultSet.getString("employment_status");
                String taxId = resultSet.getString("tax_id");
                boolean us_citizen = resultSet.getBoolean("us_citizen");
                String gender = resultSet.getString("gender");
                String occupation = resultSet.getString("occupation");

                Person person = new Person(id, name, occupation, AgeCategory.valueOf(age),
                        EmploymentCategory.valueOf(emp), taxId, us_citizen, Gender.valueOf(gender));
                people.add(person);

                Collections.sort(people, Comparator.comparingInt(Person::getId));


                System.out.println(person);

            }
        }
    }





    public void addPerson(Person person){
        people.add(person);
    }

    public void removePerson (int index) {
        people.remove(index);
    }

    public List<Person> getPeople() {
        return Collections.unmodifiableList(people); // returns List that can not be modified by other classes etc.
    }

    // method to save array to file - it uses sterilization to safe object.
    // it saves ArrayList into Array to avoid some unchecked exceptions.
    public void saveToFile(File file) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))){

            Person[] persons = people.toArray(new Person[people.size()]);

            oos.writeObject(persons);
        }
    }

    // method to load ArrayList of persons from a file.

    public void loadFromFile(File file) throws IOException{
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))){

            Person[] persons = (Person[]) ois.readObject();

            people.clear();

            people.addAll(Arrays.asList(persons));


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }





}

