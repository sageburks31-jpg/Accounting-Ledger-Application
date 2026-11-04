package com.pluralsight;

import java.util.*;
import java.time.*;
import java.time.format.*;
import java.io.*;

public class Main {

    static Scanner scanner = new Scanner(System.in);
    static List<Transaction> transactionList = new ArrayList<>();
    //static String testReadWriteFile = "test-writer.csv";
    static String transactionReadWriteFile = "transactions.csv";
    static DateTimeFormatter formatYear = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    static DateTimeFormatter formatHours = DateTimeFormatter.ofPattern("HH:mm:ss");

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
                    Transaction product1 = new Transaction(localDate, lineSegment[1], lineSegment[2], lineSegment[3], Double.parseDouble(lineSegment[4]));
                    transactionList.add(product1);
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
                
                === Main Account Menu ===
                (D) Add Deposit
                (P) Make Payment
                (L) Ledger
                (X) Exit""");
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
                
                === Ledger Menu ===
                (A) All
                (D) Deposits
                (P) Payments
                (R) Report
                (H) Home""");
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
                
                === Reports Menu ===
                (1) Month To Date
                (2) Previous Month
                (3) Year To Date
                (4) Previous Year
                (5) Search by Vendor
                (0) back""");
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
                case "enter description" -> transaction.setDescription(stringToFormatString(command + "-> "));
                case "enter vendor" -> transaction.setVendor(stringToFormatString(command + "-> "));
                case "enter amount" -> transaction.setAmount(stringToDouble(command + "-> ") * -1);
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
                case "enter description" -> transaction.setDescription(stringToFormatString(command+"-> "));
                case "enter vendor" -> transaction.setVendor(stringToFormatString(command+"-> "));
                case "enter amount" -> transaction.setAmount(stringToDouble(command+"-> "));
            }
        }
        addToTransactionList(transaction);
    }

    private static void displayAllDeposits() {
        for (Transaction transaction: transactionList) {
            if (transaction.getAmount()>0) {
                print(transaction.toRow());
            }
        }
        returnToMain();
    }
    private static void displayAllPayments() {
        for (Transaction transaction: transactionList) {
            if (transaction.getAmount()<0) {
                print(transaction.toRow());
            }
        }
        returnToMain();
    }

    // display date methods
    private static void displayMonthToDate() {
        print("""
                == key of mouths ==
                 Jan 1 | Feb 2
                 Mar 3 | Apr 4
                 May 5 | Jun 6
                 Jul 7 | Aug 8
                 Sep 9 | Oct 10
                Nov 11 | Dec 12""");
        LocalDateTime today = LocalDateTime.now();
        String[] currentDateList = today.format(formatYear).split("-");

        int thisYear = Integer.parseInt(currentDateList[0]);
        int thisMouth = Integer.parseInt(currentDateList[1]);
        int enterDate=stringToInteger("please enter date of month here -> ");

        boolean itemsInYear = false;

        for (Transaction transaction: transactionList) {
            String[] dateList = transaction.getDate().split("-");
            int transactionMouth = Integer.parseInt(dateList[1]);
            int transactionYear = Integer.parseInt(dateList[0]);

            if (transactionYear == thisYear) {
                if (enterDate <= thisMouth) {
                    if (transactionMouth == enterDate)
                    {itemsInYear = isItemsInYear(transaction);}
                    else if (enterDate < transactionMouth)
                    {itemsInYear = isItemsInYear(transaction);}
                }
            }
        }
        if (!itemsInYear) {print("No transactions found.");}
        returnToMain();
    }
    private static void displayItemsFromPreviousMonth() {
        String[] mouthNames = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        LocalDateTime today = LocalDateTime.now();

        String[] currentDateList = today.format(formatYear).split("-");
        int thisYear = Integer.parseInt(currentDateList[0]);
        int thisMouth = Integer.parseInt(currentDateList[1]);

        boolean itemsInYear = false;
        int lastMouth = thisMouth-1;
        if (lastMouth == 0) {lastMouth = 12;}

        for (Transaction transaction: transactionList) {
            String[] dateList = transaction.getDate().split("-");
            int transactionYear = Integer.parseInt(dateList[0]);
            int transactionMonth = Integer.parseInt(dateList[1]);

            if (transactionYear == thisYear) {
                if ((lastMouth == transactionMonth)) {itemsInYear = isItemsInYear(transaction);}
            }
        }
        if (!itemsInYear) {print("No transactions found.");}
        print("current month is " + mouthNames[thisMouth-1] + " " + thisMouth);
        returnToMain();
    }

    private static void displayYearToDate() {
        LocalDateTime today = LocalDateTime.now();
        String[] currentDateList = today.format(formatYear).split("-");

        int thisYear = Integer.parseInt(currentDateList[0]);
        int enterDate;
        boolean itemsInYear = false;

        do {
            enterDate = stringToInteger("please enter date of Year here -> ");
            if (enterDate < 1000) {print("Please enter a proper date.");}
        }while (enterDate < 1000);

        for (Transaction transaction: transactionList) {
            String[] transactionDate = transaction.getDate().split("-");
            int transactionYear = Integer.parseInt(transactionDate[0]);

            if (enterDate == transactionYear) {itemsInYear = isItemsInYear(transaction);}
            else if (enterDate < thisYear) {itemsInYear = isItemsInYear(transaction);}
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
    private static double stringToDouble(String prompt) {
        while (true) {
            print(prompt,false);
            String stringOfAmount = scanner.nextLine().trim();
            try {
                double amount = doubleToPositive(Double.parseDouble(stringOfAmount));
                if (amount == 0) {
                    int integerOfCommand = Integer.parseInt(stringOfAmount);
                    print(String.valueOf(integerOfCommand),false);
                }
                return amount;
            } catch (NumberFormatException e) {
                print("Error invalid command! Please enter a valid command");
            }
        }
    }
    private static int stringToInteger(String prompt) {
        while (true) {
            print(prompt,false);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                print("Error invalid command! Please enter a valid command");
            }
        }
    }
    private static String stringToFormatString(String prompt) {
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
        LocalTime today = LocalTime.now();
        LocalDate localDate = LocalDate.now();
        transaction.setDate(localDate);
        transaction.setTime(today.format(formatHours));

        transactionList.add(transaction);
        writeFile(); returnToMain();
    }
    private static void returnToMain() {
        print("\n press ENTER to return to main", false);
        scanner.nextLine();
    }

    // Misc display methods
    private static void displayAllReports() {
        transactionList.forEach(transaction -> print(transaction.toRow()));
        returnToMain();
    }
    private static void displayVendorNamedItems() {
        print("Please enter name of vendor -> ",false);
        String enteredCommand = scanner.nextLine().toLowerCase().trim();

        for (int i = 0; i <= transactionList.size()-1; i++) {
            String vendor = transactionList.get(i).getVendor().toLowerCase().trim();
            if (vendor.equals(enteredCommand)) {print(transactionList.get(i).toRow());}
        }
        returnToMain();
    }

}