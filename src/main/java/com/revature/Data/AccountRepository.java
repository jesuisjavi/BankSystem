package com.revature.Data;

import com.revature.Account.Transaction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collection;

public interface AccountRepository<ID, T> {
    Logger rootLogger = LogManager.getRootLogger();

    Collection<T> getAll(Integer owner);
    T getById(ID accountNumber);
    void save(T account);
    void update(T account);
    void delete(T obj);
    Collection<Transaction> getAllTransactions(ID account);
    void saveTransaction(Transaction transaction);
}
