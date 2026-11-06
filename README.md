# Account Ledger

## How to use/download
- --
### Download and setup
1. download the Zip file in [Account Ledger](https://github.com/sageburks31-jpg/Accounting-Ledger-Application) from GitHub and unzip it
2. download and open IntelliJ(if you don't have IntelliJ already)
3. open IntelliJ and click the `Open` option and select the unzipped folder for `Accounting-Ledger-Application` and open it.
4. once opened under `Project` and scroll down till you see `transactions.csv`, open it (Make sure `Text` is selected) and then replace from line 2 down all the template field data with your transaction field data
5. go back to under `Project` go to `src/main/java/com/pluralsight/App.java` and double click and open `App.java`
6. now to the row of IntelliJ next to `Current File` click the run arrow button to start the program.
- --
### How to use
1. when the program is running go and tap your curser where it says `enter Valid command here -> ` and enter one of the listed command options.

- 
  1. if your in the `=== Home ===` menu then the listed commands are
   
       D for `D) Add Deposit`
        -   this is where you will enter future deposits

       P for `P) Make Payment`
        -   this is where you will enter future Payment

       L for `L) Ledger`
        -   this will bring you to the Ledger menu

       X for `X) Exit`
        -   this will End the program


-
  2. if your in the `=== Ledger ===` menu then the listed commands are
   
       A for `A) All`
     -   this will allow you to view all of your provided transaction history

       D for `D) Deposits`
     -   this will allow you to view all of your provided deposit history

     P for `P) Payment`
     -   this will allow you to view all of your provided Payment history

       R for `R) Reports`
     -   this will take you to the Reports menu
 
       H for `H) Home`.
     -   this will take you back to the Home menu


- 
  3. if your in the `=== Reports ===` menu then the listed commands are

       1 for `1) Month To Date`
      -   this will show you all of your provided transaction history done for this month

       2 for `2) Previous Month`
      -   this will show you all of your provided transaction history done for the previous month

       3 for `3) Year To Date`
      -   this will show you all of your provided transaction history done for this year

       4 for `4) Previous Year`
      -   this will show you all of your provided transaction history done for the previous year

       5 for `5) Search by Vendor`
      -   this will allow you to search for a list transactions done with a specific vendor 

       0 for `0) Back`
      -   this will take you back to the Ledger menu


2. how to add Deposit/Make Payment: you first need to be in the `=== Home ===` menu and enter your desired command (D to Add Deposit and P to Make Payment). 

    -- when you entered the command it will prompt you to `enter description`(make sure to enter some kind of descriptive text or will not allow you to move on). 

    -- After you entered the description it will prompt you for `enter vendor`(same as before make sure to enter text, or it won't allow you to move on)

    -- After you entered the vendor it will prompt you to `enter amount`(make that you're just entering the number, because otherwise it will re-request the prompt)

    
- after you filled out all three prompts it will automatically add it to the transaction.csv file


- --
## Interesting code
- --
*Location of Provided method: found in lines 294-322 of Main.java*
```java 
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
```
- --
## Screenshots
- --
#### Shows how the console looks when ran-
Context:

<img alt="Screenshot 2026-11-04 at 9.13.49 AM.png" height="400" src="images/Screenshot%202026-11-04%20at%209.13.49%E2%80%AFAM.png" width="400"/>

*this screenshot shows the main-menu being used to go to the Ledger-Menu. And the ledger-Menu being used to go to the Reports-Menu, with the Report Menu showing an "Error Invalid Command" response*

- --