package src.commands;

import src.interfaces.Command;
import src.interfaces.CommandManagerCustom;

import java.util.Collections;

public class ReorderCommand extends CommandBase implements Command {

    public ReorderCommand(CommandManagerCustom commandManager){
        super(commandManager);
    }

    @Override
    public boolean execute(String[] args) {
        Collections.reverse(commandManager.getCollectionManager().get());
        commandManager
                .getUndoManager()
                .logReorderCommand();
        commandManager.getMessageHandler().displayToUser("collection was reordered successfully");
        return true;
    }

    @Override
    public String getInfo() {
        return "reorder the collection";
    }
}
