package com.revature.Bank;

import com.revature.Account.*;
import com.revature.Data.CostumerRepo;
import com.revature.Data.DepositAccountRepo;
import com.revature.Data.EmployeeRepo;
import com.revature.Data.LineOfCreditAccountRepo;
import com.revature.Person.Costumer;
import com.revature.Person.Employee;
import com.revature.Utils.ConnectionUtil;
import com.revature.Utils.PostgresConnectionUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Bank {
    private int[] empSSNs;
    Logger rootLogger = LogManager.getRootLogger();
    //JDBC
    Properties dbProps;
    ConnectionUtil util;
    CostumerRepo costumerRepo;
    EmployeeRepo employeeRepo;
    DepositAccountRepo depositAccountRepo;
    LineOfCreditAccountRepo lineOfCreditAccountRepo;

    public Bank() {
        empSSNs = new int[] {123456789, 987654321, 456789123};

        if (LocalDate.now().getDayOfMonth() == 1)
            ResetAllowedWithdrawals();

        try {
            dbProps = new Properties();
            dbProps.load(new FileReader(ClassLoader.getSystemClassLoader().getResource("db.properties").getFile()));
            util = new PostgresConnectionUtil(dbProps);

            costumerRepo = new CostumerRepo();
            employeeRepo = new EmployeeRepo();
            depositAccountRepo = new DepositAccountRepo();
            lineOfCreditAccountRepo = new LineOfCreditAccountRepo();

        } catch (SQLException | IOException throwable) {
            rootLogger.error("JDBC: message: {}", throwable.getMessage());
        }
    }

    /**+
     * Resets the allowed withdrawals for every savings account
     */
    private void ResetAllowedWithdrawals(){
        depositAccountRepo.updateWithdrawalsLeft();
    }

    /**+
     * Retrieves the costumer with the username provided so the costumer can be authenticated
     * @param userName Username of the costumer trying to log in
     * @return The costumer associated with the username provided or null if there is no such costumer
     */
    public Costumer AuthenticateCostumer(String userName){
        try{
            util = new PostgresConnectionUtil(dbProps);
            costumerRepo.setConnection(util);
            return costumerRepo.getByUsername(userName);
        } catch (Exception e) {
            rootLogger.error("JDBC: AuthenticateCostumer. message: {} username: {}", e.getMessage(), userName);
        }
        return null;
    }

    /**+
     * Retrieves the employee with the username provided so the employee can be authenticated
     * @param userName Username of the employee trying to log in
     * @return The employee associated with the username provided or null if there is no such costumer
     */
    public Employee AuthenticateEmployee(String userName){
        try{
            util = new PostgresConnectionUtil(dbProps);
            employeeRepo.setConnection(util);
            return employeeRepo.getByUsername(userName);
        } catch (Exception e) {
            rootLogger.error("JDBC: AuthenticateEmployee. message: {} username: {}", e.getMessage(), userName);
        }
        return null;
    }

    /**+
     * Determines if there is already a costumer with the ssn provided
     * @param ssn Social Security Number of the costumer trying to create a profile
     * @return {@code true} if there is already a costumer associated with this social security number. {@code false} otherwise
     */
    public boolean ExistingCostumer(int ssn){
        try{
            util = new PostgresConnectionUtil(dbProps);
            costumerRepo.setConnection(util);
            Costumer costumer = costumerRepo.getById(ssn);
            return costumer != null;
        } catch (Exception e) {
            rootLogger.error("JDBC: ExistingCostumer. message: {}", e.getMessage());
        }
        return false;
    }

    /**+
     * Determines if there is already a costumer with the username provided
     * @param username Username of the costumer trying to create a profile
     * @return {@code true} if there is alrwady a costumer with this username. {@code false} otherwise
     */
    public boolean ExistingUsername(String username){
        try{
            util = new PostgresConnectionUtil(dbProps);
            costumerRepo.setConnection(util);
            Costumer costumer = costumerRepo.getByUsername(username);
            return costumer != null;
        } catch (Exception e) {
            rootLogger.error("JDBC: ExistingUsername. message: {} username: {}", e.getMessage(), username);
        }
        return false;
    }

    /**+
     * Adds a brand new costumer to the database
     * @param costumer Costumer to be added to the database
     */
    public void AddNewCostumer(Costumer costumer){
        try{
            util = new PostgresConnectionUtil(dbProps);
            costumerRepo.setConnection(util);
            costumerRepo.save(costumer);
        } catch (Exception e) {
            rootLogger.error("JDBC: AddNewCostumer. message: {} username: {}", e.getMessage(), costumer.getUserName());
        }
    }

    /**+
     * Opens an account of type {Checking or Savings} belonging to {costumer} starting with {initialDeposit}
     * @param costumer Owner of the account
     * @param type Type of account to be opened {Checking or Savings}
     * @param initialDeposit Initial Deposit
     * @return AccountAction with success status and the account number of the new account.
     */
    public AccountAction OpenDepositAccount(Costumer costumer, String type, double initialDeposit){
        DepositAccount account;
        int accountNumber = Math.abs(LocalDateTime.now().hashCode());
        if (type.equals("Checking"))
            account = new CheckingAccount(accountNumber, initialDeposit, costumer);
        else
            account = new SavingsAccount(accountNumber, initialDeposit, costumer);

        try{
            util = new PostgresConnectionUtil(dbProps);
            depositAccountRepo.setConnection(util);
            depositAccountRepo.save(account);
            depositAccountRepo.saveTransaction(new Transaction(accountNumber, TransactionType.DEPOSIT, initialDeposit, LocalDate.now(), "*Initial Deposit*"));
        } catch (Exception e) {
            rootLogger.error("JDBC: OpenDepositAccount. message: {}", e.getMessage());
        }

        return new AccountAction(true, "" + accountNumber);
    }

    /**+
     * Opens an line of credit account belonging to {costumer} with credit limit {creditLimit} and interest rate {interest}
     * @param owner Owner of the account
     * @param creditLimit Credit limit of the account
     * @param interest Interest rate of the account
     * @return AccountAction with success status and the account number of the new account.
     */
    public AccountAction OpenCreditLineAccount(Costumer owner, double creditLimit, double interest){
        int accountNumber = Math.abs(LocalDateTime.now().hashCode());
        LineOfCreditAccount account = new LineOfCreditAccount(accountNumber, owner,creditLimit, interest);

        try{
            util = new PostgresConnectionUtil(dbProps);
            lineOfCreditAccountRepo.setConnection(util);
            lineOfCreditAccountRepo.save(account);
        } catch (Exception e) {
            rootLogger.error("JDBC: OpenCreditLineAccount. message: {}", e.getMessage());
        }

        return new AccountAction(true, "" + accountNumber);
    }

    /**+
     * Retrieves an account from the database
     * @param accountNumber Account number of the account needed from the database
     * @return The Account associated with the provided account number
     */
    public Account GetAccount(int accountNumber){
        try{
            util = new PostgresConnectionUtil(dbProps);
            depositAccountRepo.setConnection(util);
            DepositAccount dAccount = depositAccountRepo.getById(accountNumber);
            if (dAccount != null)
                return dAccount;

            lineOfCreditAccountRepo.setConnection(util);
            return lineOfCreditAccountRepo.getById(accountNumber);
        } catch (Exception e) {
            rootLogger.error("JDBC: GetAccount. message: {}", e.getMessage());
        }
        return null;
    }

    /**+
     * Approval Tool for Loan Accounts
     * @param costumer Costumer applying for the loan
     * @return AccountAction with information about the decision and the interest rate in case of approval. Loan can be denied due to poor credit, instantly approved or sent to review.
     */
    public AccountAction LoanApprovalTool(Costumer costumer){
        int creditScore = GetCreditScore(costumer.getSSN());
        if (creditScore <= 600)
            return new AccountAction(false, "poor credit");
        else if (creditScore <= 670)
            return new AccountAction(true, "Review Needed");
        else if (creditScore <= 770)
            return new AccountAction(true, "" + ThreadLocalRandom.current().nextInt(6, 13 + 1));
        else
            return new AccountAction(true, "" + ThreadLocalRandom.current().nextInt(10, 15 + 1));
    }

    /**+
     * Method used to simulate obtaining a costumer's credit score based on their social security number
     * @param SSN Social Security Number of the costumer the credit score is needed.
     * @return An integer in the range 500-850
     */
    private int GetCreditScore(int SSN){
        return ThreadLocalRandom.current().nextInt(500, 850 + 1);
    }

    /**+
     * Retrieves a banker from the database.
     * @return A bank employee
     */
    public Employee getEmployee(){
        Random rand = new Random();
        int ssn = empSSNs[rand.nextInt(3)];
        Employee employee;

        try{
            util = new PostgresConnectionUtil(dbProps);
            employeeRepo.setConnection(util);
            employee = employeeRepo.getById(ssn);
            return employee;
        } catch (Exception e) {
            rootLogger.error("JDBC: GetEmployee. message: {}", e.getMessage());
        }
        return null;
    }
}
