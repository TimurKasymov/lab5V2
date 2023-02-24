package src.commands;

import src.interfaces.CollectionCustom;
import src.interfaces.Command;
import src.interfaces.CommandManagerCustom;
import src.models.Product;
import src.models.UnitOfMeasure;

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
        return true;
    }

    @Override
    public String getInfo() {
        return "clear the collection";
    }
}
