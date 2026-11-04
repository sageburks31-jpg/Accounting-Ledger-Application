# Account Ledger

## How to use/download
- --
### Download and setup
1. download the Zip file in [Account Ledger](https://github.com/sageburks31-jpg/Accounting-Ledger-Application) from GitHub
2. download and open IntelliJ(if you don't have IntelliJ already nor have any other Java coding application)
3. 
- --
### Use
1. 
2. 
3. 
- --
## Interesting code
- --
*Location of Provided method: found in lines 315-343 of Main.java*
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
#### Explanation of the Code shown above- 

- v 
- v
- v
- --
## Screenshots
- --
#### Shows how the console looks when ran-
<img height="547" src="/Users/treeman/Documents/intel_files/Accounting-Ledger-Application/Screenshot 2026-11-04 at 9.13.49 AM.png" width="452" alt=""/>
Context:
*this screenshot shows the main-menu being used to go to the Ledger-Menu. And the ledger-Menu being used to go to the Reports-Menu, with the Report Menu showing an "Error Invalid Command" response*

- --