package com.revature.BankApplication.Screen;

import com.revature.Account.Account;
import com.revature.Account.DepositAccount;
import com.revature.Account.LineOfCreditAccount;
import com.revature.Person.Costumer;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AccountScreen implements BankSystemScreen {
    Costumer costumer;

    public AccountScreen(Costumer costumer) {
        this.costumer = costumer;
    }

    /**+
     * Screen used by a costumer to access their accounts. the costumer can either enter manually the account number or they can all be listed for them.
     * @return A bankScreen depending on the users selections
     */
    @Override
    public BankSystemScreen displayScreen() {
        Scanner scanner = new Scanner(System.in);
        String input;

        do {
            System.out.println("Welcome to your Bank");
            System.out.println("Account Screen");
            System.out.println("1. Enter account number");
            System.out.println("2. List deposit account numbers");
            System.out.println("3. List loan account numbers");
            System.out.println("4. Go back");

            input = scanner.nextLine();
        } while (!input.equals("1") && !input.equals("2") && !input.equals("3") && !input.equals("4"));

        int accountNumber = -1;
        int index = 0;

        try {
            switch (input) {
                case "1":
                    //The costumer will enter the account's number manually
                    System.out.println("Enter your account number");
                    accountNumber = scanner.nextInt();
                    break;
                case "2":
                    //The costumer wants their deposit accounts listed and the will make a selection to access one of them.
                    List<Integer> dAccounts = new ArrayList<>(costumer.getDepositAccounts());
                    if (dAccounts.size() != 0) {
                        for (int i = 0; i < dAccounts.size(); i++)
                            System.out.println("" + i + ". " + dAccounts.get(i));
                        do {
                            System.out.println("Enter the index of the account number you would like to access");
                            index = scanner.nextInt();
                            if (index < 0 || index >= dAccounts.size())
                                System.out.println("Invalid index. Please try again...");
                            else
                                break;
                        }while(true);
                        accountNumber = dAccounts.get(index);
                    } else {
                        System.out.println("You do not have any deposit accounts.");
                        return new AccountScreen(costumer);
                    }
                    break;
                case "3":
                    //The costumer wants their loan accounts listed and the will make a selection to access one of them.
                    List<Integer> lAccounts = new ArrayList<>(costumer.getLoanAccounts());
                    if (lAccounts.size() != 0) {
                        for (int i = 0; i < lAccounts.size(); i++) {
                            System.out.println("" + i + ". " + lAccounts.get(i));
                        }
                        do {
                            System.out.println("Enter the index of the account number you would like to access");
                            index = scanner.nextInt();
                            if (index < 0 || index >= lAccounts.size())
                                System.out.println("Invalid index. Please try again...");
                            else
                                break;
                        }while(true);
                        accountNumber = lAccounts.get(index);
                    } else {
                        System.out.println("You do not have any loan accounts.");
                        return new AccountScreen(costumer);
                    }
                    break;
                case "4":
                    return new CostumerScreen(costumer);
            }
        }catch (Exception ex){
            System.out.println("Something went wrong. Let's try again...");
            rootLogger.error("INPUT: message: {} username: {}", ex.getMessage() ,costumer.getUserName());
            return new AccountScreen(costumer);
        }
        //the account is retrieved and used to create a Screen depending on the type it is.
        Account account = bank.GetAccount(accountNumber);
        if (account != null){
            if (account instanceof DepositAccount)
                return new DepositAccountScreen((DepositAccount) account);
            else
                return new LineOfCreditAccountScreen((LineOfCreditAccount) account);
        }
        System.out.println("Wrong account number");
        return displayScreen();
    }
}
