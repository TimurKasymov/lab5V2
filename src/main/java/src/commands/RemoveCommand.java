package src.commands;

import src.interfaces.CollectionCustom;
import src.interfaces.Command;
import src.interfaces.CommandManagerCustom;
import src.models.Product;
import src.models.UnitOfMeasure;

public class RemoveCommand extends CommandBase implements Command {

    public RemoveCommand(CommandManagerCustom commandManager){
        super(commandManager);
    }
    @Override
    public boolean execute(String[] args) {
        var commandMessageHandler = commandManager.getMessageHandler();

        try {
            var id = Long.parseLong(args[0]);
            commandManager.getCollectionManager().get().remove(id);
            return true;
        }
        catch (NumberFormatException exception){
            commandMessageHandler.displayToUser("ID must be an number. Try typing this command again");
                return false;
        }
    }

    @Override
    public String getInfo() {
        return "remove an element from the collection by its ID.";
    }
}
