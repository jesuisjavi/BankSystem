package com.revature.Utils;

import org.postgresql.Driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class PostgresConnectionUtil extends ConnectionUtil {
    public PostgresConnectionUtil() throws SQLException {
        DriverManager.registerDriver(new Driver());
    }

    public PostgresConnectionUtil(String url, String username, String password) throws SQLException {
        this();
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public PostgresConnectionUtil(Properties props) throws SQLException {
        this();
        this.url = props.getProperty("db.url");
        this.username = props.getProperty("db.username");
        this.password = props.getProperty("db.password");
    }

    @Override
    public Connection newConnection() throws SQLException {
        Connection c =  DriverManager.getConnection(this.url, this.username, this.password);
        c.setAutoCommit(false);
        return c;
    }
}

