package com.revature.Account;

import java.time.LocalDate;
import java.util.Objects;

public class Transaction {
    int accountNumber;
    TransactionType type;
    double amount;
    LocalDate dob;
    String info;

    /**+
     *
     * @param accountNumber The account number where the transaction happenedc
     * @param type The type of transaction. { DEPOSIT, WITHDRAWAL, PAYMENT, TRANSFER }
     * @param amount The amount of the transaction
     * @param dob The date when the transaction happened
     * @param info Information about the transaction
     */
    public Transaction(int accountNumber, TransactionType type, double amount, LocalDate dob, String info) {
        this.accountNumber = accountNumber;
        this.type = type;
        this.amount = amount;
        this.dob = dob;
        this.info = info;
    }

    /**+
     *
     * @return The account number where the transaction happened
     */
    public int getAccountNumber() { return accountNumber;  }

    /**+
     *
     * @return The type of transaction. { DEPOSIT, WITHDRAWAL, PAYMENT, TRANSFER }
     */
    public TransactionType getType() {
        return type;
    }

    /**+
     *
     * @return The amount of the transaction
     */
    public double getAmount() {
        return amount;
    }

    /**+
     *
     * @return The date when the transaction happened
     */
    public LocalDate getDob() {
        return dob;
    }

    /**+
     *
     * @return Information about the transaction
     */
    public String getInfo() {
        return info;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Double.compare(that.amount, amount) == 0 &&
                type == that.type &&
                dob.equals(that.dob) &&
                info.equals(that.info);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, amount, dob, info);
    }
}


