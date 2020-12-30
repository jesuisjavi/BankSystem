package com.revature.Account;

import com.revature.Person.Costumer;

import java.time.LocalDate;

public class CheckingAccount extends DepositAccount {

    /**+
     * This constructor is used when creating a checking account for the first time
     * @param accountNumber Account number
     * @param initialDeposit Initial deposit
     * @param owner Owner of the account
     */
    public CheckingAccount(int accountNumber, double initialDeposit, Costumer owner) {
        super(accountNumber, initialDeposit, owner);
    }

    /**+
     * This constructor is used when a checking account is retrieved from the database
     * @param accountNumber Account number
     * @param balance Account balance
     * @param owner Owner of the account
     * @param dob Date when the account was opened
     */
    public CheckingAccount(int accountNumber, double balance, Costumer owner, LocalDate dob) {
        super(accountNumber, balance, owner, dob);
    }

    /**+
     * This method is used to withdraw from account
     * @param amount The amount to be withdrawn
     * @param isATransfer {@code true} if the withdrawal is part of a transfer between account
     * @param toAccount If the withdrawal is part of a transfer, it contains the account number where the money is going
     * @return AccountAction with success status and information about the transaction
     */
    public AccountAction Withdraw(double amount, boolean isATransfer, int toAccount) {
        if (amount > balance)
            return new AccountAction(false, "Amount to be withdrawn needs to be lower than the available balance");
        balance -= amount;
        try {
            repo.setConnection(util);
            repo.update(this);
            if (isATransfer)
                repo.saveTransaction(new Transaction(accountNumber, TransactionType.TRANSFER, amount, LocalDate.now(), "To Account: " + toAccount));
            else
                repo.saveTransaction(new Transaction(accountNumber, TransactionType.WITHDRAWAL, amount, LocalDate.now(), ""));
        }catch (Exception ex) {
            rootLogger.error("JDBC: message: {}", ex.getMessage());
        }
        return new AccountAction(true, "");
    }
}
