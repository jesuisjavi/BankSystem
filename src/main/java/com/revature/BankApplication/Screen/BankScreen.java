package com.revature.BankApplication.Screen;

import java.util.Scanner;

public class BankScreen implements BankSystemScreen {
    /**+
     * First screen shown when someone accesses the Bank's system. GFive the option to login for existing costumers and employees, as well as the creation of a profile for new costumers.
     * @return A BankScreen depending on the users selections
     */
    public BankSystemScreen displayScreen() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to your Bank");
        System.out.println("1. Costumer Login");
        System.out.println("2. New Costumer");
        System.out.println("3. Employee Login");
        System.out.println("4. Exit");

        try {
            String input = scanner.nextLine();
            switch (input) {
                case "1":
                    return new CostumerLoginScreen();
                case "2":
                    return new NewCostumerScreen();
                case "3":
                    return new EmployeeLoginScreen();
                case "4":
                    return null;
                default:
                    return displayScreen();
            }
        }catch (Exception ex){
            System.out.println("Something went wrong. Let's try again...");
            rootLogger.error("INPUT: message: {}", ex.getMessage());
            return displayScreen();
        }
    }
}
