package com.revature.Data;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collection;

public interface EmployeeRepository<ID, T> {
    Logger rootLogger = LogManager.getRootLogger();

    Collection<T> getAll();
    T getById(ID ssn);
    T getByUsername(String username);
    ID save(T obj);
    void delete(T obj);
}
