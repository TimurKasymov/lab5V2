package src;

import src.exceptions.FileLoadingException;
import src.exceptions.NoAccessToFileException;
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


    /**
     * HashSet collection for keeping a collection as java-object
     */
    private LinkedList<Product> products;

    /**
     * Field for saving date of initialization thw collection
     */
    private ZonedDateTime initializationDate;

    /**
     * Class which check file is existed, and can be readable and writeable.
     *
     * @return result of checkup
     */
    private boolean checkPermissions(File file) {
        if (!file.canRead()) {
            System.out.println("File cannot be read from. You should have this permission.");
            return false;
        }
        if (!file.canWrite()) {
            System.out.println("File cannot be written to. You should have this permission.");
            return false;
        }
        return true;
    }

    @Override
    public void load(File xmlfile) throws NoAccessToFileException, FileLoadingException {
        try {
            var fileCreated = xmlfile.createNewFile();
            if (!checkPermissions(xmlfile)) {
                throw new NoAccessToFileException(xmlfile);
            }
            initializationDate = ZonedDateTime.now();
            if (!xmlfile.exists()) {
                System.out.println("0 products were downloaded");
                products = new LinkedList<>();
                return;
            }
            final QName qName = new QName("product");
            // create xml event reader for input stream
            XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
            var fileReader = new FileReader(xmlfile);
            XMLEventReader xmlEventReader = xmlInputFactory.createXMLEventReader(fileReader);
            // initialize jaxb
            JAXBContext context = JAXBContext.newInstance(Products.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            XMLEvent e;
            products = unmarshaller.unmarshal(xmlEventReader, Products.class).getValue().getProducts();
            Collections.sort(products);
            System.out.println("loaded " + " products: " + products.size());
        } catch (Exception jaxbException) {
            throw new FileLoadingException(jaxbException.getMessage());
        }
    }

    /**
     * Method for saving (marshaling) java collection to XML-file and updating hash of file
     */
    @Override
    public boolean save(LinkedList<Product> products, File file) throws Exception {
        try {
            if (products.size() == 0)
                return true;
            var fileWriter = new FileWriter(file);
            var productsXml = new Products();
            productsXml.setProducts(products);
            JAXBContext jaxbContext = JAXBContext.newInstance(Products.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            //Marshal the persons list in file
            jaxbMarshaller.marshal(productsXml, fileWriter);
            fileWriter.close();
            return true;
        } catch (JAXBException | IOException jaxbException) {
            throw new FileLoadingException(jaxbException.getMessage());
        }
    }

    @Override
    public ZonedDateTime getInitializationTime() {
        return initializationDate;
    }

    @Override
    public LinkedList<Product> get() {
        return products;
    }
}
