package src.commands;

import src.interfaces.CollectionCustom;
import src.interfaces.Command;
import src.interfaces.CommandManagerCustom;
import src.models.Product;
import src.models.UnitOfMeasure;

import java.util.LinkedList;

public class RemoveFirstCommand extends CommandBase implements Command {

    public RemoveFirstCommand(CommandManagerCustom commandManager){
        super(commandManager);
    }

    @Override
    public boolean execute(String[] args) {
        ((LinkedList<Product>)(commandManager.getCollectionManager().get())).removeFirst();
        return true;
    }

    @Override
    public String getInfo() {
        return "removes first element in the collection";
    }
}
