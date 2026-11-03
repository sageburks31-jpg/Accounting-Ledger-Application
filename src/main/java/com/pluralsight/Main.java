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
            for (int i = 0; i <= transactionList.size()-1; i++) {
                bufferedWriter.append(transactionList.get(i).toRow());
                bufferedWriter.newLine();
            }
            bufferedWriter.close();
        } catch(IOException e) {print(String.valueOf(e));}
    }

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
                case "x" -> {
                    print("thank you for using our service");
                    writeFile();
                }
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
                case "1" -> monthToDate();
                case "2" -> previousMonth();
                case "3" -> yearToDate();
                case "4" -> PreviousYear();
                case "5" -> searchByVendor();
                default -> {
                    if (!enteredCommand.equals("0")) {
                        enteredCommand="";
                        print("Error! Invalid command found. Please enter a Valid command");
                    }
                }
            }
        } while (!enteredCommand.equals("0"));
    }

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
        extracted(transaction);
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
        extracted(transaction);
    }

    private static void extracted(Transaction transaction) {
        LocalDateTime today = LocalDateTime.now();
        transaction.setDate(today.format(formatYear));
        transaction.setTime(today.format(formatHours));

        transactionList.add(transaction);
        writeFile();
        returnToMain();
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
    }

    private static void displayAllPayments() {
        for (Transaction transaction: transactionList) {
            if (transaction.getAmount()<0) {
                print(transaction.toRow());
            }
        }
        returnToMain();
    }

    private static void displayAllReports() {
        transactionList.forEach(transaction -> print(transaction.toRow()));
        returnToMain();
    }

    private static void monthToDate() {
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
        int enterDate=stringToInteger("please enter date search mouth here -> ");

        for (Transaction transaction: transactionList) {
            String[] dateList = transaction.getDate().split("-");
            int dateOfMouth = Integer.parseInt(dateList[1]);

            int dateOfYear = Integer.parseInt(dateList[0]);
            if (dateOfYear == thisYear) {
                if (dateOfMouth >= enterDate || dateOfMouth <= thisMouth) {
                    print(transaction.toRow());
                }
            }
        }
        returnToMain();
    }

    private static void previousMonth() {
        String[] mouthNames = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        LocalDateTime today = LocalDateTime.now();

        String[] currentDateList = today.format(formatYear).split("-");
        int thisYear = Integer.parseInt(currentDateList[0]);
        int thisMouth = Integer.parseInt(currentDateList[1]);

        int dateOfMouth = thisMouth-1;
        if (dateOfMouth == 0) {dateOfMouth=12;}
        for (Transaction transaction: transactionList) {
            String[] dateList = transaction.getDate().split("-");

            int dateOfYear = Integer.parseInt(dateList[0]);
            if (dateOfYear == thisYear) {
                if (!(dateOfMouth > thisMouth)) print(transaction.toRow());
            }
        }
        print("current month" + mouthNames[thisMouth-1] + " " + thisMouth);
        returnToMain();
    }

    private static void yearToDate() {
        LocalDateTime today = LocalDateTime.now();
        String[] currentDateList = today.format(formatYear).split("-");

        int thisYear = Integer.parseInt(currentDateList[0]);
        int enterDate=stringToInteger("please enter date search Year here -> ");

        for (Transaction transaction: transactionList) {
            if (enterDate <= thisYear) {
                print(transaction.toRow());
            }
        }
        returnToMain();
    }

    public static double stringToDouble(String prompt) {
        while (true) {
            print(prompt,false);
            try {
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                print("Error invalid command! Please enter a valid command");
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
            }
        }
    }
    public static String stringToFormatString(String prompt) {
        while (true) {
            print(prompt,false);
            boolean hasPipe = false;

            String enteredCommand=scanner.nextLine().trim();
            try {
                if (enteredCommand.isEmpty()) {
                    int integerOfCommand = Integer.parseInt(enteredCommand);
                }
                String[] splitString = enteredCommand.split("");
                StringBuilder enteredCommandBuilder = new StringBuilder();
                for (String character : splitString) {
                    if (character.equals("|")) {
                            character = "/";
                            hasPipe = true;
                    }
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

    private static void PreviousYear() {
        LocalDateTime today = LocalDateTime.now();
        String[] currentDateList = today.format(formatYear).split("-");

        int thisYear = Integer.parseInt(currentDateList[0]);
        int lastYear = thisYear - 1;

        for (Transaction transaction: transactionList) {
            String[] transactionDate = transaction.getDate().split("-");
            int transactionYear = Integer.parseInt(transactionDate[0]);
            if (lastYear == transactionYear) {
                print(transaction.toRow());
            }
        }
        print("\n",true);
        returnToMain();
    }

    private static void searchByVendor() {}

}