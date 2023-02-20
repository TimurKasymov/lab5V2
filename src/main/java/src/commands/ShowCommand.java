package src.commands;

import src.interfaces.CollectionCustom;
import src.interfaces.Command;
import src.models.Product;
import src.models.UnitOfMeasure;

import java.util.Iterator;
import java.util.List;

public class ShowCommand implements Command {
    private CollectionCustom<Product> collectionCustom;
    public ShowCommand(CollectionCustom<Product> collectionCustom){
        this.collectionCustom = collectionCustom;
    }

    @Override
    public boolean execute(String[] args) {
        var products = collectionCustom.get();
        if(products == null || products.size() == 0){
            System.out.println("there is no products yet.. add a new one");
            return true;
        }
        for (Product product : products) {
            System.out.println(product.toString() + "\n");
        }
        return true;
    }

    @Override
    public String getInfo() {
        return "printing collection elements into the string representation";
    }
}
