package com.revature.Account;

import com.revature.Person.Costumer;

import java.time.LocalDate;
import java.util.Objects;

public class SavingsAccount extends DepositAccount {
    int monthlyWithdrawalsLeft;

    /**+
     * This constructor is used when creating a checking account for the first time
     * @param accountNumber Account number
     * @param initialDeposit Initial deposit
     * @param owner Owner of the account
     */
    public SavingsAccount(int accountNumber, double initialDeposit, Costumer owner) {
        super(accountNumber, initialDeposit, owner);
        monthlyWithdrawalsLeft = 6;
    }

    /**+
     * This constructor is used when a savings account is retrieved from the database
     * @param accountNumber Account number
     * @param balance Account balance
     * @param owner Owner of the account
     * @param dob Date when the account was opened
     * @param monthlyWithdrawalsLeft
     */
    public SavingsAccount(int accountNumber, double balance, Costumer owner, LocalDate dob, int monthlyWithdrawalsLeft) {
        super(accountNumber, balance, owner, dob);
        this.monthlyWithdrawalsLeft = monthlyWithdrawalsLeft;
    }

    /**+
     * This method is used to withdraw from account
     * @param amount Amount to be withdrawn
     * @param isATransfer {@code true} if the withdrawal is part of a transfer between account
     * @param toAccount If the withdrawal is part of a transfer, it contains the account number where the money is going
     * @return AccountAction with success status and information about the transaction
     */
    public AccountAction Withdraw(double amount, boolean isATransfer, int toAccount) {
        if (monthlyWithdrawalsLeft == 0)
            return new AccountAction(false, "You have reached the allowed number of withdrawals this month. Visit a branch");
        else if (amount > balance)
            return new AccountAction(false, "Amount to be withdrawn needs to be lower than the available balance");
        balance -= amount;
        monthlyWithdrawalsLeft--;
        try{
            repo.setConnection(util);
            repo.update(this);
            if (isATransfer)
                repo.saveTransaction(new Transaction(accountNumber, TransactionType.TRANSFER, amount, LocalDate.now(), "To Account: " + toAccount));
            else
                repo.saveTransaction(new Transaction(accountNumber, TransactionType.WITHDRAWAL, amount, LocalDate.now(), ""));
        }catch (Exception ex) {
            rootLogger.error("JDBC: message: {}", ex.getMessage());
        }

        return new AccountAction(true, "You can only do " + monthlyWithdrawalsLeft + " more withdrawals this month");
    }

    /**+
     *
     * @return The amount of allowed withdrawals left for the month
     */
    public int getMonthlyWithdrawalsLeft() {
        return monthlyWithdrawalsLeft;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SavingsAccount that = (SavingsAccount) o;
        return monthlyWithdrawalsLeft == that.monthlyWithdrawalsLeft;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), monthlyWithdrawalsLeft);
    }
}
