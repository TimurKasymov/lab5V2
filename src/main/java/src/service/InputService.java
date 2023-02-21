package src.service;

import src.MessageHandler;
import src.models.*;
import src.models.Product;

import java.util.Collection;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class InputService {

    private final MessageHandler messageHandler;
    public InputService(MessageHandler messageHandler){
        this.messageHandler = messageHandler;
    }
    private Scanner scanner = null;
    {
        scanner = new Scanner(System.in);
    }

    public void setScanner(Scanner scanner){
        this.scanner = scanner;
    }

    public String inputName() throws NoSuchElementException {
        for ( ; ; ) {
            try {
                messageHandler.displayToUser("Do not enter a very long name, some parts of it may get lost");
                messageHandler.displayToUser("Enter a name: ");
                String name = scanner.nextLine().trim();
                if (name.equals("")) {
                    messageHandler.displayToUser("This value cannot be empty. Try again");
                    continue;
                }
                return name;
            } catch (InputMismatchException inputMismatchException) {
                messageHandler.displayToUser("This value must be string.");
                scanner.next();
            }
        }
    }

    public OrganizationType inputOrganizationType() throws NoSuchElementException {
        for ( ; ; ) {
            try {
                messageHandler.displayToUser("Choose OrganizationType. Enter the number corresponding to the desired option. ");
                inputEnum(OrganizationType.class);
                var orgType = readEnum(OrganizationType.class);
                return (OrganizationType) orgType;
            } catch (InputMismatchException inputMismatchException) {
                messageHandler.displayToUser("This value must be a number. Try again. ");
                scanner.next();
            }
        }
    }

    public Integer getInt() throws NoSuchElementException {
        for ( ; ; ) {
            try {
                return scanner.nextInt();
            } catch (InputMismatchException inputMismatchException) {
                messageHandler.displayToUser("This value must be a number. Try again. ");
                scanner.next();
            }
        }
    }


    /** method for combining X and Y inputs */
    public Coordinates inputCoordinates() throws NoSuchElementException {
        messageHandler.displayToUser("adding coordinates..");
        var coor = new Coordinates(inputXLocation(), inputYLocation());
        messageHandler.displayToUser("done with coordinates..");
        return coor;
    }

    /**
     * Method for receiving x-coordinate of location of element
     * @return Double xLocation
     */
    public Double inputXLocation() throws NoSuchElementException {
        for ( ; ; ) {
            try {
                messageHandler.displayToUser("Enter X coordinate of location: ");
                return scanner.nextDouble();
            } catch (InputMismatchException inputMismatchException) {
                messageHandler.displayToUser("This value must be a double-type number. Try again. ");
                scanner.next();
            }
        }
    }

    /**
     * Method for receiving y-coordinate of element
     * @return float yLocation
     */
    public float inputYLocation()  throws NoSuchElementException {
        for ( ; ; ) {
            try {
                messageHandler.displayToUser("Enter Y coordinate of location: ");
                return scanner.nextFloat();
            } catch (InputMismatchException inputMismatchException) {
                messageHandler.displayToUser("This value must be a float-type number. Try again. ");
                scanner.next();
            }
        }
    }

    /** method for taking price input */
    public float inputPrice() throws NoSuchElementException {
        for ( ; ; ) {
            try {
                messageHandler.displayToUser("Enter the price of the product: ");
                var price = scanner.nextFloat();
                if(price < 0)
                    throw new InputMismatchException();
                return price;
            } catch (InputMismatchException inputMismatchException) {
                messageHandler.displayToUser("This value must be a float-type positive number. Try again. ");
                scanner.next();
            }
        }
    }

    /** method for taking price input */
    public Double inputManufactureCost() throws NoSuchElementException {
        for ( ; ; ) {
            try {
                messageHandler.displayToUser("Enter manufacture cost: ");
                var inp = scanner.nextDouble();
                if(inp < 1)
                    throw new InputMismatchException();
                return inp;
            } catch (InputMismatchException inputMismatchException) {
                messageHandler.displayToUser("This value must be a Double-type number. Try again. ");
                scanner.next();
            }
        }
    }

    public <T extends Enum<T>> void inputEnum(Class<T> enumClass) throws NoSuchElementException {
        var enums = enumClass.getEnumConstants();
        for(int i = 1; i <= enums.length; i++)
            messageHandler.displayToUser(enums[i - 1] + " - " + i);
    }

    public <T extends Enum<T>> Enum<T> readEnum(Class<T> enumClass) throws NoSuchElementException {
        var enums = enumClass.getEnumConstants();
        while (true){
            try {
                var index = scanner.nextInt();
                if(1 > index || index > enums.length){
                    messageHandler.displayToUser(String.format("You should enter a number from %s to %s. Try again. ", 1, enums.length));
                    continue;
                }
                return enums[index - 1];
            } catch (InputMismatchException inputMismatchException) {
                scanner.next();
            }

        }
    }



    public UnitOfMeasure inputUnitOfMeasure() throws NoSuchElementException {
        for ( ; ; ) {
            try {
                messageHandler.displayToUser("Choose UnitOfMeasure. Enter the number corresponding to the desired option. ");
                inputEnum(UnitOfMeasure.class);
                var unitOfMeasure = readEnum(UnitOfMeasure.class);
                return (UnitOfMeasure) unitOfMeasure;

            } catch (InputMismatchException inputMismatchException) {
                messageHandler.displayToUser("This value must be a number. Try again. ");
                scanner.next();
            }
        }
    }

    public Organization inputOrganization(Collection<Product> products) throws NoSuchElementException {
        var maxId = Long.MIN_VALUE;
        for (var prod: products
        ) {
            if(prod.getManufacturer() !=  null){
                var organization = prod.getManufacturer();
                maxId = Long.max(organization.getId(), maxId);
            }
        }
        messageHandler.displayToUser("adding organization..");
        var org = new Organization(maxId < 0 ? 1 : maxId + 1, inputName(), inputAnnualTurnover(), inputOrganizationType() );
        messageHandler.displayToUser("done with organization..");
        return org;
    }

    public Integer inputAnnualTurnover()  throws NoSuchElementException {
        for ( ; ; ) {
            try {
                messageHandler.displayToUser(String.format("Enter annual turnover. Note that value can only be from %s to %s: ", 1, Integer.MAX_VALUE));
                var inp = scanner.nextInt();
                if(inp < 1)
                    throw new InputMismatchException();
                return inp;
            } catch (InputMismatchException inputMismatchException) {
                messageHandler.displayToUser("This value must be a double-type number. Try again. ");
                scanner.next();
            }
        }
    }

}
