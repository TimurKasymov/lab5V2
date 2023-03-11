package src.commands;

import src.interfaces.Command;
import src.interfaces.CommandManagerCustom;

public class ClearCommand extends CommandBase implements Command {

    public ClearCommand(CommandManagerCustom commandManager){
        super(commandManager);
    }

    @Override
    public boolean execute(String[] args) {
        var prods = commandManager.getCollectionManager().get();
        var numbOFLoops = prods.size();
        commandManager.getUndoManager().startOrEndTransaction();
        for (int i = 0; i < numbOFLoops; i++){
            commandManager.executeCommand("remove_by_id " + prods.get(0).getId());
        }
        commandManager.getUndoManager().startOrEndTransaction();
        commandManager.getMessageHandler().displayToUser("collection cleared successfully");
        return true;
    }

    @Override
    public String getInfo() {
        return "clear the collection";
    }
}
