package com.pluralsight;

import java.io.*;
import java.util.*;

public class Main {

    static Scanner scanner = new Scanner(System.in);
    static List<transaction> transactionList = new ArrayList<>();
    static String testReadWriteFile = "test-writer.csv";
    static String transactionReadWriteFile = "transactions.csv";

    private static void print(String text) {System.out.println(text);}
    private static void print(String text, boolean nextLine) {
        if (nextLine) {System.out.println(text);}
        else {System.out.print(text);}
    }

    public static void readFile(){}
    public static void writeFile(){}

    public static void main(String[] args) {
        runMainMenu();
    }
    public static void runMainMenu() {
        String enteredCommand = "";
        print("""
                === Main Account Menu ===
                (D) Add Deposit
                (P) Make Payment
                (L) Ledger
                (X) Exit""");
        do {
            print("enter Valid command here -> ",false);
            enteredCommand=scanner.nextLine();

        } while (enteredCommand.isEmpty());
    }
    private static void addDeposit() {

    }
    private static void MakePayment() {

    }
    private static void runLedgerMenu() {

    }
    private static void displayAllDeposits() {

    }
    private static void displayAllPayments() {

    }
    private static void displayAllReports() {

    }
    private static void monthToDate() {

    }
    private static void previousMonth() {

    }
    private static void yearToDate() {

    }
    private static void PreviousYear() {

    }
    private static void searchByVendor() {

    }

}