package com.revature.BankApplication.Screen;

import com.revature.Account.LineOfCreditAccount;
import com.revature.Account.Transaction;
import com.revature.Data.LineOfCreditAccountRepo;
import com.revature.Utils.ConnectionUtil;
import com.revature.Utils.PostgresConnectionUtil;

import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Properties;
import java.util.Scanner;

public class LineOfCreditAccountScreen implements BankSystemScreen {
    LineOfCreditAccount account;
    ConnectionUtil util;
    Properties dbProps;
    LineOfCreditAccountRepo repo;

    /**+
     *
     * @param account
     */
    public LineOfCreditAccountScreen(LineOfCreditAccount account) {
        this.account = account;

        try {
            dbProps = new Properties();
            dbProps.load(new FileReader(ClassLoader.getSystemClassLoader().getResource("db.properties").getFile()));
            util = new PostgresConnectionUtil(dbProps);
            repo = new LineOfCreditAccountRepo();
            repo.setConnection(util);
        }catch (SQLException | IOException throwable) {
            rootLogger.error("JDBC: message: {} username: {} account#: {}", throwable.getMessage(), account.getOwner().getUserName());
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
        System.out.println("Line of Credit Account Screen");
        System.out.println("What would you like to do?");
        System.out.println("1. Show Balance");
        System.out.println("2. Show Available Credit");
        System.out.println("3. Make Payment");
        System.out.println("4. Show minimum payment");
        System.out.println("5. Show payment due date");
        System.out.println("6. Withdrawal");
        System.out.println("7. Transaction History");
        System.out.println("8. Show interest rate");
        System.out.println("9. Go back");

        try {
            String input = scanner.next();
            switch (input) {
                case "1":
                    System.out.println("balance: $" + account.getBalance());
                    return displayScreen();
                case "2":
                    System.out.println("Your available credit is $" + account.GetAvailableCredit());
                    return displayScreen();
                case "3":
                    MakePayment(scanner);
                case "4":
                    System.out.println("Your minimum payment due is $" + account.getMinimumPayment());
                    return displayScreen();
                case "5":
                    System.out.println("Your payment due date is " + account.getDueDate());
                    return displayScreen();
                case "6":
                   return Withdraw(scanner);
                case "7":
                    System.out.println("Transaction History");                   
                    for (Transaction transaction : repo.getAllTransactions(account.getAccountNumber()))
                        System.out.println("Type: " + transaction.getType() + " Amount: " + transaction.getAmount() + " Date: " + transaction.getDob());                
                    return displayScreen();
                case "8":
                    System.out.println("Your line of credit interest rate is " + account.getInterest() + "%");
                    return displayScreen();
                case "9":
                    return new CostumerScreen(account.getOwner());
            }
        }catch (Exception ex){
            rootLogger.error("INPUT: message: {} username: {} account#: {}", ex.getMessage(), account.getOwner().getUserName(), account.getAccountNumber() % 10000);
            return displayScreen();
        }
        return null;
    }

    /**+
     * This method is used to make a payment towards this loan account
     * @param scanner
     * @return A BankScreen depending on the users selections
     */
    private BankSystemScreen MakePayment(Scanner scanner){
        if (account.getBalance() == 0) {
            System.out.println("Your account's balance is $0.00. There is no need for a payment at the moment");
            return displayScreen();
        }
        System.out.println("Minimum payment due: $" + account.getMinimumPayment());
        double pmt = -1;
        while (pmt < 0) {
            System.out.println("How much would you like to pay?");
            try {
                pmt = scanner.nextDouble();
            } catch (InputMismatchException ex) {
                System.out.println("The payment amount must be a number");
                continue;
            }
            if (pmt < account.getMinimumPayment()) {
                System.out.println("The amount entered is less than the minimum payment due.");
                pmt = -1;
            }
            if (pmt > account.getBalance()) {
                pmt = account.getBalance();
                System.out.println("The payment cannot be more than the account balance. Your payment will be $" + pmt);
            }
        }
        account.makePayment(pmt);
        System.out.println("Your current balance is $" + account.getBalance());
        return displayScreen();
    }

    /**+
     * This method is used to withdraw money from this Loan Account
     * @param scanner
     * @return A BankScreen depending on the users selections
     */
    private BankSystemScreen Withdraw(Scanner scanner){
        double withdraw = -1;
        while (withdraw <= 0 || withdraw > account.GetAvailableCredit()) {
            System.out.println("How much would you like to withdraw?");
            withdraw = scanner.nextDouble();
            if (withdraw <= 0)
                System.out.println("You need to withdraw at least one cent");
            else if (withdraw > account.GetAvailableCredit())
                System.out.println("You can not withdraw more than your available credit of: $" + account.GetAvailableCredit());
        }
        account.Withdraw(withdraw);
        System.out.println("Your new balance is $" + account.getBalance());
        System.out.println("Your available credit is $" + account.GetAvailableCredit());
        return displayScreen();
    }
}
