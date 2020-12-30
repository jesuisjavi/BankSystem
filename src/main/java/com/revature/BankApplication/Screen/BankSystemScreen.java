package com.revature.BankApplication.Screen;

import com.revature.Bank.Bank;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public interface BankSystemScreen {
    Bank bank = new Bank();
    Logger rootLogger = LogManager.getRootLogger();

    BankSystemScreen displayScreen();
}