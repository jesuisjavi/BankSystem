package com.revature.Person;

import com.revature.Data.CostumerRepo;
import com.revature.Utils.ConnectionUtil;
import com.revature.Utils.PostgresConnectionUtil;

import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Properties;


public class Costumer extends Person {
    ConnectionUtil util;
    Properties dbProps;
    CostumerRepo costumerRepo;

    /**+
     * Creates a Costumer
     * @param firstName First Name
     * @param lastName Last Name
     * @param ssn Social Security Number
     * @param userName Username
     * @param password Password Hash
     * @param dob Date of birth
     */
    public Costumer(String firstName, String lastName, int ssn, String userName, String password, LocalDate dob) {
        super(firstName, lastName, ssn, userName, password, dob);

        try {
            dbProps = new Properties();
            dbProps.load(new FileReader(ClassLoader.getSystemClassLoader().getResource("db.properties").getFile()));
            util = new PostgresConnectionUtil(dbProps);
            costumerRepo = new CostumerRepo();
            costumerRepo.setConnection(util);
        }catch (SQLException | IOException throwable) {
            rootLogger.error("JDBC: message: {}", throwable.getMessage());
        }
    }

    /**+
     * Retrieves from the data base all the loan account numbers from this costumer
     * @return A collection with all the loan account numbers from this costumer
     */
    public Collection<Integer> getLoanAccounts() {
        try {
            return costumerRepo.getAllAccounts(ssn, "LineOfCreditAccount");
        }catch (Exception ex) {
            rootLogger.error("JDBC: message: {}", ex.getMessage());
        }
        return null;
    }
    /**+
     * Retrieves from the data base all the deposit account numbers from this costumer
     * @return A collection with all the deposit account numbers from this costumer
     */
    public Collection<Integer> getDepositAccounts() {
        try {
            return costumerRepo.getAllAccounts(ssn, "DepositAccount");
        }catch (Exception ex) {
            rootLogger.error("JDBC: message: {}", ex.getMessage());
        }
        return null;
    }
}
