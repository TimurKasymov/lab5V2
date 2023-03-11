package src.commands;

import src.container.CommandsContainer;
import src.exceptions.CommandInterruptionException;
import src.exceptions.InterruptionCause;
import src.interfaces.Command;
import src.interfaces.CommandManagerCustom;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.Scanner;

public class ExecuteScriptCommand extends CommandBase implements Command {
    private final LinkedList<String> scriptFilesBeingExecuted;
    private int recDepth = -1;

    public ExecuteScriptCommand(CommandManagerCustom commandManager) {
        super(commandManager);
        scriptFilesBeingExecuted = new LinkedList<>();
    }

    private int specifyRecDepth() throws CommandInterruptionException {
        var commandMessageHandler = commandManager.getMessageHandler();
        commandMessageHandler.displayToUser("WARNING. Recursion is detected.");
        commandMessageHandler.displayToUser("please specify the recursion depth");
        var sc = new Scanner(System.in);
        for (; ; ) {
            try {
                var str = sc.nextLine();
                if (str.equals("")) {
                    commandManager.getMessageHandler().displayToUser("This value cannot be empty. Try again");
                    continue;
                }
                if (CommandsContainer.contains(str))
                    throw new CommandInterruptionException(str);
                return Integer.parseInt(str);
            } catch (InputMismatchException inputMismatchException) {
                commandManager.getMessageHandler().displayToUser("This value must be a number. Try again. ");
                sc.next();
            }
        }
    }

    @Override
    public boolean execute(String[] args) {
        var commandMessageHandler = commandManager.getMessageHandler();
        try {
            if (scriptFilesBeingExecuted.contains(args[0])) {
                if (recDepth == -1) {
                    recDepth = specifyRecDepth();
                }
                var currentRecursionDepth = scriptFilesBeingExecuted.stream().filter(s -> s.equals(args[0])).count();
                if (currentRecursionDepth >= recDepth) {
                    recDepth = -1;
                    return true;
                }
            }
            if (scriptFilesBeingExecuted.size() == 0) {
                commandManager.getUndoManager().startOrEndTransaction();
            }
            scriptFilesBeingExecuted.add(args[0]);

            Scanner reader = new Scanner(new FileReader(args[0]));
            commandManager.getInputService().setScanner(reader);
            String[] finalUserCommand;
            String command;
            while (reader.hasNext() && (command = reader.nextLine()) != null) {
                commandManager.executeCommand(command);
                commandManager.getInputService().setScanner(reader);
            }
            commandMessageHandler.displayToUser("Commands ended.");
            reader.close();

            scriptFilesBeingExecuted.remove(args[0]);
            if (scriptFilesBeingExecuted.size() == 0) {
                commandManager.getUndoManager().startOrEndTransaction();
            }
        } catch (FileNotFoundException fileNotFoundException) {
            commandMessageHandler.log("File not found. Try again.");
            return false;
        } catch (CommandInterruptionException e) {
            if (e.getInterruptionCause() == InterruptionCause.EXIT)
                commandMessageHandler.displayToUser("adding product was successfully canceled");
            else {
                commandMessageHandler.displayToUser("adding product was canceled by entered command");
                commandManager.executeCommand(e.getEnteredCommand());
            }
        }
        return true;
    }


    @Override
    public String getInfo() {
        return "read and execute a script from specified file. You should enter path to file after entering a command.";
    }
}