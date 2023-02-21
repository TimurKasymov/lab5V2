package src.commands;

import src.interfaces.CollectionCustom;
import src.interfaces.Command;
import src.interfaces.CommandManagerCustom;
import src.models.Product;
import src.models.UnitOfMeasure;

public class FilterGreaterThanPriceCommand extends CommandBase implements Command {
    public FilterGreaterThanPriceCommand(CommandManagerCustom commandManager){
        super(commandManager);
    }

    @Override
    public boolean execute(String[] args) {
        var commandMessageHandler = commandManager.getMessageHandler();

        var products = commandManager.getCollectionManager().get();
        try{
            var flag = false;
            var price = Float.parseFloat(args[0]);
            for (var prod : products){
                if(prod.getPrice() > price){
                    System.out.println(prod.toString() + "\n");
                    flag = true;
                }
            }
            if(!flag)
                commandMessageHandler.displayToUser("no such elements found");
            return true;
        }
        catch (NumberFormatException | IndexOutOfBoundsException exception){
            commandMessageHandler.displayToUser("ID must be provided and it must be a number. Try typing this command again");
            return false;
        }
    }

    @Override
    public String getInfo() {
        return "display elements whose price field value is greater than the specified one";
    }
}
