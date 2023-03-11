package src;

import src.exceptions.NoAccessToFileException;
import src.interfaces.CollectionCustom;
import src.interfaces.Loadable;
import src.models.Organization;
import src.models.Product;
import src.service.CustomCollectionService;
import src.service.WayOfOrder;
import java.io.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;


public class CollectionManager implements CollectionCustom<Product> {

    private LinkedList<Product> products;
    private File xmlfile;
    private final Loadable fileManager;
    private LocalDateTime initializationTime;
    private final MessageHandler messageHandler;

    public CollectionManager(Loadable fileManager, MessageHandler messageHandler) {
        this.fileManager = fileManager;
        this.messageHandler = messageHandler;
        for(;;){
            try {
                Scanner scanner = new Scanner(System.in);
                var pathToFile = "";
                Map<String, String> env = System.getenv();
                if (env != null && env.get("pathToXMLCollection") != null)
                    pathToFile = env.get("pathToXMLCollection");
                else {
                    messageHandler.displayToUser("Enter a full path to XML file with collection or of the file where collection elements are " +
                            "going to be stored to while being saved: ");
                    pathToFile = scanner.nextLine();
                    var extension = pathToFile.split("\\.")[1];
                    if(!extension.equals("xml")){
                        messageHandler.displayToUser("the extension of the file must be .xml, try again");
                        continue;
                    }
                }
                xmlfile = new File(pathToFile);
                fileManager.load(xmlfile);
                products = fileManager.get();
                if (validateData())
                    products = products == null ? new LinkedList<Product>() : products;
                else {
                    products = new LinkedList<Product>();
                    messageHandler.displayToUser("the products in the specified file do not meet the validation criteria, loaded collection is cleared");
                }
                boolean up = true, down = true;
                var wayOfOrder = CustomCollectionService.determineWayOfOrder(products);
                if (wayOfOrder == WayOfOrder.NON)
                    products.sort((p, p1) -> (int) (p1.getId() - p.getId()));
                initializationTime = LocalDateTime.now(ZoneId.of("Europe/Moscow"));
                return;
            }
            catch (IndexOutOfBoundsException exception){
                messageHandler.displayToUser("the extension of the file must be .xml, try again");
            }
            catch (NoAccessToFileException exception) {
                messageHandler.displayToUser("You dont have access to the specified file. Try again.");
            }
            catch (Exception e){
                messageHandler.log("Something went wrong try again :)");
            }
        }

    }

    @Override
    public boolean validateData() {
        if (products.isEmpty())
            return true;

        var organizations = new LinkedList<Organization>();
        var productIds = new HashSet<Long>();
        var organizationIds = new HashSet<Long>();

        for (var prod : products) {
            if (prod.getManufacturer() != null) {
                var organization = prod.getManufacturer();
                organizationIds.add(organization.getId());
                organizations.add(prod.getManufacturer());
                if (organization.getName() == null || organization.getName().isEmpty() || organization.getAnnualTurnover() == null ||
                        organization.getAnnualTurnover() < 1 || organization.getOrganizationType() == null) {
                    return false;
                }
            }
            if (prod.getPrice() < 1 || prod.getCreationDate() == null || prod.getCoordinates() == null ||
                    prod.getName() == null || prod.getManufactureCost() == null || prod.getCoordinates().getX() == null
                    || prod.getCoordinates().getY() <= -264)
            {return false;}

            productIds.add(prod.getId());
        }
        var ids = productIds.toArray();
        var minId = Long.MAX_VALUE;
        for (Object id : ids) {
            if ((Long) id < minId)
                minId = (Long) id;
        }
        var minOrganizationId = organizationIds.stream().reduce(Long.MAX_VALUE, (m, i) -> {
            if (i < m) {
                m = i;
            }
            return m;
        });

        return ids.length >= products.size() && minId >= 1
                && minOrganizationId >= 1 && (long) organizationIds.size() >= organizationIds.size();

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
        try {
            if (!validateData()) {
                messageHandler.displayToUser("the product collection does not meet the validation criteria");
                return;
            }
            fileManager.save(products, xmlfile);
            messageHandler.displayToUser("collections successfully saved");
        } catch (Exception exception) {
            messageHandler.log(exception.getMessage());
        }
    }
}
