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
        return true;
    }

    @Override
    public String getInfo() {
        return "reorder the collection";
    }
}
