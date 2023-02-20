package src.commands;

import src.interfaces.CollectionCustom;
import src.interfaces.Command;
import src.models.Product;
import src.models.UnitOfMeasure;

public class SaveCommand implements Command {

    private final CollectionCustom<Product> productCollection;

    public SaveCommand(CollectionCustom<Product> productCollection){
        this.productCollection = productCollection;
    }

    @Override
    public boolean execute(String[] args) {
        productCollection.save();
        return true;
    }

    @Override
    public String getInfo() {
        return "save the collection to file";
    }
}
