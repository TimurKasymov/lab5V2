package src.commands;

import src.interfaces.CollectionCustom;
import src.interfaces.Command;
import src.interfaces.CommandManagerCustom;
import src.models.Product;

public class HistoryCommand extends CommandBase implements Command {

    public HistoryCommand(CommandManagerCustom commandManager){
        super(commandManager);
    }

    @Override
    public boolean execute(String[] args) {
        var commandMessageHandler = commandManager.getMessageHandler();

        commandMessageHandler.displayToUser("9 last used commands:");
        var commandHistory = commandManager.getCommandHistory();
        int counter = 0;
        for(int i = commandHistory.size() - 1; i >= 0 && counter < 9; i--){
            System.out.println(commandHistory.get(i));
            counter++;
        }
        return true;
    }

    @Override
    public String getInfo() {
        return "output the last 9 used commands";
    }
}
