package src;

import src.exceptions.FileLoadingException;
import src.interfaces.Loadable;
import src.models.Product;
import src.models.Products;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.XMLEvent;
import java.io.*;
import java.time.ZonedDateTime;
import java.util.*;

public class FileHandler implements Loadable {

    /** HashSet collection for keeping a collection as java-object */
    private LinkedList<Product> products;

    /** Field used for saving collection into xml file */
    private File xmlfile;
    /** Field for saving date of initialization thw collection */
    private ZonedDateTime initializationDate;

    /**
     * Class which check file is existed, and can be readable and writeable.
     * @return result of checkup
     */
    private boolean checkPermissions(String pathToFile) {
        File checkingFile = new File(pathToFile);
        if (!checkingFile.exists()) {
            return true;
        }
        if (!checkingFile.canRead()) {
            System.out.println("File cannot be readable. You should to have this permission.");
            return false;
        }
        if (!checkingFile.canWrite()) {
            System.out.println("File cannot be writeable. You should to have this permission.");
            return false;
        }
        return true;
    }

    @Override
    public LinkedList<Product> load() throws Exception {
        Scanner scanner = new Scanner(System.in);
        try {
            for ( ; ; ) {
                Map<String, String> env = System.getenv();
                var pathToFile = env.get("pathToXMLCollection");
                if(pathToFile == null){
                    System.out.print("Enter a full path to XML file with collection or of the file where collection elements are " +
                            "going to be stored to while being saved: ");
                    pathToFile = scanner.nextLine();
                }

                if (!checkPermissions(pathToFile)){
                    System.out.println("You dont have access to the specified file. Try again.");
                    load();
                    return null;
                }
                try {
                    initializationDate = ZonedDateTime.now();
                    xmlfile = new File(pathToFile);
                    if(!xmlfile.exists()){
                        System.out.println("0 products were downloaded");
                        return null;
                    }
                    final QName qName = new QName("product");
                    // create xml event reader for input stream
                    XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
                    xmlfile = new File(pathToFile);
                    var fileReader = new FileReader(xmlfile);
                    XMLEventReader xmlEventReader = xmlInputFactory.createXMLEventReader(fileReader);
                    // initialize jaxb
                    JAXBContext context = JAXBContext.newInstance(Products.class);
                    Unmarshaller unmarshaller = context.createUnmarshaller();
                    XMLEvent e;
                    products = unmarshaller.unmarshal(xmlEventReader, Products.class).getValue().getProducts();
                    Collections.sort(products);
                    System.out.println("loaded " + " products: " + products.size());
                    break;
                } catch (Exception jaxbException) {
                    throw new FileLoadingException(jaxbException.getMessage());
                }
            }
        } catch (NoSuchElementException noSuchElementException) {
            System.exit(0);
        }
        return products;
    }

    /** Method for saving (marshaling) java collection to XML-file and updating hash of file */
    @Override
    public boolean save(LinkedList<Product> products) throws Exception{
        try {
            if(products.size() == 0)
                return true;
            var fileWriter = new FileWriter(xmlfile);
            var productsXml = new Products();
            productsXml.setProducts(products);
            JAXBContext jaxbContext = JAXBContext.newInstance(Products.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            //Marshal the persons list in file
            jaxbMarshaller.marshal(productsXml, fileWriter);
            return true;
        } catch (JAXBException | IOException jaxbException) {
            throw new FileLoadingException(jaxbException.getMessage());
        }
    }

    @Override
    public ZonedDateTime getInitializationTime() {
        return initializationDate;
    }
}
