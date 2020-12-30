package com.revature.BankApplication.Screen;

import com.revature.Person.Employee;

import java.util.Scanner;

public class EmployeeScreen implements BankSystemScreen {

    Employee employee;
    public EmployeeScreen(Employee employee) {
        this.employee = employee;
    }

    /**+
     *
     * @return A BankScreen depending on the users selections
     */
    public BankSystemScreen displayScreen() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to your Bank");
        System.out.println("Employee Screen");
        System.out.println("What would you like to do?");
        System.out.println("1. Open Account");
        System.out.println("2. Process Credit Application");
        System.out.println("3. LogOff");
        String input = scanner.next();

        switch (input){
            case "1":
            case "2":
                break;
            case "3":
                return new BankScreen();
        }
        return null;
    }
}
