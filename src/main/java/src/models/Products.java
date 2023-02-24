package src.models;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@XmlRootElement(name = "products")
@XmlAccessorType (XmlAccessType.NONE)
public class Products {

    @XmlElement(name = "product")
    private List<Product> products = null;

    public LinkedList<Product> getProducts() {
        return new LinkedList<Product>(products);
    }

    public void setProducts(List<Product> persons) {
        this.products = persons;
    }

}


