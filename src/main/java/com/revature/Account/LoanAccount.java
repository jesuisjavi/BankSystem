package com.revature.Account;

import com.revature.Person.Costumer;

import java.time.LocalDate;
import java.util.Objects;


public abstract class LoanAccount extends Account{
    protected double loanAmount;
    protected int loanDuration;
    protected double interest;
    protected LocalDate paymentDueDate;

    /**+
     * This constructor is used when creating a loan account for the first time
     * @param accountNumber Account number
     * @param owner Owner of the account
     * @param loanAmount Amount of the loan
     * @param loanDuration Duration of the loan
     * @param interest Interest rate of the loan
     * @param loanDOB Date when the loan was approved and processed
     */
    public LoanAccount(int accountNumber, Costumer owner, double loanAmount, int loanDuration, double interest, LocalDate loanDOB) {
        super(accountNumber, 0, owner, loanDOB);
        this.loanAmount = loanAmount;
        this.loanDuration = loanDuration;
        this.interest= interest;
        paymentDueDate = loanDOB.plusDays(30);
    }

    /**+
     * This constructor is used when a loan account is retrieved from the database
     * @param accountNumber Account number
     * @param loanAmount Amount of the loan
     * @param loanDuration Duration of the loan
     * @param balance Balance of the account
     * @param owner Owner of the account
     * @param accountDOB Date when the loan was approved and processed
     * @param interest Interest rate of the loan
     * @param paymentDueDate Payment due date
     */
    public LoanAccount(int accountNumber, double loanAmount, int loanDuration, double balance, Costumer owner, LocalDate accountDOB, double interest, LocalDate paymentDueDate) {
        super(accountNumber, balance, owner, accountDOB);
        this.loanAmount = loanAmount;
        this.interest = interest;
        this.paymentDueDate = paymentDueDate;
        this.loanDuration = loanDuration;
    }

    /**+
     * Abstract method. This method is used to make a payment towards the loan account
     * @param amount The amount of the payment
     * @return AccountAction with success status and information about the transaction
     */
    public abstract AccountAction makePayment(double amount);

    /**+
     * This method calculates the minimum payment due for the loan account. it calculated based on the account's balance and the accounts' interest rate.
     * @return The minimum payment due for the loan account
     */
    public double getMinimumPayment() {
       return ((balance  / 10) + (balance * interest)/100);
    }

    /**+
     *
     * @return The loan amount
     */
    public double getLoanAmount() {
        return loanAmount;
    }

    /**+
     *
     * @return The payment due date
     */
    public LocalDate getDueDate(){
        return paymentDueDate;
    }

    /**+
     *
     * @return The account's interest rate
     */
    public double getInterest() {
        return interest;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        LoanAccount that = (LoanAccount) o;
        return Double.compare(that.loanAmount, loanAmount) == 0 &&
                loanDuration == that.loanDuration &&
                Double.compare(that.interest, interest) == 0 &&
                paymentDueDate.equals(that.paymentDueDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), loanAmount, loanDuration, interest, paymentDueDate);
    }
}
