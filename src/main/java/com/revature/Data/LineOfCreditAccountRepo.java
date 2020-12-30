package com.revature.Data;

import com.revature.Account.LineOfCreditAccount;
import com.revature.Account.LoanAccount;
import com.revature.Account.Transaction;
import com.revature.Account.TransactionType;
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

public class LineOfCreditAccountRepo implements AccountRepository<Integer, LoanAccount> {
    private ConnectionUtil util;
    private final CostumerRepo costumerRepo;

    public LineOfCreditAccountRepo() {
        costumerRepo = new CostumerRepo();
    }

    public void setConnection(ConnectionUtil connection){
        util = connection;
    }

    /**+
     * Retrieves all the Line of Credit accounts from the database that belong to the costumer with the Social Security Number provided
     * @param owner Social Security Number
     * @return A collection with all the Line of Credit accounts belonging to the costumer with the Social Security Number provided
     */
    @Override
    public Collection<LoanAccount> getAll(Integer owner) {
        List<LoanAccount> accounts = new ArrayList<>();
        String sql = "select accountnumber, ssn, balance, creditlimit, interest, dob, pmtduedate from depositaccount where depositaccount.ssn =?";

        try(Connection c = this.util.newConnection()) {
            try(PreparedStatement ps = c.prepareStatement(sql)) {
                c.setAutoCommit(false);
                ps.setInt(1, owner);
                ResultSet r = ps.executeQuery();

                while (r.next()) {
                    costumerRepo.setConnection(util);
                    Costumer costumer = costumerRepo.getById(r.getInt("ssn"));
                    double balance = r.getDouble("balance");
                    double creditLimit = r.getDouble("creditlimit");
                    double interest = r.getDouble("interest");
                    LocalDate dob = LocalDate.parse(r.getString("dob"));
                    LocalDate pmtduedate = LocalDate.parse(r.getString("pmtduedate"));

                    accounts.add(new LineOfCreditAccount(owner, creditLimit, balance, costumer, dob, interest, pmtduedate));
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
     * retrieves from the database the Line Of Credit Account with the account number provided
     * @param accountNumber Account Number
     * @return The Line Of Credit Account associated with the account number provided
     */
    @Override
    public LoanAccount getById(Integer accountNumber) {
        LineOfCreditAccount account = null;
        String sql = "select accountnumber, ssn, balance, creditlimit, interest, dob, pmtduedate from lineofcreditaccount where lineofcreditaccount.accountnumber =?";

        try(Connection c = this.util.newConnection()) {
            try(PreparedStatement ps = c.prepareStatement(sql)) {
                c.setAutoCommit(false);
                ps.setInt(1, accountNumber);
                ResultSet r = ps.executeQuery();

                while (r.next()) {
                    costumerRepo.setConnection(util);
                    Costumer costumer = costumerRepo.getById(r.getInt("ssn"));
                    double balance = r.getDouble("balance");
                    double creditLimit = r.getDouble("creditlimit");
                    double interest = r.getDouble("interest");
                    LocalDate dob = LocalDate.parse(r.getString("dob"));
                    LocalDate pmtduedate = LocalDate.parse(r.getString("pmtduedate"));

                    account = new LineOfCreditAccount(accountNumber, creditLimit, balance, costumer, dob, interest, pmtduedate);
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
     * Saves a brand new Line of Credit Account to the database
     * @param account Account to be saved in the database
     */
    @Override
    public void save(LoanAccount account) {
        String sql = "insert into lineofcreditaccount (accountnumber, ssn, balance, creditlimit, interest, dob, pmtduedate) values (?, ?, ?, ?, ?, ?, ?) ";

        try(Connection c = this.util.newConnection()) {
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                c.setAutoCommit(false);
                ps.setInt(1, account.getAccountNumber());
                ps.setInt(2, account.getOwner().getSSN());
                ps.setDouble(3, account.getBalance());
                ps.setDouble(4, account.getLoanAmount());
                ps.setDouble(5, account.getInterest());
                ps.setString(6, account.getAccountDOB().toString());
                ps.setString(7, account.getDueDate().toString());
                int r = ps.executeUpdate();
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
     * Updates a Line Of Credit Account account in the database
     * @param account Account to be saved in the database
     */
    @Override
    public void update(LoanAccount account) {
        String sql = "update lineofcreditaccount set balance =?, pmtduedate =? where lineofcreditaccount.accountnumber =?";

        try(Connection c = this.util.newConnection()) {
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                c.setAutoCommit(false);
                ps.setDouble(1, account.getBalance());
                ps.setString(2, account.getDueDate().toString());
                ps.setInt(3, account.getAccountNumber());

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
     * Deletes the provided Line of Credit from the database
     * @param obj Account to be deleted
     */
    @Override
    public void delete(LoanAccount obj) {
        String sql = "delete from lineofcreditaccount where lineofcreditaccount.accountnumber =?";

        try(Connection c = this.util.newConnection()) {
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                c.setAutoCommit(false);
                ps.setInt(1, obj.getAccountNumber());
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
     * Retrieves all the Transaction from the Line of Credit Account associated with the account number provided
     * @param account Account Number
     * @return A collection with all the Transactions that have happened in the Line Of Credit Account associated with the account number provided
     */
    @Override
    public Collection<Transaction> getAllTransactions(Integer account) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "select accountnumber, type, amount, dob, info from lineofcreditaccttranshist where lineofcreditaccttranshist.accountnumber =?";

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
        String sql = "insert into lineofcreditaccttranshist (accountnumber, type, amount, dob, info) values (?, ?, ?, ?, ?)";

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
}
