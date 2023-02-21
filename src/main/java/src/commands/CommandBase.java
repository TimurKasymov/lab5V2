package src.commands;

import src.MessageHandler;
import src.interfaces.CollectionCustom;
import src.interfaces.CommandManagerCustom;
import src.models.Product;
import src.service.InputService;

public class CommandBase {
    protected CommandManagerCustom commandManager;

    public CommandBase(CommandManagerCustom commandManager){
        this.commandManager = commandManager;
    }
}
