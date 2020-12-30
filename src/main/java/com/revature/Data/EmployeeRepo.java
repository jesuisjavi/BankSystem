package com.revature.Data;

import com.revature.Person.Employee;
import com.revature.Utils.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;

public class EmployeeRepo implements EmployeeRepository<Integer, Employee> {
    private ConnectionUtil util;

    public void setConnection(ConnectionUtil connection){
        util = connection;
    }

    @Override
    public Collection<Employee> getAll() {
        return null;
    }

    @Override
    public Employee getById(Integer ssn) {
        Employee employee = null;
        String sql = "select firstname, lastname, dob, username, password from employee where employee.ssn =?";

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

                    employee = new Employee(firstName , lastName, ssn, username, password, dob);
                }
                c.commit();
                return employee;
            } catch(SQLException ex){
                c.rollback();
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return employee;
    }

    @Override
    public Employee getByUsername(String username) {
        Employee employee = null;
        String sql = "select ssn, firstname, lastname, dob, password from employee where employee.username =?";

        try(Connection c = this.util.newConnection()) {
            try(PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setBytes(1, username.getBytes());
                ResultSet r = ps.executeQuery();

                while (r.next()) {
                    int ssn = r.getInt("ssn");
                    String firstName = r.getString("firstname");
                    String lastName = r.getString("lastname");
                    LocalDate dob = LocalDate.parse(r.getString("dob"));
                    String password = r.getString("password");

                    employee = new Employee(firstName, lastName, ssn, username, password, dob);
                }
                c.commit();
                return employee;
            } catch(SQLException ex){
                c.rollback();
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    @Override
    public Integer save(Employee obj) {
        return null;
    }

    @Override
    public void delete(Employee obj) {

    }
}
