package com.pluralsight;

import java.util.*;
import java.time.*;
import java.time.format.*;
import java.io.*;

public class Main {

    static Scanner scanner = new Scanner(System.in);
    static List<Transaction> transactionList = new ArrayList<>();
    static String testReadWriteFile = "test-writer.csv";
    static String transactionReadWriteFile = "transactions.csv";
    static DateTimeFormatter formatYear = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    static DateTimeFormatter formatHours = DateTimeFormatter.ofPattern("HH:mm:ss");

    private static void print(String text) {System.out.println(text);}
    private static void print(String text, boolean pushNextLine) {
        if (pushNextLine) {System.out.println(text);}
        else {System.out.print(text);}
    }

    public static void readFile() {
        try {
            FileReader fileReader = new FileReader(testReadWriteFile);
            BufferedReader bufReader = new BufferedReader(fileReader);
            String input;
            while((input = bufReader.readLine()) != null) {
                print(input);
                if (!input.equals("date|time|description|vendor|amount")) {
                    String[] lineSegment = input.split("\\|");
                    Transaction product1 = new Transaction(lineSegment[0], lineSegment[1], lineSegment[2], lineSegment[3], Double.parseDouble(lineSegment[4]));
                    transactionList.add(product1);
                }
            }
            bufReader.close();
        } catch(IOException e) {print(String.valueOf(e));}
    }

    public static void writeFile() {
        try {
            FileWriter fileWriter = new FileWriter(testReadWriteFile);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.append("date|time|description|vendor|amount");
            bufferedWriter.newLine();
            for (int i = 0; i <= transactionList.size() - 1; i++) {
                bufferedWriter.write(transactionList.get(i).toRow());
                bufferedWriter.newLine();
            }
            bufferedWriter.close();
        } catch(IOException e) {print(String.valueOf(e));}
    }

    public static void main(String[] args) {
        readFile();
        runMainMenu();
    }

    public static void runMainMenu() {
        String enteredCommand;
        print("""
                
                === Main Account Menu ===
                (D) Add Deposit
                (P) Make Payment
                (L) go to Ledger
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
                        enteredCommand="";
                        print("Error! Invalid command found. Please enter a Valid command");
                    } else {
                        print("thank you for using our service");
                        writeFile();
                    }
                }
            }
        } while (enteredCommand.isEmpty());
    }

    private static void runLedgerMenu() {
        String enteredCommand;
        print("""
                
                === Ledger Menu ===
                (D) to see All Deposits
                (P) to see All Payments
                (A) to see All Reports
                (R) to see more Report options
                (x) to return to main""");
        do {
            print("enter Valid command here -> ",false);
            enteredCommand=scanner.nextLine().toLowerCase().trim();
            switch (enteredCommand){
                case "d" -> displayAllDeposits();
                case "p" -> displayAllPayments();
                case "a" -> displayAllReports();
                case "r" -> runReportsMenu();
                default -> {
                    if (!enteredCommand.equals("x")) {
                        enteredCommand="";
                        print("Error! Invalid command found. Please enter a Valid command");
                    } else {runMainMenu();}
                }
            }
        } while (enteredCommand.isEmpty());
    }

    private static void runReportsMenu() {
        String enteredCommand;
        print("""
                
                === Reports Menu ===
                (1) Month To Date
                (2) Previous Month
                (3) Year To Date
                (4) Previous Year
                (5) Search by Vendor
                (x) to return to Ledger""");
        do {
            print("enter Valid command here -> ",false);
            enteredCommand=scanner.nextLine().toLowerCase().trim();
            switch (enteredCommand){
                case "1" -> monthToDate();
                case "2" -> previousMonth();
                case "3" -> yearToDate();
                case "4" -> PreviousYear();
                case "5" -> searchByVendor();
                default -> {
                    if (!enteredCommand.equals("x")) {
                        enteredCommand="";
                        print("Error! Invalid command found. Please enter a Valid command");
                    } else {runLedgerMenu();}
                }
            }
        } while (enteredCommand.isEmpty());

    }

    private static void makePayment() {
        String enteredCommand = "";
        String[] commandList = {"enter description", "enter vendor", "enter amount"};

        boolean depositIsComplete = true;
        Transaction transaction;
        transaction = new Transaction();

        for (String command : commandList) {
            if (!command.equals("enter amount")) {
                print(String.format("""
                        %s
                        enter C to cancel and return to main menu
                        enter here ->""" + " ", command), false);
                enteredCommand = scanner.nextLine().trim();
            }
            String cancelCommand = enteredCommand.toLowerCase();
            if (!cancelCommand.equals("c")) {
                switch (command) {
                    case "enter description" -> transaction.setDescription(enteredCommand);
                    case "enter vendor" -> transaction.setVendor(enteredCommand);
                    case "enter amount" -> transaction.setAmount(stringToDouble(command+"-> ")*-1);
                }
            } else {
                depositIsComplete = false;
                runMainMenu();
                break;
            }
        }
        if (depositIsComplete) extracted(transaction);
    }

    private static void addDeposit() {
        String enteredCommand = "";
        String[] commandList = {"enter description", "enter vendor", "enter amount"};

        boolean depositIsComplete = true;
        Transaction transaction;
        transaction = new Transaction();

        for (String command : commandList) {
            if (!command.equals("enter amount")) {
                print(String.format("""
                        %s
                        enter C to cancel and return to main menu
                        enter here ->""" + " ", command), false);
                enteredCommand = scanner.nextLine().trim();
            }
            String cancelCommand=enteredCommand.toLowerCase();
            if (!cancelCommand.equals("c")) {
                switch (command) {
                    case "enter description" -> transaction.setDescription(enteredCommand);
                    case "enter vendor" -> transaction.setVendor(enteredCommand);
                    case "enter amount" -> transaction.setAmount(stringToDouble(command+"-> "));
                }
            } else {
                depositIsComplete = false;
                runMainMenu();
                break;
            }
        }
        if (depositIsComplete) {extracted(transaction);}
    }

    private static void extracted(Transaction transaction) {
        LocalDateTime today = LocalDateTime.now();
        transaction.setDate(today.format(formatYear));
        transaction.setTime(today.format(formatHours));

        transactionList.add(transaction);
        writeFile();
        returnToMain();
        runMainMenu();
    }

    private static void returnToMain() {
        print("press ENTER to return to main", false);
        scanner.nextLine();
    }

    private static void displayAllDeposits() {
        for (Transaction transaction: transactionList) {
            if (transaction.getAmount()>0) {
                print(transaction.toRow());
            }
        }
        returnToMain();
        runLedgerMenu();
    }

    private static void displayAllPayments() {
        for (Transaction transaction: transactionList) {
            if (transaction.getAmount()<0) {
                print(transaction.toRow());
            }
        }
        returnToMain();
        runLedgerMenu();
    }

    private static void displayAllReports() {
        transactionList.forEach(transaction -> print(transaction.toRow()));
        returnToMain();
        runLedgerMenu();
    }

    private static void monthToDate() {
        print("""
                == Mouth Key ==
                 Jan 1 | Feb 2
                 Mar 3 | Apr 4
                 May 5 | Jun 6
                 Jul 7 | Aug 8
                 Sep 9 | Oct 10
                Nov 11 | Dec 12""");
        LocalDateTime today = LocalDateTime.now();
        String[] currentDateList = today.format(formatYear).split("-");
        int currentMouth = Integer.parseInt(currentDateList[1]);
        int enterDate=stringToInteger("please enter date search mouth here -> ");
        for (Transaction transaction: transactionList) {
            String[] dateList = transaction.date.split("-");
            int dateOfMouth = Integer.parseInt(dateList[1]);
            if (dateOfMouth >= enterDate || dateOfMouth <= currentMouth) {
                print(transaction.toRow());
            }
        }
    }

    private static void previousMonth() {}

    private static void yearToDate() {}

    public static double stringToDouble(String prompt) {
        while (true) {
            print(prompt,false);
            try {
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                print("Error invalid command! Please enter a valid command");
                stringToDouble(prompt);
            }
        }
    }
    public static int stringToInteger(String prompt) {
        while (true) {
            print(prompt,false);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                print("Error invalid command! Please enter a valid command");
                stringToDouble(prompt);
            }
        }
    }

    private static void PreviousYear() {}

    private static void searchByVendor() {}

}