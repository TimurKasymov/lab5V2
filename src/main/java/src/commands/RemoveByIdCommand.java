package src.commands;

import src.interfaces.Command;
import src.interfaces.CommandManagerCustom;
import src.models.Product;

public class RemoveByIdCommand extends CommandBase implements Command {

    public RemoveByIdCommand(CommandManagerCustom commandManager){
        super(commandManager);
    }
    @Override
    public boolean execute(String[] args) {
        var commandMessageHandler = commandManager.getMessageHandler();

        try {
            var id = Long.parseLong(args[0]);
            var prods = commandManager.getCollectionManager().get();
            if(prods.stream().map(Product::getId).toList().contains(id)) {
                var prodWithId = prods.stream().filter(p-> p.getId() == id).findFirst();
                if(prodWithId.isEmpty())
                    throw new NumberFormatException();
                commandManager.getUndoManager().logRemoveCommand(prodWithId.get());
                prods.remove(prodWithId.get());
                return true;
            }
            commandMessageHandler.displayToUser("Element with this id doesnt exist");
            return false;
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
