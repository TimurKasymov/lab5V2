package src.commands;

import src.interfaces.CollectionCustom;
import src.interfaces.Command;
import src.models.Product;
import src.models.UnitOfMeasure;

import java.util.LinkedList;

public class RemoveFirstCommand implements Command {
    private CollectionCustom<Product> productCollection;

    public RemoveFirstCommand(CollectionCustom<Product> productCollection){
        this.productCollection = productCollection;
    }

    @Override
    public boolean execute(String[] args) {
        ((LinkedList<Product>)(productCollection.get())).removeFirst();
        return true;
    }

    @Override
    public String getInfo() {
        return "removes first element in the collection";
    }
}
