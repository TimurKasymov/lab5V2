package src;

import src.commands.*;
import src.container.CommandsContainer;
import src.interfaces.*;
import src.models.InputMedium;
import src.models.Product;
import src.interfaces.CollectionCustom;
import src.interfaces.Command;
import src.service.InputService;

import java.io.File;
import java.util.*;

public class CommandManager implements CommandManagerCustom {

    private UndoManager undoManager;
    private CollectionCustom<Product> collectionManager = null;
    private InputService inputService;
    private LinkedList<String> scriptFilesBeingExecuted;
    private HashMap<String, Command> commandsMap;

    private LinkedList<String> commandHistory;
    private MessageHandler messageHandler;

    /**
     * Constructor for making a CommandManager
     * @param manager - collection with objects to manipulate
     * @param messageHandler - service for message handling
     */
    public CommandManager(CollectionCustom<Product> manager, MessageHandler messageHandler, InputService inputService) {
        this.messageHandler = messageHandler;
        this.collectionManager = manager;
        this.inputService = inputService;
        this.scriptFilesBeingExecuted = new LinkedList<>();
        commandHistory = new LinkedList<>();
        commandsMap = new HashMap<>();
        commandsMap.put("add", new AddCommand(this));
        commandsMap.put("clear", new ClearCommand(this));
        commandsMap.put("filter_greater_than_price", new FilterGreaterThanPriceCommand(this));
        commandsMap.put("print_unique_unit_of_measure", new PrintUniqueUnitOfMeasureCommand(this));
        commandsMap.put("remove_by_id", new RemoveByIdCommand(this));
        commandsMap.put("remove_first", new RemoveFirstCommand(this));
        commandsMap.put("reorder", new ReorderCommand(this));
        commandsMap.put("show", new ShowCommand(this));
        commandsMap.put("update_by_id", new UpdateByIdCommand(this));
        commandsMap.put("history", new HistoryCommand(this));
        commandsMap.put("help", new HelpCommand(this));
        commandsMap.put("info", new InfoCommand(this));
        commandsMap.put("execute_script", new ExecuteScriptCommand(this));
        commandsMap.put("filter_by_manufacture_cost", new FilterByManufactureCostCommand(this));
        commandsMap.put("save", new SaveCommand(this));
        commandsMap.put("exit", new ExitCommand(this));
        commandsMap.put("undo_commands", new UndoCommand(this));
        CommandsContainer.setCommands(commandsMap.keySet().stream().toList());
        try{
            this.undoManager = new UndoManager(new File("product_logging.xml"), new File("command_logging.txt"), messageHandler, collectionManager);
        }
        catch (Exception e){
            messageHandler.displayToUser("fatal error, logging files can not be created or opened");
            System.exit(0);
        }

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

    @Override
    public List<String> getCommandHistory() {
        return commandHistory;
    }

    @Override
    public List<String> getCommandsInfo() {
        var commandInfos = new ArrayList<String>(commandsMap.size());
        commandsMap.forEach((key, value) -> commandInfos.add(key + " - " + value.getInfo()));
        return commandInfos;
    }

    @Override
    public InputService getInputService() {
        return inputService;
    }



    @Override
    public CollectionCustom<Product> getCollectionManager() {
        return collectionManager;
    }

    @Override
    public MessageHandler getMessageHandler() {
        return messageHandler;
    }

    @Override
    public UndoManager getUndoManager() {
        return undoManager;
    }


}
