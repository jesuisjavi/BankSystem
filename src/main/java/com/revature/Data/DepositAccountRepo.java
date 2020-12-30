package com.revature.Data;

import com.revature.Account.*;
import com.revature.Person.Costumer;
import com.revature.Utils.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DepositAccountRepo implements AccountRepository<Integer, DepositAccount> {
    private ConnectionUtil util;
    private final CostumerRepo costumerRepo;

    public DepositAccountRepo() {
        costumerRepo = new CostumerRepo();
    }

    public void setConnection(ConnectionUtil connection){
        util = connection;
    }

    /**+
     * Retrieves all the deposit accounts from the database that belong to the costumer with the Social Security Number provided
     * @param owner Social Security Number
     * @return A collection with all the Deposit Accounts belonging to the costumer with the Social Security Number provided
     */
    @Override
    public Collection<DepositAccount> getAll(Integer owner) {
        List<DepositAccount> accounts = new ArrayList<>();
        String sql = "select accountnumber, ssn, balance, dob, issavings, withdrawals from depositaccount where depositaccount.ssn =?";

        try(Connection c = this.util.newConnection()) {
            try(PreparedStatement ps = c.prepareStatement(sql)) {
                c.setAutoCommit(false);
                ps.setInt(1, owner);
                ResultSet r = ps.executeQuery();

                while (r.next()) {
                    int accountNumber = r.getInt("accountnumber");
                    double balance = r.getDouble("balance");
                    LocalDate dob = LocalDate.parse(r.getString("dob"));
                    boolean isSavings = r.getBoolean("issavings");
                    int withdrawals = r.getInt("withdrawals");
                    Costumer costumer = costumerRepo.getById(owner);

                    if (isSavings)
                        accounts.add(new SavingsAccount(accountNumber, balance, costumer, dob, withdrawals));
                    else
                        accounts.add(new CheckingAccount(accountNumber, balance, costumer, dob));
                }
                c.commit();
                return accounts;
            } catch(SQLException ex){
                rootLogger.error("JDBC: SQLException message: {}", ex.getMessage());
                c.rollback();
            }
        } catch (SQLException throwable) {
            rootLogger.error("JDBC: SQLException message: {}", throwable.getMessage());
        }
        return null;
    }

    /**+
     * retrieves from the database the Deposit Account with the account number provided
     * @param accountNumber Account Number
     * @return The Deposit Account associated with the account number provided
     */
    @Override
    public DepositAccount getById(Integer accountNumber) {
        DepositAccount account = null;
        String sql = "select accountnumber, ssn, balance, dob, issavings, withdrawals from depositaccount where depositaccount.accountnumber =?";

        try(Connection c = this.util.newConnection()) {
            try(PreparedStatement ps = c.prepareStatement(sql)) {
                c.setAutoCommit(false);
                ps.setInt(1, accountNumber);
                ResultSet r = ps.executeQuery();

                while (r.next()) {
                    costumerRepo.setConnection(util);
                    Costumer costumer = costumerRepo.getById(r.getInt("ssn"));
                    double balance = r.getDouble("balance");
                    LocalDate dob = LocalDate.parse(r.getString("dob"));
                    boolean isSavings = r.getBoolean("issavings");
                    int withdrawals = r.getInt("withdrawals");

                    if (isSavings)
                        account = new SavingsAccount(accountNumber, balance, costumer, dob, withdrawals);
                    else
                        account = new CheckingAccount(accountNumber, balance, costumer, dob);
                }
                c.commit();
                return account;
            } catch(SQLException ex){
                rootLogger.error("JDBC: SQLException message: {}", ex.getMessage());
                c.rollback();
            }
        } catch (SQLException throwable) {
            rootLogger.error("JDBC: SQLException message: {}", throwable.getMessage());
        }
        return account;
    }

    /**+
     * Saves a brand new Deposit Account to the database
     * @param account Account to be saved in the database
     */
    @Override
    public void save(DepositAccount account) {
        String sql = "insert into depositaccount (accountnumber, ssn, balance, dob, issavings, withdrawals) values (?, ?, ?, ?, ?, ?)";

        try(Connection c = this.util.newConnection()) {
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                c.setAutoCommit(false);
                ps.setInt(1, account.getAccountNumber());
                ps.setInt(2, account.getOwner().getSSN());
                ps.setDouble(3, account.getBalance());
                ps.setString(4, account.getAccountDOB().toString());
                ps.setBoolean(5, account instanceof SavingsAccount);
                if (account instanceof SavingsAccount)
                    ps.setInt(6, ((SavingsAccount)account).getMonthlyWithdrawalsLeft());
                else
                    ps.setInt(6, 0);
                ps.executeUpdate();
                c.commit();

            } catch (SQLException ex){
                rootLogger.error("JDBC: SQLException message: {}", ex.getMessage());
                c.rollback();
            }
        } catch (SQLException throwable) {
            rootLogger.error("JDBC: SQLException message: {}", throwable.getMessage());
        }
    }

    /**+
     * Updates a Deposit Account account in the database
     * @param account Account to be saved in the database
     */
    @Override
    public void update(DepositAccount account) {
        String sql;
        if (account instanceof SavingsAccount)
            sql = "update depositaccount set balance =?, withdrawals =? where depositaccount.accountnumber =?";
        else
            sql = "update depositaccount set balance =? where depositaccount.accountnumber =?";

        try(Connection c = this.util.newConnection()) {
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                c.setAutoCommit(false);
                if (account instanceof SavingsAccount){
                    ps.setDouble(1, account.getBalance());
                    ps.setInt(2, ((SavingsAccount)account).getMonthlyWithdrawalsLeft());
                    ps.setInt(3, account.getAccountNumber());
                }
                else{
                    ps.setDouble(1, account.getBalance());
                    ps.setInt(2, account.getAccountNumber());
                }
                ps.executeUpdate();
                c.commit();
            } catch (SQLException ex){
                rootLogger.error("JDBC: SQLException message: {}", ex.getMessage());
                c.rollback();
            }
        } catch (SQLException throwable) {
            rootLogger.error("JDBC: SQLException message: {}", throwable.getMessage());
        }
    }

    /**+
     * Deletes the provided Deposit Account from the database
     * @param obj Account to be deleted
     */
    @Override
    public void delete(DepositAccount obj) {
        String sql = "delete from depositaccount where depositaccount.accountnumber =?";

        try(Connection c = this.util.newConnection()) {
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                c.setAutoCommit(false);
                ps.setInt(1, obj.getAccountNumber());
                ps.executeUpdate();
                c.commit();
            } catch (SQLException ex){
                c.rollback();
            }
        } catch (SQLException throwable) {
            rootLogger.error("JDBC: SQLException message: {}", throwable.getMessage());
        }
    }

    /**+
     * Retrieves all the Transaction from the Deposit Account associated with the account number provided
     * @param account Account Number
     * @return A collection with all the Transactions that have happened in the Deposit Account associated with the account number provided
     */
    @Override
    public Collection<Transaction> getAllTransactions(Integer account) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "select accountnumber, type, amount, dob, info from depositaccttranshist where depositaccttranshist.accountnumber =?";

        try(Connection c = this.util.newConnection()) {
            try(PreparedStatement ps = c.prepareStatement(sql)) {
                c.setAutoCommit(false);
                ps.setInt(1, account);
                ResultSet r = ps.executeQuery();

                while (r.next()) {
                    TransactionType type = TransactionType.valueOf(r.getString("type"));
                    double amount = r.getDouble("amount");
                    LocalDate dob = LocalDate.parse(r.getString("dob"));
                    String info =  r.getString("info");
                    transactions.add(new Transaction(account, type, amount, dob, info));
                }
                c.commit();
                return transactions;
            } catch(SQLException ex){
                rootLogger.error("JDBC: SQLException message: {}", ex.getMessage());
                c.rollback();
            }
        } catch (SQLException throwable) {
            rootLogger.error("JDBC: SQLException message: {}", throwable.getMessage());
        }
        return null;
    }

    /**
     * Saves a new Transaction in the database
     * @param transaction Transaction
     */
    @Override
    public void saveTransaction(Transaction transaction) {
        String sql = "insert into depositaccttranshist (accountnumber, type, amount, dob, info) values (?, ?, ?, ?, ?)";

        try(Connection c = this.util.newConnection()) {
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                c.setAutoCommit(false);
                ps.setInt(1, transaction.getAccountNumber());
                ps.setString(2, transaction.getType().toString());
                ps.setDouble(3, transaction.getAmount());
                ps.setString(4, transaction.getDob().toString());
                ps.setString(5, transaction.getInfo());
                ps.executeUpdate();
                c.commit();
            } catch (SQLException ex){
                rootLogger.error("JDBC: SQLException message: {}", ex.getMessage());
                c.rollback();
            }
        } catch (SQLException throwable) {
            rootLogger.error("JDBC: SQLException message: {}", throwable.getMessage());
        }
    }

    /**+
     * Updates the withdrawals value of every savings account to 0
     */
    public void updateWithdrawalsLeft(){
        String sql = "update depositaccount set withdrawals = 0 where depositaccount.issavings = true";

        try(Connection c = this.util.newConnection()) {
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                c.setAutoCommit(false);
                ps.executeUpdate();
                c.commit();
            } catch (SQLException ex){
                rootLogger.error("JDBC: SQLException message: {}", ex.getMessage());
                c.rollback();
            }
        } catch (SQLException throwable) {
            rootLogger.error("JDBC: SQLException message: {}", throwable.getMessage());
        }
    }
}
