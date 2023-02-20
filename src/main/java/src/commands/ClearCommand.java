package src.commands;

import src.interfaces.CollectionCustom;
import src.interfaces.Command;
import src.models.Product;
import src.models.UnitOfMeasure;

public class ClearCommand implements Command {
    private CollectionCustom<Product> productCollection;

    public ClearCommand(CollectionCustom<Product> productCollection){
        this.productCollection = productCollection;
    }

    @Override
    public boolean execute(String[] args) {
        productCollection.get().clear();
        return true;
    }

    @Override
    public String getInfo() {
        return "clear the collection";
    }
}
