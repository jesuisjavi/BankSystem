package com.revature.Account;

import com.revature.Person.Costumer;
import com.revature.Utils.ConnectionUtil;
import com.revature.Utils.PostgresConnectionUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Properties;

/**+
 *
 */
public abstract class Account {
    protected int accountNumber;
    protected double balance;
    protected Costumer owner;
    protected LocalDate accountDOB;
    ConnectionUtil util;
    Properties dbProps;
    Logger rootLogger;

    /**+
     *
     * @param accountNumber The account number
     * @param balance The account balance
     * @param owner The account owner
     * @param accountDOB The date the account was created
     */
    public Account(int accountNumber, double balance, Costumer owner, LocalDate accountDOB) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.owner = owner;
        this.accountDOB = accountDOB;
        rootLogger = LogManager.getRootLogger();

        try {
            dbProps = new Properties();
            dbProps.load(new FileReader(ClassLoader.getSystemClassLoader().getResource("db.properties").getFile()));
            util = new PostgresConnectionUtil(dbProps);
        }catch (SQLException | IOException throwable) {
            rootLogger.error("JDBC: message: {}", throwable.getMessage());
        }
    }

    /**+
     *
     * @return The account number
     */
    public int getAccountNumber() {
        return accountNumber;
    }

    /**+
     *
     * @return The account's balance
     */
    public double getBalance() {
        return balance;
    }

    /**+
     *
     * @return The owner of the account
     */
    public Costumer getOwner() {
        return owner;
    }

    /**+
     *
     * @return The date the account was opened
     */
    public LocalDate getAccountDOB() {
        return accountDOB;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return accountNumber == account.accountNumber &&
                Double.compare(account.balance, balance) == 0 &&
                owner.equals(account.owner) &&
                accountDOB.equals(account.accountDOB) &&
                util.equals(account.util) &&
                dbProps.equals(account.dbProps);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNumber, balance, owner, accountDOB, util, dbProps);
    }
}
