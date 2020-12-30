package com.revature.Data;

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

public class CostumerRepo implements CostumerRepository<Integer, Costumer> {
    private ConnectionUtil util;

    public void setConnection(ConnectionUtil connection){
        util = connection;
    }

    /**+
     * Retrieves from the database all the accounts of the type {type} that belong to the costumer with this ssn
     * @param ssn Social Security Number of the costumer
     * @param type Type of account to be retrieved {Deposit Account or LineOfCredit Account}
     * @return A collection with all the account numbers of type {type} belonging to the costumer with Social Security {ssn}
     */
    @Override
    public Collection<Integer> getAllAccounts(Integer ssn, String type) {
        List<Integer> accounts = new ArrayList<>();
        String sql;
        if (type.equals("DepositAccount"))
            sql = "select accountnumber from depositaccount where depositaccount.ssn =?";
        else
            sql = "select accountnumber from lineofcreditaccount where lineofcreditaccount.ssn =?";

        try(Connection c = this.util.newConnection()) {
            try(PreparedStatement ps = c.prepareStatement(sql)) {
                c.setAutoCommit(false);
                ps.setInt(1, ssn);
                ResultSet r = ps.executeQuery();

                while (r.next()) {
                    int account = r.getInt("accountnumber");
                    accounts.add(account);
                }
                c.commit();
                return accounts;
            } catch(SQLException ex){
                c.rollback();
            }
        } catch (SQLException throwable) {
            rootLogger.error("JDBC: SQLException message: {}", throwable.getMessage());
        }
        return null;
    }

    /**+
     * Retrieves from the database the costumer with Social Security Number {ssn}
     * @param ssn Social Security Number
     * @return The costumer associated with the Social Security Number provided
     */
    @Override
    public Costumer getById(Integer ssn) {
        Costumer costumer = null;
        String sql = "select ssn, firstname, lastname, dob, username, password from costumer where costumer.ssn =?";

        try(Connection c = this.util.newConnection()) {
            try(PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setInt(1, ssn);
                ResultSet r = ps.executeQuery();

                while (r.next()) {
                    String firstName = r.getString("firstname");
                    String lastName = r.getString("lastname");
                    LocalDate dob = LocalDate.parse(r.getString("dob"));
                    String username = r.getString("username");
                    String password = r.getString("password");

                    costumer = new Costumer(firstName , lastName, ssn, username, password, dob);
                }
                c.commit();
                return costumer;
            } catch(SQLException ex){
                c.rollback();
            }
        } catch (SQLException throwable) {
            rootLogger.error("JDBC: SQLException message: {}", throwable.getMessage());
        }
        return costumer;
    }

    /**+
     * Saves a brand new costumer to the database
     * @param obj Costumer
     */
    @Override
    public void save(Costumer obj) {

        String sql = "insert into costumer (ssn, firstname, lastname, dob, username, password) values (?, ?, ?, ?, ?, ?)";

        try(Connection c = this.util.newConnection()) {
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                c.setAutoCommit(false);
                ps.setInt(1, obj.getSSN());
                ps.setString(2, obj.getFirstName());
                ps.setString(3, obj.getLastName());
                ps.setString(4, obj.getDOB().toString());
                ps.setString(5, obj.getUserName());
                ps.setString(6, obj.getPassword());
                int r = ps.executeUpdate();
                c.commit();
            } catch (SQLException ex){
                c.rollback();
            }
        } catch (SQLException throwable) {
            rootLogger.error("JDBC: SQLException message: {}", throwable.getMessage());
        }
    }

    /**+
     * Deletes from the database the Costumer provided
     * @param obj Costumer to be deleted
     */
    @Override
    public void delete(Costumer obj) {
        String sql = "delete from costumer where costumer.ssn =?";

        try(Connection c = this.util.newConnection()) {
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                c.setAutoCommit(false);
                ps.setInt(1, obj.getSSN());
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
     * Retrieves from the database the costumer with Username {username}
     * @param username Username
     * @return The costumer associated with the Username provided
     */
    public Costumer getByUsername(String username) {
        Costumer costumer = null;
        String sql = "select ssn, firstname, lastname, dob, username, password from costumer where costumer.username =?";

        try(Connection c = this.util.newConnection()) {
            try(PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setString(1, username);
                ResultSet r = ps.executeQuery();

                while (r.next()) {
                    int ssn = r.getInt("ssn");
                    String firstName = r.getString("firstname");
                    String lastName = r.getString("lastname");
                    LocalDate dob = LocalDate.parse(r.getString("dob"));
                    String password = r.getString("password");

                    costumer = new Costumer(firstName, lastName, ssn, username, password, dob);
                }
                c.commit();
                return costumer;
            } catch(SQLException ex){
                c.rollback();
            }
        } catch (SQLException throwable) {
            rootLogger.error("JDBC: SQLException message: {}", throwable.getMessage());
        }
        return costumer;
    }

}
