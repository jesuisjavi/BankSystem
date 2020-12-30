package com.revature.BankApplication.Screen;

import java.io.Console;

public class BankApplication {

    private static BankSystemScreen currentScreen;

    public static void main(String[] args) {

        currentScreen = new BankScreen();
        while(currentScreen != null) {
            currentScreen = currentScreen.displayScreen();
        }
    }
}