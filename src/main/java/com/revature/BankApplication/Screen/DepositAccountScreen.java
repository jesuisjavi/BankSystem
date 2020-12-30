package com.revature.BankApplication.Screen;

import com.revature.Account.*;
import com.revature.Data.DepositAccountRepo;
import com.revature.Person.Costumer;
import com.revature.Utils.ConnectionUtil;
import com.revature.Utils.PostgresConnectionUtil;

import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

public class DepositAccountScreen implements BankSystemScreen {
    DepositAccount account;
    ConnectionUtil util;
    Properties dbProps;
    DepositAccountRepo repo;

    /**+
     * Creates a Deposit Account Screen for the provided account
     * @param account Account to be accessed
     */
    public DepositAccountScreen(DepositAccount account) {
        this.account = account;

        try {
            dbProps = new Properties();
            dbProps.load(new FileReader(ClassLoader.getSystemClassLoader().getResource("db.properties").getFile()));
            util = new PostgresConnectionUtil(dbProps);
            repo = new DepositAccountRepo();
            repo.setConnection(util);
        }catch (SQLException | IOException throwable) {
            rootLogger.error("JDBC: message: {} username: {} account#: {}", throwable.getMessage(), account.getOwner().getUserName(), account.getAccountNumber() % 10000);
        }
    }

    /**+
     * Provides the costumer with options to access the account's information or to make a transaction on the account.
     * @return A BankScreen depending on the users selections
     */
    @Override
    public BankSystemScreen displayScreen() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to your Bank");
        if (account instanceof CheckingAccount)
            System.out.println("Checking Account Screen");
        else
            System.out.println("Savings Account Screen");
        System.out.println("What would you like to do?");
        System.out.println("1. Check Balance");
        System.out.println("2. Deposit");
        System.out.println("3. Withdrawal");
        System.out.println("4. Transfer");
        System.out.println("5. Transaction History");
        System.out.println("6. Go back");

        try {
            String input = scanner.next();
            switch (input) {
                case "1":
                    System.out.println("balance: " + account.getBalance());
                    return displayScreen();
                case "2":
                    Deposit(scanner);
                    return displayScreen();
                case "3":
                    Withdraw(scanner);
                    return displayScreen();
                case "4":
                    return Transfer(scanner);
                case "5":
                    TransactionHistory();
                    return displayScreen();
                case "6":
                    return new AccountScreen(account.getOwner());
                default:
                    return displayScreen();
            }
        }catch (Exception ex){
            System.out.println("Something went wrong. Let's try again...");
            rootLogger.error("INPUT: message: {} username: {} account#: {}", ex.getMessage(), account.getOwner().getUserName(), account.getAccountNumber() % 10000);
            return displayScreen();
        }
    }

    /**+
     * Deposits money into the account
     * @param scanner
     */
    private void Deposit(Scanner scanner){
        double deposit = -1;
        while (deposit < 0) {
            System.out.println("How much would you like to deposit?");
            deposit = scanner.nextDouble();
            if (deposit <= 0)
                System.out.println("A deposit needs to be at least one cent");
        }
        account.Deposit(deposit, false, 0);
        System.out.println("Your new balance is $" + account.getBalance());
    }

    /**+
     * Withdraws money from the account
     * @param scanner
     */
    private void Withdraw(Scanner scanner){
        double withdraw = -1;
        while (withdraw < 0 || withdraw > account.getBalance()) {
            System.out.println("How much would you like to withdraw?");
            withdraw = scanner.nextDouble();
            if (withdraw > account.getBalance())
                System.out.println("You can not withdraw more than your balance of: $" + account.getBalance());
        }
        AccountAction result = account.Withdraw(withdraw, false, 0);
        System.out.println("Your new balance is $" + account.getBalance());
        if (account instanceof SavingsAccount)
            System.out.println(result.getMessage());
    }

    /**+
     * Transfers money from this account to another account belonging to the owner of this account
     * @param scanner
     * @return A BankScreen depending on the users selections
     */
    private BankSystemScreen Transfer(Scanner scanner){
        Costumer costumer = account.getOwner();
        List<Integer> accounts = new ArrayList<>(costumer.getDepositAccounts());
        if (accounts.size() == 1) {
            System.out.println("There are no other deposit accounts to transfer money to");
            return displayScreen();
        }
        int acct;
        do {
            System.out.println("To which account are you transferring the money?");
            int acctIndex = -1;
            for (int i = 0; i < accounts.size(); i++) {
                if (accounts.get(i) == account.getAccountNumber()) {
                    acctIndex = i;
                    continue;
                }
                System.out.println("" + i + ". " + accounts.get(i));
            }
            acct = scanner.nextInt();
            if (acct == acctIndex || acct < 0 || acct >= accounts.size())
                System.out.println("Invalid index. Please try again...");
            else
                break;;
        }while (true);
        System.out.println("How much are you transferring?");
        double amount = scanner.nextDouble();
        AccountAction transfer = account.Transfer(repo.getById(accounts.get(acct)), amount);
        if (!transfer.wasSuccessful()) {
            System.out.println(transfer.getMessage());
            rootLogger.info("TRANSACTION: DepositAccount Screen. message: {} username: {}", transfer.getMessage(), costumer.getUserName());
            return displayScreen();
        }
        System.out.println("" + account.getAccountNumber() + " 's balance: " + account.getBalance());
        return displayScreen();
    }

    /**+
     * Prints the transaction history for this account
     */
    private void TransactionHistory(){
        System.out.println("Transaction History");
        for (Transaction transaction : repo.getAllTransactions(account.getAccountNumber()))
            System.out.println("Type: " + transaction.getType() + " Amount: " + transaction.getAmount() + " Date: " + transaction.getDob() + " " + transaction.getInfo());
    }
}
