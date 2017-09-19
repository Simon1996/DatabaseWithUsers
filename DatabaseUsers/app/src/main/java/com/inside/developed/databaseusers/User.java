package com.inside.developed.databaseusers;

/**
 * Created by Simon on 19.09.2017.
 */

public class User {
    private String firstName, lastName, year;

    public User() {
    }

    public User(String firstName, String lastName, String year) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.year = year;
    }
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
