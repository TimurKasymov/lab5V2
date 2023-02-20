package src.commands;

import src.interfaces.CollectionCustom;
import src.interfaces.Command;
import src.models.Product;
import src.models.UnitOfMeasure;

import java.util.HashSet;

public class PrintUniqueUnitOfMeasureCommand implements Command {
    private CollectionCustom<Product> collectionCustom;
    public PrintUniqueUnitOfMeasureCommand(CollectionCustom<Product> collectionCustom){
        this.collectionCustom = collectionCustom;
    }

    @Override
    public boolean execute(String[] args) {
        var set = new HashSet<UnitOfMeasure>();
        for(var prod : collectionCustom.get()){
            if(prod.getUnitOfMeasure() != null)
                set.add(prod.getUnitOfMeasure());
        }
        for (var unit: set
        ) {
            System.out.println(unit);
        }
        return true;
    }

    @Override
    public String getInfo() {
        return "displays unique values of the unitOfMeasure field of all elements in the collection";
    }
}
