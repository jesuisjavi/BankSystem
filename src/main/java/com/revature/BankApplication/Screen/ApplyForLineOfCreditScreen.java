package com.revature.BankApplication.Screen;

import com.revature.Account.AccountAction;
import com.revature.Person.Costumer;
import com.revature.Person.Employee;

import java.util.Scanner;

public class ApplyForLineOfCreditScreen implements BankSystemScreen {
    Costumer costumer;
    Employee employee;

    /**+
     *
     * @param costumer Costumer that will be applying for a Line opf Credit Account
     * @param employee Bank employee who will be taking the loan application
     */
    public ApplyForLineOfCreditScreen(Costumer costumer, Employee employee) {
        this.costumer = costumer;
        this.employee = employee;
    }

    /**+
     * Screen used by a costumer to apply for a Line of Credit Account. the requeste can be denied due to poor credit, instantly approved or it might need to be reviewed by the bank employee
     * @return A BankScreen depending on the users selections
     */
    @Override
    public BankSystemScreen displayScreen() {
        System.out.println("Hello, my name is " + employee.getFirstName() + " " + employee.getLastName() + " and I will be your banker today.");

        try{
            Scanner scanner = new Scanner(System.in);
            double amount = -1;
            while (amount < 500) {
                System.out.println("How much do you want your Line of Credit to be?");
                amount = scanner.nextDouble();
                if (amount < 500)
                    System.out.println("The amount needs to be at least $500");
            }
            AccountAction result = bank.LoanApprovalTool(costumer);
            if (!result.wasSuccessful()) {
                System.out.println("Your request has been denied due to " + result.getMessage());
                return new CostumerScreen(costumer);
            }
            else if (result.getMessage().equals("Review Needed")){
                System.out.println("How much is your yearly salary?");
                double salary = scanner.nextDouble();
                System.out.println("How much debt do you have?");
                double debt = scanner.nextDouble();
                AccountAction decision = employee.ProcessCreditApplication(costumer, salary, debt);
                if (!decision.wasSuccessful()) {
                    System.out.println("Your request has been denied due to " + decision.getMessage());
                    return new CostumerScreen(costumer);
                }
                else
                    result = decision;
            }
            AccountAction account = bank.OpenCreditLineAccount(costumer, amount, Double.parseDouble(result.getMessage()));
            System.out.println("Your request has been approved!");
            System.out.println("The account number is " + account.getMessage());
            System.out.println("The interest rate is " + result.getMessage() + "%");
            return new CostumerScreen(costumer);
        }catch (Exception ex){
            System.out.println("Something went wrong. Let's try again...");
            rootLogger.error("INPUT: message: {} username: {}", ex.getMessage(), costumer.getUserName());
            return displayScreen();
        }
    }
}
