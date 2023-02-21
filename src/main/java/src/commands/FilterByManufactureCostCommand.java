package src.commands;

import src.interfaces.Command;
import src.interfaces.CommandManagerCustom;
import src.models.Product;

public class FilterByManufactureCostCommand extends CommandBase implements Command {
    public FilterByManufactureCostCommand(CommandManagerCustom commandManager){
        super(commandManager);
    }

    @Override
    public boolean execute(String[] args) {
        var commandMessageHandler = commandManager.getMessageHandler();
        try {
            var manufactureCost = Double.valueOf(args[0]);
            var products = commandManager.getCollectionManager().get();
            for (Product product : products) {
                if(product.getManufactureCost().doubleValue() == manufactureCost)
                    commandMessageHandler.displayToUser(product.toString() + "\n");
            }
        }
        catch (Exception exception){
            commandMessageHandler.displayToUser(String.format("Manufacture cost must be from %s to %s. Try typing this command again", 0, Double.MAX_VALUE));
            return false;
        }
        return true;
    }

    @Override
    public String getInfo() {
        return "display items whose manufactureCost field value is equal to the given on enter the manufacture cost right after the command name";
    }
}
