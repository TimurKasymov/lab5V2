package src.commands;

import src.interfaces.CollectionCustom;
import src.interfaces.Command;
import src.interfaces.CommandManagerCustom;
import src.models.Product;
import src.models.UnitOfMeasure;

import java.util.Collections;

public class ReorderCommand extends CommandBase implements Command {

    public ReorderCommand(CommandManagerCustom commandManager){
        super(commandManager);
    }

    @Override
    public boolean execute(String[] args) {
        Collections.reverse(commandManager.getCollectionManager().get());
        return true;
    }

    @Override
    public String getInfo() {
        return "reorder the collection";
    }
}
