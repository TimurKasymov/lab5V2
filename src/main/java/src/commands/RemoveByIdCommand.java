package src.commands;

import src.interfaces.CollectionCustom;
import src.interfaces.Command;
import src.interfaces.CommandManagerCustom;
import src.models.Product;
import src.models.UnitOfMeasure;

public class RemoveByIdCommand extends CommandBase implements Command {
    public RemoveByIdCommand(CommandManagerCustom commandManager){
        super(commandManager);
    }

    @Override
    public boolean execute(String[] args) {
        var commandMessageHandler = commandManager.getMessageHandler();

        try{
            long id = Long.parseLong(args[0]);
            var products = commandManager.getCollectionManager().get();
            if(id == 0){
                commandMessageHandler.displayToUser("ID must be an number greater than 0. Try typing this command again");
                return false;
            }
            for (Product product : products) {
                Long intId = product.getId();
                if (intId == id) {
                    products.remove(product);
                    commandMessageHandler.displayToUser("Element was successfully removed.");
                    return true;
                }
            }
        }
        catch (NumberFormatException exception){
            commandMessageHandler.displayToUser("ID must be an number. Try typing this command again");
            return false;
        }

        commandMessageHandler.displayToUser("Element with this ID is not defined. Try again.");
        return true;
    }

    @Override
    public String getInfo() {
        return "You should enter ID after entering a command.";
    }
}
