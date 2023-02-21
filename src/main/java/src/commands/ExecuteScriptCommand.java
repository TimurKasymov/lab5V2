package src.commands;

import src.interfaces.Command;
import src.interfaces.CommandManagerCustom;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ExecuteScriptCommand extends CommandBase implements Command {
    private final LinkedList<String> scriptFilesBeingExecuted;
    public ExecuteScriptCommand(CommandManagerCustom commandManager){
        super(commandManager);
        scriptFilesBeingExecuted = new LinkedList<>();
    }

    @Override
    public boolean execute(String[] args) {
        var commandMessageHandler = commandManager.getMessageHandler();

        try {
            if(scriptFilesBeingExecuted.contains(args[0])){
                commandMessageHandler.displayToUser("WARNING. Recursion is prevented.");
                return true;
            }
            else
                scriptFilesBeingExecuted.add(args[0]);
            Scanner reader = new Scanner(new FileReader(args[0]));
            commandManager.getInputService().setScanner(reader);
            String[] finalUserCommand;
            String command;
            while((command = reader.nextLine()) != null) {
                commandManager.executeCommand(command);
                commandManager.getInputService().setScanner(reader);
            }
            commandMessageHandler.displayToUser("Commands ended.");
            reader.close();
        } catch (FileNotFoundException fileNotFoundException) {
            commandMessageHandler.log("File not found. Try again.");
            return false;
        } catch (NoSuchElementException exception){
            commandMessageHandler.displayToUser("commands from the script " + scriptFilesBeingExecuted.getLast() + " ended.");
        }
        scriptFilesBeingExecuted.remove(args[0]);
        if(scriptFilesBeingExecuted.size() == 0)
            commandManager.getInputService().setScanner(new Scanner(System.in));
        return true;
    }

    @Override
    public String getInfo() {
        return "read and execute a script from specified file. You should enter path to file after entering a command.";
    }
}