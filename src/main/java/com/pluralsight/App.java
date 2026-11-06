package com.pluralsight;

import java.util.*;
import java.time.*;
import java.time.format.*;
import java.io.*;

public class App {
    static Scanner scanner = new Scanner(System.in);
    static List<Transaction> transactionList = new ArrayList<>();
    //static String testReadWriteFile = "test-writer.csv";

    static String transactionReadWriteFile = "transactions.csv";
    static DateTimeFormatter formatYear = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    static DateTimeFormatter formatTime = DateTimeFormatter.ofPattern("HH:mm:ss");

    // helper print methods
    private static void print(String text) {System.out.println(text);}
    private static void print(String text, boolean pushNextLine) {
        if (pushNextLine) {System.out.println(text);}
        else {System.out.print(text);}
    }

    // read/write File methods
    public static void readFile() {
        try {
            FileReader fileReader = new FileReader(transactionReadWriteFile);
            BufferedReader bufReader = new BufferedReader(fileReader);
            String input;

            while((input = bufReader.readLine()) != null) {
                // print(input);
                if (!input.equals("date|time|description|vendor|amount")) {
                    String[] lineSegment = input.split("\\|");
                    LocalDate localDate = LocalDate.parse(lineSegment[0]);
                    LocalTime localTime = LocalTime.parse(lineSegment[1]);
                    Transaction transaction = new Transaction(localDate, localTime, lineSegment[2], lineSegment[3], Double.parseDouble(lineSegment[4]));
                    transactionList.add(transaction);
                }
            }
            bufReader.close();
        } catch(IOException e) {print(String.valueOf(e));}
    }
    public static void writeFile() {
        try {
            FileWriter fileWriter = new FileWriter(transactionReadWriteFile, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.append(transactionList.get(transactionList.size()-1).toRow());

            bufferedWriter.newLine();
            bufferedWriter.close();
        } catch(IOException e) {print(String.valueOf(e));}
    }

    // main/runMenu methods
    public static void main(String[] args) {
        readFile();
        runMainMenu();
        scanner.close();
    }

    public static void runMainMenu() {
        String enteredCommand;
        do {
            print("""
                
                === Home ===
                D) Add Deposit
                P) Make Payment
                L) Ledger
                X) Exit""");
            print("enter Valid command here -> ",false);
            enteredCommand=scanner.nextLine().toLowerCase().trim();

            switch (enteredCommand){
                case "d" -> addDeposit();
                case "p" -> makePayment();
                case "l" -> runLedgerMenu();
                case "x" -> print("thank you for using our service");
                default -> print("Error! Invalid command found. Please enter a Valid command");
            }
        } while (!enteredCommand.equals("x"));
    }
    private static void runLedgerMenu() {
        String enteredCommand;
        do {
            print("""
                
                === Ledger ===
                A) All
                D) Deposits
                P) Payments
                R) Reports
                H) Home""");
            print("enter Valid command here -> ",false);
            enteredCommand=scanner.nextLine().toLowerCase().trim();

            switch (enteredCommand){
                case "d" -> displayAllDeposits();
                case "p" -> displayAllPayments();
                case "a" -> displayAllReports();
                case "r" -> runReportsMenu();
                default -> {
                    if (!enteredCommand.equals("h")) {
                        enteredCommand="";
                        print("Error! Invalid command found. Please enter a Valid command");
                    }
                }
            }
        } while (!enteredCommand.equals("h"));
    }
    private static void runReportsMenu() {
        String enteredCommand;
        do {
            print("""
                
                === Reports ===
                1) Month To Date
                2) Previous Month
                3) Year To Date
                4) Previous Year
                5) Search by Vendor
                0) Back""");
            print("enter Valid command here -> ",false);
            enteredCommand=scanner.nextLine().toLowerCase().trim();

            switch (enteredCommand){
                case "1" -> displayMonthToDate();
                case "2" -> displayItemsFromPreviousMonth();
                case "3" -> displayYearToDate();
                case "4" -> displayItemsFromPreviousYear();
                case "5" -> displayVendorNamedItems();
                default -> {
                    if (!enteredCommand.equals("0")) {
                        enteredCommand="";
                        print("Error! Invalid command found. Please enter a Valid command");
                    }
                }
            }
        } while (!enteredCommand.equals("0"));
    }

    // Payment/Deposit methods
    private static void makePayment() {
        String[] commandList = {"enter description", "enter vendor", "enter amount"};
        Transaction transaction;
        transaction = new Transaction();

        for (String command : commandList) {
            switch (command) {
                case "enter description" -> transaction.setDescription(promptToString(command + "-> "));
                case "enter vendor" -> transaction.setVendor(promptToString(command + "-> "));
                case "enter amount" -> transaction.setAmount(promptToDouble(command + "-> ") * -1);
            }
        }
        addToTransactionList(transaction);
    }
    private static void addDeposit() {
        String[] commandList = {"enter description", "enter vendor", "enter amount"};
        Transaction transaction;
        transaction = new Transaction();

        for (String command : commandList) {
            print(command);
            switch (command) {
                case "enter description" -> transaction.setDescription(promptToString(command+"-> "));
                case "enter vendor" -> transaction.setVendor(promptToString(command+"-> "));
                case "enter amount" -> transaction.setAmount(promptToDouble(command+"-> "));
            }
        }
        addToTransactionList(transaction);
    }

    private static void displayAllDeposits() {
        boolean hasTransaction = false;

        for (Transaction transaction: transactionList) {
            if (transaction.getAmount()>0) {
                hasTransaction=true;
                print(transaction.toRow());
            }
        }
        if (!hasTransaction) print("No transactions found");
        returnToMain();
    }
    private static void displayAllPayments() {
        boolean hasTransaction = false;

        for (Transaction transaction: transactionList) {
            if (transaction.getAmount()<0) {
                hasTransaction=true;
                print(transaction.toRow());
            }
        }
        if (!hasTransaction) print("No transactions found");
        returnToMain();
    }

    // display date methods
    private static void displayMonthToDate() {
        LocalDateTime today = LocalDateTime.now();
        String[] currentDateList = today.format(formatYear).split("-");

        int thisYear = Integer.parseInt(currentDateList[0]);
        int thisMouth = Integer.parseInt(currentDateList[1]);
        boolean itemsInYear = false;

        for (Transaction transaction: transactionList) {
            String[] dateList = transaction.getDate().split("-");
            int transactionMouth = Integer.parseInt(dateList[1]);
            int transactionYear = Integer.parseInt(dateList[0]);

            if (transactionYear == thisYear) {
                if (transactionMouth == thisMouth) {itemsInYear = isItemsInYear(transaction);}
            }
        }
        if (!itemsInYear) {print("No transactions found.");}
        returnToMain();
    }
    private static void displayItemsFromPreviousMonth() {
        String[] monthNames = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        LocalDateTime today = LocalDateTime.now();

        String[] currentDateList = today.format(formatYear).split("-");
        int thisYear = Integer.parseInt(currentDateList[0]);
        int thisMonth = Integer.parseInt(currentDateList[1]);

        boolean itemsInYear = false;
        int lastMonth = thisMonth - 1;
        if (lastMonth == 0) {thisYear -= 1; lastMonth = 12;}

        for (Transaction transaction: transactionList) {
            String[] dateList = transaction.getDate().split("-");
            int transactionYear = Integer.parseInt(dateList[0]);
            int transactionMonth = Integer.parseInt(dateList[1]);

            if (transactionYear == thisYear) {
                if ((lastMonth == transactionMonth)) {itemsInYear = isItemsInYear(transaction);}
            }
        }
        if (!itemsInYear) {print("No transactions found.");}
        print("current month is " + monthNames[thisMonth-1] + " " + thisMonth);
        returnToMain();
    }
    private static void displayYearToDate() {
        LocalDateTime today = LocalDateTime.now();
        String[] currentDateList = today.format(formatYear).split("-");

        int thisYear = Integer.parseInt(currentDateList[0]);
        boolean itemsInYear = false;

        for (Transaction transaction: transactionList) {
            String[] transactionDate = transaction.getDate().split("-");
            int transactionYear = Integer.parseInt(transactionDate[0]);

            if (thisYear == transactionYear) {itemsInYear = isItemsInYear(transaction);}
        }
        if (!itemsInYear) {print("No transactions found.");}
        returnToMain();
    }
    private static void displayItemsFromPreviousYear() {
        LocalDateTime today = LocalDateTime.now();
        String[] currentDateList = today.format(formatYear).split("-");

        boolean itemsInYear=false;
        int thisYear = Integer.parseInt(currentDateList[0]);
        int lastYear = thisYear - 1;

        for (Transaction transaction: transactionList) {
            String[] transactionDate = transaction.getDate().split("-");
            int transactionYear = Integer.parseInt(transactionDate[0]);
            if (lastYear == transactionYear) {itemsInYear = isItemsInYear(transaction);}
        }
        if (!itemsInYear) {print("No transactions found.");}
        print("",true);
        returnToMain();
    }

    // helper methods
    private static double promptToDouble(String prompt) {
        while (true) {
            print(prompt,false);
            String stringOfAmount = scanner.nextLine().trim();
            try {
                if (Double.parseDouble(stringOfAmount) == 0 || stringOfAmount.equals("Infinity") || stringOfAmount.equals("NaN")) {
                    stringOfAmount = "invalid"; print("0 is " + stringOfAmount);
                }
                else {return doubleToPositive(Double.parseDouble(stringOfAmount));}
            } catch (NumberFormatException e) {
                print("Error invalid command! Please enter a valid command");
            }
        }
    }
    private static String promptToString(String prompt) {
        while (true) {
            print(prompt,false);
            boolean hasPipe = false;
            String enteredCommand=scanner.nextLine().trim();

            try {
                if (enteredCommand.isEmpty()) {
                    int integerOfCommand = Integer.parseInt(enteredCommand);
                    print(String.valueOf(integerOfCommand),false);
                }
                String[] splitString = enteredCommand.split("");
                StringBuilder enteredCommandBuilder = new StringBuilder();

                for (String character : splitString) {
                    if (character.equals("|")) {character = "/"; hasPipe = true;}
                    enteredCommandBuilder.append(character);
                }
                enteredCommand = enteredCommandBuilder.toString();

                if (hasPipe) {
                    print("character(s) found to be in conflict with .csv format. '|' is overwritten with '/'");
                }
                return enteredCommand;
            } catch (NumberFormatException e) {
                print("Error invalid command! Please enter a valid command");
            }
        }
    }

    private static double doubleToPositive(double amount) {
        while (true) {
            if (amount < 0) {amount *= -1;}
            else if (amount > 0) {return amount;}
        }
    }
    private static boolean isItemsInYear(Transaction transaction) {
        print(transaction.toRow()); return true;
    }

    // Misc methods
    private static void addToTransactionList(Transaction transaction) {
        LocalTime localTime = LocalTime.now();
        LocalDate localDate = LocalDate.now();

        transaction.setDate(localDate);
        transaction.setTime(LocalTime.parse(localTime.format(formatTime)));
        transactionList.add(transaction);

        writeFile();
        returnToMain();
    }
    private static void returnToMain() {
        print("\n press ENTER to return to main", false);
        scanner.nextLine();
    }

    // Misc display methods
    private static void displayAllReports() {
        if (transactionList.isEmpty()) print("No transactions found");
        transactionList.forEach(transaction -> print(transaction.toRow()));
        returnToMain();
    }
    private static void displayVendorNamedItems() {
        print("Please enter name of vendor -> ",false);
        String enteredCommand = scanner.nextLine().toLowerCase().trim();
        boolean hasTransaction = false;

        for (int i = 0; i <= transactionList.size()-1; i++) {
            String vendor = transactionList.get(i).getVendor().toLowerCase().trim();
            hasTransaction = true;
            if (vendor.equals(enteredCommand)) {print(transactionList.get(i).toRow());}
        }
        if (!hasTransaction) print("No transactions found");
        returnToMain();
    }
}