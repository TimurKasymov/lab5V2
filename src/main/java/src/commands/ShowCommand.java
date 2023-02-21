package src.commands;

import src.interfaces.CollectionCustom;
import src.interfaces.Command;
import src.interfaces.CommandManagerCustom;
import src.models.Product;


public class ShowCommand extends CommandBase implements Command {
    public ShowCommand(CommandManagerCustom commandManager){
        super(commandManager);
    }

    @Override
    public boolean execute(String[] args) {
        var commandMessageHandler = commandManager.getMessageHandler();
        var products = commandManager.getCollectionManager().get();
        if(products == null || products.size() == 0){
            commandMessageHandler.displayToUser("there is no products yet.. add a new one");
            return true;
        }
        for (Product product : products) {
            String mess = product.toString() + '\n';
            commandMessageHandler.displayToUser(mess); //TODO:
        }
        return true;
    }

    @Override
    public String getInfo() {
        return "printing collection elements into the string representation";
    }
}
