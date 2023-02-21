package src;

import src.service.InputService;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        var mesHandler = new MessageHandler();
        var collection = new CollectionManager(new FileHandler(), mesHandler);
        var inputService = new InputService(mesHandler);
        var commandManager = new CommandManager(collection, mesHandler, inputService);
        while (true){
            var scanner = new Scanner(System.in);
            mesHandler.displayToUser("Enter a command: ");
            commandManager.executeCommand(scanner.nextLine());
        }
    }
}