package src.commands;

import src.interfaces.CollectionCustom;
import src.interfaces.Command;
import src.interfaces.CommandManagerCustom;
import src.models.Product;
import src.models.UnitOfMeasure;

import java.util.HashSet;

public class PrintUniqueUnitOfMeasureCommand extends CommandBase implements Command {
    public PrintUniqueUnitOfMeasureCommand(CommandManagerCustom commandManager){
        super(commandManager);
    }

    @Override
    public boolean execute(String[] args) {
        var commandMessageHandler = commandManager.getMessageHandler();
        var products = commandManager.getCollectionManager().get();
        var set = new HashSet<UnitOfMeasure>();
        for(var prod : products){
            if(prod.getUnitOfMeasure() != null)
                set.add(prod.getUnitOfMeasure());
        }
        for (var unit: set
        ) {
            commandMessageHandler.displayToUser(unit.toString());
        }
        return true;
    }

    @Override
    public String getInfo() {
        return "displays unique values of the unitOfMeasure field of all elements in the collection";
    }
}
