package com.revature.Account;

import com.revature.Data.DepositAccountRepo;
import com.revature.Person.Costumer;
import com.revature.Utils.PostgresConnectionUtil;

import java.time.LocalDate;

public abstract class DepositAccount extends Account {
    protected DepositAccountRepo repo;

    public abstract AccountAction Withdraw(double amount, boolean isATransfer, int toAccount);

    /**+
     * This constructor is used when creating a deposit account for the first time
     * @param accountNumber Account number
     * @param initialDeposit Initial deposit
     * @param owner Owner of the account
     */
    public DepositAccount(int accountNumber, double initialDeposit, Costumer owner) {
        super(accountNumber, initialDeposit, owner, LocalDate.now());
        try{
            util = new PostgresConnectionUtil(dbProps);
            repo = new DepositAccountRepo();
            repo.setConnection(util);
        } catch (Exception ex) {
            rootLogger.error("JDBC: message: {}", ex.getMessage());
        }
    }

    /**+
     * This constructor is used when a deposit account is retrieved from the database
     * @param accountNumber Account number
     * @param balance Account balance
     * @param owner Owner of the account
     * @param dob Date when the account was opened
     */
    public DepositAccount(int accountNumber, double balance, Costumer owner, LocalDate dob) {
        super(accountNumber, balance, owner, dob);
        try{
            util = new PostgresConnectionUtil(dbProps);
            repo = new DepositAccountRepo();
            repo.setConnection(util);
        } catch (Exception ex) {
            rootLogger.error("JDBC: message: {}", ex.getMessage());
        }
    }

    /**+
     *This method is used to deposit into the account
     * @param amount Amount to be deposited
     * @param isATransfer {@code true} if the withdrawal is part of a transfer between account
     * @param fromAccount If the withdrawal is part of a transfer, it contains the account number where the money is coming from
     * @return AccountAction with success status and information about the transaction
     */
    public AccountAction Deposit(double amount, boolean isATransfer, int fromAccount) {
        if (amount <= 0)
            return new AccountAction(false, "Amount to be deposited needs to be greater than $0");
        balance += amount;
        try {
            repo.setConnection(util);
            repo.update(this);
            if (isATransfer)
                repo.saveTransaction(new Transaction(accountNumber, TransactionType.TRANSFER, amount, LocalDate.now(), "From Account: " + fromAccount));
            else
                repo.saveTransaction(new Transaction(accountNumber, TransactionType.DEPOSIT, amount, LocalDate.now(), ""));
        }catch (Exception ex){
            rootLogger.error("JDBC: message: {}", ex.getMessage());
        }
        return new AccountAction(true, "");
    }

    /**+
     * This method transfers money from this account to another account
     * @param toAccount Account number to where the money is going
     * @param amount Amount to be transferred
     * @return AccountAction with success status and information about the transaction
     */
    public AccountAction Transfer(DepositAccount toAccount, double amount){
        if (amount > balance)
            return new AccountAction(false, "Amount entered is greater than the available balance");
        Withdraw(amount, true, toAccount.getAccountNumber());
        toAccount.Deposit(amount, true, accountNumber);
        return new AccountAction(true, "");
    }
}
