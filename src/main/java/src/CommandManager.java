package src;

import src.commands.*;
import src.interfaces.CollectionCustom;
import src.interfaces.Command;
import src.models.Product;
import src.interfaces.CollectionCustom;
import src.interfaces.Command;
import src.models.Product;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class CommandManager {

    private CollectionCustom<Product> collectionManager = null;

    private HashMap<String, Command> commandsMap;

    private LinkedList<String> commandHistory;

    private MessageHandler messageHandler;
    private HashSet<String> scriptsBeingExecuted;

    /**
     * Constructor for making a CommandManager
     * @param manager - collection with objects to manipulate
     * @param messageHandler - service for message handling
     */
    public CommandManager(CollectionCustom<Product> manager, MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
        this.collectionManager = manager;
        scriptsBeingExecuted = new HashSet<>();
        commandHistory = new LinkedList<>();
        commandsMap = new HashMap<>();
        commandsMap.put("add", new AddCommand(collectionManager));
        commandsMap.put("clear", new ClearCommand(collectionManager));
        commandsMap.put("filter_greater_than_price", new FilterGreaterThanPriceCommand(collectionManager));
        commandsMap.put("print_unique_unit_of_measure", new PrintUniqueUnitOfMeasureCommand(collectionManager));
        commandsMap.put("remove_by_id", new RemoveByIdCommand(collectionManager));
        commandsMap.put("remove", new RemoveCommand(collectionManager));
        commandsMap.put("remove_first", new RemoveFirstCommand(collectionManager));
        commandsMap.put("reorder", new ReorderCommand(collectionManager));
        commandsMap.put("show", new ShowCommand(collectionManager));
        commandsMap.put("update_by_id", new UpdateByIdCommand(collectionManager));
        commandsMap.put("history", new HistoryCommand(collectionManager));
        commandsMap.put("help", new HelpCommand(collectionManager));
        commandsMap.put("info", new InfoCommand(collectionManager));
        commandsMap.put("execute_script", new ExecuteScriptCommand(collectionManager));
        commandsMap.put("filter_by_manufacture_cost", new FilterByManufactureCostCommand(collectionManager));
        commandsMap.put("save", new SaveCommand(collectionManager));
        commandsMap.put("exit", new ExitCommand(collectionManager));
    }

    /**
     * executes the command in userInput
     * @param userInput the string that user entered as a command
     * @return the execution was successful
     */
    public boolean executeCommand(String userInput){
        var commandUnits = userInput.trim().toLowerCase().split(" ", 2);
        if(!commandsMap.containsKey(commandUnits[0])){
            messageHandler.displayToUser("Unknown command. Write help for help.");
            return false;
        }
        var command = commandsMap.get(commandUnits[0]);
        commandHistory.add(commandUnits[0]);
        command.execute(Arrays.copyOfRange(commandUnits, 1, commandUnits.length));
        return true;
    }

    private class ExecuteScriptCommand implements Command{
        private CollectionCustom<Product> productCollection;
        public ExecuteScriptCommand(CollectionCustom<Product> productCollection){
            this.productCollection = productCollection;
        }
        @Override
        public boolean execute(String[] args) {
            try {
                if(scriptsBeingExecuted.contains(args[0])){
                    messageHandler.displayToUser("WARNING. Recursion is prevented.");
                    return true;
                }
                else
                    scriptsBeingExecuted.add(args[0]);
                BufferedReader reader = new BufferedReader(new FileReader(args[0]));
                String[] finalUserCommand;
                String command;
                while((command = reader.readLine()) != null) {
                    executeCommand(command);
                }
                messageHandler.displayToUser("Commands ended.");
                reader.close();
            } catch (FileNotFoundException fileNotFoundException) {
                messageHandler.log("File not found. Try again.");
                return false;
            } catch (IOException ioException) {
                messageHandler.log("File reading exception. Try again.");
                return false;
            }
            scriptsBeingExecuted.remove(args[0]);
            return true;
        }

        @Override
        public String getInfo() {
            return "read and execute a script from specified file. You should enter path to file after entering a command.";
        }
    }

    private class HelpCommand implements Command{

        private CollectionCustom<Product> productCollection;
        public HelpCommand(CollectionCustom<Product> productCollection){
            this.productCollection = productCollection;
        }

        @Override
        public boolean execute(String[] args) {
            commandsMap.forEach((key, value) -> System.out.println(key + " - " + value.getInfo()));
            return true;
        }

        @Override
        public String getInfo() {
            return "print all elements in string representation to standard output";
        }
    }

    private class HistoryCommand implements Command{

        private CollectionCustom<Product> productCollection;
        public HistoryCommand(CollectionCustom<Product> productCollection){
            this.productCollection = productCollection;
        }

        @Override
        public boolean execute(String[] args) {
            System.out.println("9 last used commands:");
            for (var comm: commandHistory
                 ) {
                System.out.println(comm);
            }
            return true;
        }

        @Override
        public String getInfo() {
            return "output the last 9 used commands";
        }
    }
}
