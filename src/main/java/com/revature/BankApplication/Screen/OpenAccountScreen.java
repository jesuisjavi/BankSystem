package com.revature.BankApplication.Screen;

import com.revature.Account.AccountAction;
import com.revature.Person.Costumer;
import com.revature.Person.Employee;

import java.util.Scanner;

public class OpenAccountScreen implements BankSystemScreen {

    Costumer costumer;
    Employee employee;

    /**+
     *
     * @param costumer Costumer who will be opening the account
     * @param employee Employee who will be opening the account for the costumer
     */
    public OpenAccountScreen(Costumer costumer, Employee employee){
        this.costumer = costumer;
        this.employee = employee;
    }
    /**+
     * Opens a new account of type {Checking or Savings}
     * @return A BankScreen depending on the users selections
     */
    @Override
    public BankSystemScreen displayScreen() {
        System.out.println("Hello, my name is " + employee.getFirstName() + " " + employee.getLastName() + " and I will be your banker today.");
        try {
            Scanner scanner = new Scanner(System.in);
            String type = "";
            double deposit = -1;

            while (!type.equals("1") && !type.equals("2")) {
                System.out.println("Which type of account would you like to open?");
                System.out.println("1. Checking");
                System.out.println("2. Savings");

                type = scanner.nextLine();
            }

            while ((type.equals("1") && deposit < 25) || (type.equals("2") && deposit < 50)) {
                System.out.println("The minimum deposit for a " + type + " account is:");
                switch (type) {
                    case "1":
                        System.out.println("$25");
                        break;
                    case "2":
                        System.out.println("$50");
                        break;
                }
                System.out.println("How much would you like to deposit?");
                deposit = scanner.nextDouble();
            }

            AccountAction action;
            if (type.equals("1"))
                action = bank.OpenDepositAccount(costumer, "Checking", deposit);
            else
                action = bank.OpenDepositAccount(costumer, "Savings", deposit);

            if (action.wasSuccessful()) {
                System.out.println("Your account has been created.");
                System.out.println("Account number: " + action.getMessage());
                System.out.println("Congratulations on your new account. Goodbye!");
            }
            else{
                System.out.println("Something went wrong. Let's try again...");
                return displayScreen();
            }

        } catch (Exception ex){
            rootLogger.error("INPUT: message: {}", ex.getMessage());
            return displayScreen();
        }
        return new CostumerScreen(costumer);
    }
}