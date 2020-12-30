package com.revature.Account;

import com.revature.Data.LineOfCreditAccountRepo;
import com.revature.Person.Costumer;
import com.revature.Utils.PostgresConnectionUtil;

import java.time.LocalDate;


public class LineOfCreditAccount extends LoanAccount {
    LineOfCreditAccountRepo repo;

    /**+
     * This constructor is used when creating a line of credit account for the first time
     * @param accountNumber Account number
     * @param owner Owner of the account
     * @param creditLimit Credit limit of the account
     * @param interest Interest rate of the account
     */
    public LineOfCreditAccount(int accountNumber, Costumer owner, double creditLimit, double interest){
       super(accountNumber, owner, creditLimit, 0, interest, LocalDate.now());

        try{
            util = new PostgresConnectionUtil(dbProps);
            repo = new LineOfCreditAccountRepo();
        } catch (Exception ex) {
            rootLogger.error("JDBC: message: {}", ex.getMessage());
        }
    }

    /**+
     * This constructor is used when a line of credit account is retrieved from the database
     * @param accountNumber Account number
     * @param creditLimit Credit limit of the account
     * @param balance Account's balance
     * @param owner Owner of the account
     * @param accountDOB Date when the account was opened
     * @param interest Interest rate of the account
     * @param paymentDueDate Payment due date
     */
    public LineOfCreditAccount(int accountNumber, double creditLimit, double balance, Costumer owner, LocalDate accountDOB, double interest, LocalDate paymentDueDate) {
        super(accountNumber, creditLimit, 0,  balance, owner, accountDOB, interest, paymentDueDate);
        try{
            util = new PostgresConnectionUtil(dbProps);
            repo = new LineOfCreditAccountRepo();
        } catch (Exception ex) {
            rootLogger.error("JDBC: message: {}", ex.getMessage());
        }
    }

    /**+
     *
     * @return The available credit of the account
     */
    public double GetAvailableCredit(){
        return loanAmount - balance;
    }

    /**+
     * This method is used to make a payment towards the line of credit
     * @param amount The amount of the payment
     * @return AccountAction with success status and information about the transaction
     */
    @Override
    public AccountAction makePayment(double amount) {
        if (amount < getMinimumPayment())
            return new AccountAction(false, "This amount is less than the minimum payment due.");
        balance -= amount;
        paymentDueDate = paymentDueDate.plusDays(30);

        try {
            repo.setConnection(util);
            repo.update(this);
            repo.saveTransaction(new Transaction(accountNumber, TransactionType.PAYMENT, amount, LocalDate.now(), ""));
        }catch (Exception ex) {
            rootLogger.error("JDBC: message: {}", ex.getMessage());
        }

        return new AccountAction(true, "");
    }

    /**+
     * This method is used to withdraw from the line of credit
     * @param amount Amount to be withdrawn from the account
     * @return AccountAction with success status and information about the transaction
     */
    public AccountAction Withdraw(double amount){
        if (loanAmount - balance < amount)
            return new AccountAction(false, "Amount is greater than available credit");
        balance += amount;

        try {
            repo.setConnection(util);
            repo.update(this);
            repo.saveTransaction(new Transaction(accountNumber, TransactionType.WITHDRAWAL, amount, LocalDate.now(), ""));
        }catch (Exception ex) {
            rootLogger.error("JDBC: message: {}", ex.getMessage());
        }

        return new AccountAction(true, "Available credit is $" + (loanAmount - balance));
    }
}