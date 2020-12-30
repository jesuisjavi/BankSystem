package com.revature.BankApplication.Screen;

import com.revature.Person.Costumer;
import com.revature.Utils.PasswordUtil;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class NewCostumerScreen implements BankSystemScreen {

    /**+
     * Creates a profile for a new costumer
     * @return A BankScreen depending on the users selections
     */
    @Override
    public BankSystemScreen displayScreen() {
        Scanner scanner = new Scanner(System.in);
        int ssn;

        try {
            System.out.println("Enter your Social Security Number");
            try {
                ssn = scanner.nextInt();
            } catch (InputMismatchException ex) {
                System.out.println("Input must be a number. Try again");
                return new NewCostumerScreen();
            }

            if (bank.ExistingCostumer(ssn)) {
                System.out.println("There is already a costumer associated with this Social Security Number. Please, visit a branch.");
                rootLogger.info("INPUT: Attempt to use an existing costumer's SSN");
                return new BankScreen();
            }

            System.out.println("Enter your first name");
            String firstName = scanner.next();
            System.out.println("Enter your last name");
            String lastName = scanner.next();
            LocalDate dobDate = null;
            while (dobDate == null) {
                try {
                    System.out.println("Enter your date of birth YYYY-MM-DD");
                    String dob = scanner.next();
                    dobDate = LocalDate.parse(dob);
                } catch (DateTimeParseException e) {
                    System.out.println("The date was not entered in the correct format. Try again");
                }
            }

            String username = "";
            while (username.equals("")) {
                System.out.println("Enter the username you would like to use");
                username = scanner.next();
                if (bank.ExistingUsername(username)) {
                    username = "";
                    System.out.println("There is a costumer with that username, please type a different one");
                }
            }
            String password;
            String confirmation;
            String passwordHash;
            while (true) {
                System.out.println("Enter the password");
                password = scanner.next();
                System.out.println("Confirm the password");
                confirmation = scanner.next();

                if (password.equals(confirmation)) {
                    passwordHash = (new PasswordUtil()).getHash(password);
                    break;
                }
                System.out.println("The passwords entered are not a match. Please try again.");
            }

            Costumer costumer = new Costumer(firstName, lastName, ssn, username, passwordHash, dobDate);
            bank.AddNewCostumer(costumer);
            return new CostumerScreen(costumer);
        }catch (Exception ex){
            rootLogger.error("INPUT: message: {}", ex.getMessage());
            return new NewCostumerScreen();
        }
    }
}
