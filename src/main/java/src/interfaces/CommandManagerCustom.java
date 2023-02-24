package src.interfaces;

import src.MessageHandler;
import src.UndoManager;
import src.models.InputMedium;
import src.models.Product;
import src.service.InputService;

import java.util.List;


public interface CommandManagerCustom {
    /** executes given command */
    boolean executeCommand(String userInput);
    /** gets the history of executed commands */
    List<String> getCommandHistory();
    /** gets the info about each command */
    List<String> getCommandsInfo();
    /** gets input service */
    InputService getInputService();
    /** gets collection manager */
    CollectionCustom<Product> getCollectionManager();
    /** gets message handler */
    MessageHandler getMessageHandler();
    /** gets undoManager */
    UndoManager getUndoManager();
}
