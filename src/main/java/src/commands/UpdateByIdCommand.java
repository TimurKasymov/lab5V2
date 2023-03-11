package src.commands;

import src.CommandManager;
import src.exceptions.CommandInterruptionException;
import src.interfaces.CollectionCustom;
import src.interfaces.Command;
import src.interfaces.CommandManagerCustom;
import src.models.Product;
import src.service.InputService;

import java.util.Collections;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Class for updating the element by it`s ID
 */
public class UpdateByIdCommand extends CommandBase implements Command {
    private final InputService inputService;

    {
        inputService = commandManager.getInputService();
    }

    public UpdateByIdCommand(CommandManagerCustom commandManager) {
        super(commandManager);
    }

    @Override
    public boolean execute(String[] args) {
        var commandMessageHandler = commandManager.getMessageHandler();
        try {
            long id = Long.parseLong(args[0]);
            var products = commandManager.getCollectionManager().get();
            if (id <= 0) {
                commandMessageHandler.displayToUser("ID must be an number greater than 0. Try typing this command again");
                return false;
            }
            commandMessageHandler.displayToUser("updating product with id: " + id);
            for (Product product : products) {
                Long intId = product.getId();
                if (intId == id) {
                    var name = inputService.inputName();
                    var coord = inputService.inputCoordinates();
                    var price = inputService.inputPrice();
                    var manufCost = inputService.inputManufactureCost();
                    var unit = inputService.inputUnitOfMeasure();

                    int yesOrNo = 0;
                    for (; ; ) {
                        try {
                            commandMessageHandler.displayToUser("enter a number: ");
                            commandMessageHandler.displayToUser("should we add organization? enter the number: 1 - Yes or 2 - No");
                            Scanner scanner = new Scanner(System.in);
                            yesOrNo = scanner.nextInt();
                            if (yesOrNo == 1 || yesOrNo == 2)
                                if (yesOrNo == 2)
                                    commandMessageHandler.displayToUser("organization will not be defined");
                            break;
                        } catch (InputMismatchException e) {
                            commandMessageHandler.displayToUser("enter a number: ");
                        }
                    }
                    var prod = new Product(id, name, coord, price, manufCost,
                            unit, yesOrNo == 1 ? inputService.inputOrganization(products) : null);
                    var idInCollection = products.indexOf(product);
                    commandManager.getUndoManager().logUpdateCommand(product);
                    products.remove(product);
                    products.add(idInCollection, prod);
                    commandMessageHandler.displayToUser("Element was updated successfully");
                    return true;
                }
            }
        } catch (NumberFormatException exception) {
            System.out.println("ID must be an number. Try typing this command again");
        } catch (CommandInterruptionException e) {
            commandMessageHandler.displayToUser("updating product was canceled by entered command");
            commandManager.executeCommand(e.getEnteredCommand());
        }
        System.out.println("Element with this ID is not defined. Try again.");
        return true;
    }

    @Override
    public String getInfo() {
        return "update the element`s value, whose ID is equal to the given." +
                " You should enter ID after entering a command.";
    }
}
