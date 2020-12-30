package com.revature.Person;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;

public abstract class Person {
    protected String firstName;
    protected String lastName;
    protected int ssn;
    protected String userName;
    protected String password;
    protected LocalDate dob;
    protected Logger rootLogger;

    /**+
     * Creates a Person. A person can be an bank employe or a costumer
     * @param firstName First Name
     * @param lastName Last Name
     * @param ssn Social Security Number
     * @param userName Username
     * @param password Password hash
     * @param dob Date of birth
     */
    public Person(String firstName, String lastName, int ssn, String userName, String password, LocalDate dob) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.ssn = ssn;
        this.userName = userName;
        this.password = password;
        this.dob = dob;
        rootLogger = LogManager.getRootLogger();
    }

    /**+
     *
     * @return The person's first name
     */
    public String getFirstName() { return firstName; }

    /**+
     *
     * @return the peron's last name
     */
    public String getLastName() { return lastName; }

    /**+
     *
     * @return The person's social security number
     */
    public int getSSN() {
        return ssn;
    }

    /**+
     *
     * @return The person's username
     */
    public String getUserName() {
        return userName;
    }

    /**+
     *
     * @return The hash of the person's password
     */
    public String getPassword() {
        return password;
    }

    /**+
     *
     * @return The date of birth of the person
     */
    public LocalDate getDOB() {
        return dob;
    }
}
