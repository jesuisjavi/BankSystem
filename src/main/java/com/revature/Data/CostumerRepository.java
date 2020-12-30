package com.revature.Data;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collection;

public interface CostumerRepository<ID, T> {
    Logger rootLogger = LogManager.getRootLogger();

    Collection<ID> getAllAccounts(Integer ssn, String type);
    T getById(ID id);
    T getByUsername(String username);
    void save(T obj);
    void delete(T obj);
}
