package com.revature.BankApplication.Screen;

import com.revature.Person.Costumer;

import java.util.Scanner;

public class CostumerScreen implements BankSystemScreen {
    Costumer costumer;

    /**+
     * Creates a Costumer Screen for the provided costumer
     * @param costumer Costumer that will be accessing the system
     */
    public CostumerScreen(Costumer costumer){
        this.costumer = costumer;
    }

    /**+
     * Provides the costumer with several options to access the bank's system and their accounts
     * @return A BankScreen depending on the users selections
     */
    public BankSystemScreen displayScreen() {

        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to your Bank");
        System.out.println("Costumer Screen");
        System.out.println("What would you like to do?");
        System.out.println("1. Open Account");
        System.out.println("2. Access Account");
        System.out.println("3. Apply for Line of Credit");
        System.out.println("4. LogOff");

        try {
            String input = scanner.nextLine();
            switch (input) {
                case "1":
                    return new OpenAccountScreen(costumer, bank.getEmployee());
                case "2":
                    return new AccountScreen(costumer);
                case "3":
                    return new ApplyForLineOfCreditScreen(costumer, bank.getEmployee());
                case "4":
                    return new BankScreen();
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
