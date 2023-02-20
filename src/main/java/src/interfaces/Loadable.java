package src.interfaces;

import src.models.Product;

import java.time.ZonedDateTime;
import java.util.LinkedList;

public interface Loadable {

    /** loads the collection from the file
     * @return LinkedList of products
     */
    LinkedList<Product> load() throws Exception;
    /** saves the collection LinkedList<Product> products to the file*/
    boolean save(LinkedList<Product> products) throws Exception;
    /** gets the initialization time */
    ZonedDateTime getInitializationTime();
}
