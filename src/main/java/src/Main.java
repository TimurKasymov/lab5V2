package src;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        var mesHandler = new MessageHandler();
        var collecation = new CollectionManager(new FileHandler(), mesHandler);
        var commandManager = new CommandManager(collecation, mesHandler);
        for(;;){
            var scanner = new Scanner(System.in);
            mesHandler.displayToUser("Enter a command: ");
            commandManager.executeCommand(scanner.nextLine());
        }
    }
}