package src;

import src.interfaces.CollectionCustom;
import src.interfaces.Loadable;
import src.models.*;
import src.interfaces.CollectionCustom;
import src.interfaces.Loadable;
import src.models.Organization;
import src.models.Product;

import java.io.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class CollectionManager implements CollectionCustom<Product> {

    private LinkedList<Product> products;
    private File xmlfile;
    private final Loadable fileManager;
    private LocalDateTime initializationTime;
    private final MessageHandler messageHandler;

    public CollectionManager(Loadable fileManager, MessageHandler messageHandler) {
        this.fileManager = fileManager;
        this.messageHandler = messageHandler;
        try{
            fileManager.load();
            products = fileManager.get();
            if(validateData())
                products = products == null ? new LinkedList<Product>() : products;
            else {
                products = new LinkedList<Product>();
                messageHandler.displayToUser("the products in the specified file do not meet the validation criteria, loaded collection is cleared");
            }
            boolean up = true, down = true;
            for (var i = 1; i < products.size(); i++) {
                if(products.get(i-1).compareTo(products.get(i)) > 0)
                    down = false;
                else if(products.get(i-1).compareTo(products.get(i)) < 0)
                    up = false;
                if(!(down || up))
                    break;
            }
            if(!(down || up))
                products.sort((p, p1) -> (int)(p1.getId() - p.getId()));
            initializationTime = LocalDateTime.now(ZoneId.of("Europe/Moscow"));
        }
        catch (Exception exception){
            messageHandler.log(exception.getMessage());
        }
    }

    @Override
    public boolean validateData(){
        if(products.isEmpty())
            return true;

        var organizations = new LinkedList<Organization>();
        var productIds = new HashSet<Long>();
        var organizationIds = new HashSet<Long>();

        for (var prod: products
             ) {
            if(prod.getManufacturer() !=  null){
                var organization = prod.getManufacturer();
                organizationIds.add(organization.getId());
                organizations.add(prod.getManufacturer());
                if(organization.getName() == null || organization.getName().isEmpty() || organization.getAnnualTurnover() == null ||
                        organization.getAnnualTurnover() < 1 || organization.getOrganizationType() == null){
                    return false;
                }
            }
            if(prod.getPrice() < 1 || prod.getCreationDate() == null || prod.getCoordinates() == null ||
            prod.getName() == null || prod.getManufactureCost() == null || prod.getCoordinates().getX() == null)
                return false;

            productIds.add(prod.getId());
        }
        var ids = productIds.toArray();
        var minId = Long.MAX_VALUE;
        for (Object id : ids) {
            if ((Long) id < minId)
                minId = (Long) id;
        }
        var minOrganizationId = organizationIds.stream().reduce(Long.MAX_VALUE, (m,i)-> {if (i< m){m = i;} return m; });

        if(ids.length < products.size() || minId < 1
                || minOrganizationId < 1 || organizationIds.stream().count() < organizationIds.size()){
            return false;
        }
        return true;

    }

    @Override
    public LinkedList<Product> get() {
        return products;
    }

    @Override
    public LocalDateTime getInitializationTime() {
        return initializationTime;
    }

    @Override
    public Class getElementType() {
        return Product.class;
    }

    @Override
    public void save() {
        try{
            if (!validateData()) {
                messageHandler.displayToUser("the product collection does not meet the validation criteria");
                return;
            }
            fileManager.save(products);
            messageHandler.displayToUser("collections successfully saved");
        }
        catch (Exception exception){
            messageHandler.log(exception.getMessage());
        }
    }
}
