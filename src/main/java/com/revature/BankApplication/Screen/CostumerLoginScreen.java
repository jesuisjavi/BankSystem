package com.revature.BankApplication.Screen;

import com.revature.Person.Costumer;
import com.revature.Utils.PasswordUtil;
import java.io.Console;
import java.util.Scanner;

public class CostumerLoginScreen implements BankSystemScreen {
    /**+
     * Login Screen for existing costumers
     * @return A BankScreen depending on the users selections
     */
    @Override
    public BankSystemScreen displayScreen() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to your Bank");
        System.out.println("Costumer Login Screen");
        System.out.println("Enter your username:");
        String userName = scanner.nextLine();
        System.out.println("Enter your password:");

        String password = scanner.nextLine();

        Costumer c = bank.AuthenticateCostumer(userName);

        if (c == null)
        {
            System.out.println("User does not exist");
            rootLogger.error("LOGIN: User not found. username: {}", userName);
            return LoginError();
        }
        else if (c.getPassword().equals((new PasswordUtil()).getHash(password)))
            return new CostumerScreen(c);
        else
        {
            System.out.println("Authentication failure");
            rootLogger.error("LOGIN: Authentication failure. username: {}", userName);
            return LoginError();
        }
    }

    /**+
     * Gives the costumer the option to try to enter their information again after an unsuccessful attempt
     * @return A BankScreen depending on the users selections
     */
    private BankSystemScreen LoginError(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("1. Try again");
        System.out.println("2. Go back");

        String input = scanner.nextLine();
        if (input.equals("2"))
            return new BankScreen();
        return new CostumerLoginScreen();
    }
}
