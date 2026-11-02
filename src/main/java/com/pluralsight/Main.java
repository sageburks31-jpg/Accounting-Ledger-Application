package com.pluralsight;

import java.util.*;

public class Main {

    static Scanner scanner = new Scanner(System.in);
    static List<Transaction> transactionList = new ArrayList<>();
    static String testReadWriteFile = "test-writer.csv";
    static String transactionReadWriteFile = "transactions.csv";

    private static void print(String text) {System.out.println(text);}
    private static void print(String text, boolean pushNextLine) {
        if (pushNextLine) {System.out.println(text);}
        else {System.out.print(text);}
    }

    public static void readFile() {}
    public static void writeFile() {}

    public static void main(String[] args) {
        readFile();
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
            enteredCommand=scanner.nextLine().toLowerCase().trim();
            switch (enteredCommand){
                case "d" -> addDeposit();
                case "p" -> makePayment();
                case "l" -> runLedgerMenu();
                default -> {
                    if (!enteredCommand.equals("x")) {
                        print("Error! Invalid command found. Please enter a Valid command");
                    } else {
                        enteredCommand="";
                        print("thank you for using our service");
                    }
                }
            }
        } while (!enteredCommand.isEmpty());
    }

    private static void runLedgerMenu() {}

    private static void makePayment() {}

    private static void addDeposit() {
        String enteredCommand = "";
        String[] commandList = {"enter description", "enter vendor", "enter amount"};

        boolean depositIsComplete = true;
        Transaction transaction;
        transaction = new Transaction();
        for (String command : commandList) {
            print(String.format("""
                    %s
                    enter (C) to cancel and return to main
                    enter here ->"""+" ",command), false);
            enteredCommand = scanner.nextLine().trim();
            String cancelCommand=enteredCommand.toLowerCase();
            if (!cancelCommand.equals("c")) {
                switch (command) {
                    case "enter description" -> {
                        transaction.setDescription(enteredCommand);
                    }
                    case "enter vendor" -> {
                        transaction.setVendor(enteredCommand);
                    }
                    case "enter amount" -> {
                        transaction.setAmount(enteredCommand);
                    }
                }
            } else {
                depositIsComplete = false;
                runMainMenu();
                break;
            }
        }

        if (depositIsComplete) {
            transactionList.add(transaction);
            print("press ENTER to return to main", false);
            scanner.nextLine();
            runMainMenu();
        }

    }

    private static void displayAllDeposits() {}

    private static void displayAllPayments() {}

    private static void displayAllReports() {}

    private static void monthToDate() {}

    private static void previousMonth() {}

    private static void yearToDate() {}

    private static void PreviousYear() {}

    private static void searchByVendor() {}

}