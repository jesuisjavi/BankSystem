package com.revature.BankApplication.Screen;

import com.revature.Person.Employee;
import com.revature.Utils.PasswordUtil;

import java.util.Scanner;

public class EmployeeLoginScreen implements BankSystemScreen {
    /**+
     * Login Screen for existing employees
     * @return A BankScreen depending on the users selections
     */
    @Override
    public BankSystemScreen displayScreen() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to your Bank");
        System.out.println("Employee Login Screen");
        System.out.println("Enter your username:");
        String userName = scanner.nextLine();
        System.out.println("Enter your password:");
        String password = scanner.nextLine();

        Employee employee = bank.AuthenticateEmployee(userName);

        if (employee == null)
        {
            System.out.println("User does not exist");
            rootLogger.error("LOGIN: User not found. username: {}", userName);

            return LoginError();
        }
        else if (employee.getPassword().equals((new PasswordUtil()).getHash(password)))
            return new EmployeeScreen(employee);
        else
        {
            System.out.println("Authentication failure");
            rootLogger.error("LOGIN: Authentication failure. username: {}", userName);
            return LoginError();
        }
    }

    /**+
     * Gives the employee the option to try to enter their information again after an unsuccessful attempt
     * @return A BankScreen depending on the users selections
     */
    private EmployeeLoginScreen LoginError(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("1. Try again");
        System.out.println("2. Exit");

        String input = scanner.nextLine();
        if (input.equals("2"))
            return null;
        return new EmployeeLoginScreen();
    }
}
