package com.revature.Person;

import com.revature.Account.AccountAction;

import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;

public class Employee extends Person {
    /**+
     * Creates an Employee
     * @param firstName First Name
     * @param lastName Last Name
     * @param ssn Social Security Number
     * @param userName Username
     * @param password Password Hash
     * @param dob Date of birth
     */
    public Employee(String firstName, String lastName, int ssn, String userName, String password, LocalDate dob) {
        super(firstName, lastName, ssn, userName, password, dob);
    }

    /**+
     * Processes a credit application for a costumer given their annual salary and debt
     * @param costumer Costumer applying for credit
     * @param salary Costumer's annual salary
     * @param debt Costumer's total debt
     * @return An AccountAction with the approval status and information about the interest rate if approved or information about the denial
     */
    public AccountAction ProcessCreditApplication(Costumer costumer, double salary, double debt){
         if (debt < (salary * 4 / 10))
             return new AccountAction(true, "" + ThreadLocalRandom.current().nextInt(18, 23 + 1));
         return new AccountAction(false, "debt to income ratio is too high.");
    }
}
