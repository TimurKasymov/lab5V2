package src.commands;

import src.interfaces.CollectionCustom;
import src.interfaces.Command;
import src.interfaces.CommandManagerCustom;
import src.models.Product;
import src.models.UnitOfMeasure;

public class SaveCommand extends CommandBase implements Command {


    public SaveCommand(CommandManagerCustom commandManager){
        super(commandManager);
    }

    @Override
    public boolean execute(String[] args) {
        commandManager.getCollectionManager().save();
        commandManager.getUndoManager().saveLoggingFiles();
        return true;
    }

    @Override
    public String getInfo() {
        return "save the collection to file";
    }
}
