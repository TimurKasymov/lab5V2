package src.commands;

import src.interfaces.CollectionCustom;
import src.interfaces.Command;
import src.interfaces.CommandManagerCustom;
import src.models.Product;
import src.models.UnitOfMeasure;

import java.lang.reflect.ParameterizedType;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class InfoCommand extends CommandBase implements Command {
    public InfoCommand(CommandManagerCustom commandManager){
        super(commandManager);
    }

    @Override
    public boolean execute(String[] args) {
        var products = commandManager.getCollectionManager().get();
        var collection = commandManager.getCollectionManager();
        var time = collection.getInitializationTime().atZone(ZoneId.of("Europe/Moscow"));
        var toPrint = String.format("type of colleciton: %s\ninitialization date: %s\nnumber of elements: %s", collection.getElementType().getName(),
                time.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")), products.size() );
        System.out.println(toPrint);
        return true;
    }

    @Override
    public String getInfo() {
        return "prints information about the collection to the standard output stream (type, initialization date, number of elements, etc.)";
    }
}
