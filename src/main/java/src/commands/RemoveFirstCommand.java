package src.commands;

import src.interfaces.Command;
import src.interfaces.CommandManagerCustom;

public class RemoveFirstCommand extends CommandBase implements Command {

    public RemoveFirstCommand(CommandManagerCustom commandManager) {
        super(commandManager);
    }

    @Override
    public boolean execute(String[] args) {
        if (commandManager.getCollectionManager().get().size() == 0) {
            commandManager.getMessageHandler().displayToUser("collection is empty");
            return true;
        }
        var removeCommand = "remove_by_id 1";
        commandManager.executeCommand(removeCommand);

        return true;
    }

    @Override
    public String getInfo() {
        return "removes first element in the collection";
    }
}
