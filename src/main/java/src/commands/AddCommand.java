package src.commands;

import src.exceptions.CommandInterruptionException;
import src.interfaces.CollectionCustom;
import src.interfaces.Command;
import src.interfaces.CommandManagerCustom;
import src.models.InputMedium;
import src.models.Product;
import src.service.InputService;

import java.io.File;
import java.util.*;


public class AddCommand extends CommandBase implements Command {

    public AddCommand(CommandManagerCustom commandManager){
        super(commandManager);
    }

    @Override
    public boolean execute(String[] args) {
        var commandMessageHandler = commandManager.getMessageHandler();
        try{
            var inputService = commandManager.getInputService();
            commandMessageHandler.displayToUser("adding product..");
            var maxId = Long.MIN_VALUE;
            var products = commandManager.getCollectionManager().get();
            for (var product : products) {
                maxId = Long.max(maxId, product.getId());
            }
            var id = products.size() == 0 ? 1 : maxId + 1;

            var name = inputService.inputName();
            var coord = inputService.inputCoordinates();
            var price = inputService.inputPrice();
            var manufCost = inputService.inputManufactureCost();
            var unit = inputService.inputUnitOfMeasure();

            int yesOrNo = 0;
            for( ; ; ) {
                try {
                    commandMessageHandler.displayToUser("enter a number: ");
                    commandMessageHandler.displayToUser("should we add organization? enter the number: 1 - Yes or 2 - No");
                    yesOrNo = commandManager.getInputService().getInt();
                    if(yesOrNo != 1 && yesOrNo != 2)
                        continue;
                    if(yesOrNo == 2)
                        commandMessageHandler.displayToUser("organization will not be defined");
                    break;
                } catch (InputMismatchException e) {
                    commandMessageHandler.displayToUser("enter a number: ");
                }
            }
            var prod = new Product(id, name, coord, price, manufCost,
                    unit, yesOrNo == 1 ? inputService.inputOrganization(products) : null);
            commandManager.getUndoManager().logAddCommand(id);
            if (products.size() == 0) {
                products.add(prod);
                return true;
            }
            if (products.peekLast().getId() == maxId)
                products.add(prod);
            else
                products.addFirst(prod);
        }

        catch (NoSuchElementException exception){
            commandMessageHandler.displayToUser("adding product was canceled");
        }
        catch (CommandInterruptionException e){
            commandMessageHandler.displayToUser("adding product was canceled by entered command");
            commandManager.executeCommand(e.getEnteredCommand());
        }

        return true;
    }

    @Override
    public String getInfo() {
        return "add new element to the collection";
    }
}
