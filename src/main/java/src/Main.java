package src;

import src.service.InputService;

import java.util.Scanner;

public class Main {
    /**
     * my <a href="https://github.com/TimurKasymov/lab5V2">repo</a>
     * @param args program arguments
     */
    public static void main(String[] args) {
        var mesHandler = new MessageHandler();
        var collection = new CollectionManager(new XmlFileHandler(), mesHandler);
        var inputService = new InputService(mesHandler);
        var commandManager = new CommandManager(collection, mesHandler, inputService);
        while (true){
            var scanner = new Scanner(System.in);
            mesHandler.displayToUser("Enter a command: ");
            commandManager.executeCommand(scanner.nextLine());
        }
    }
}