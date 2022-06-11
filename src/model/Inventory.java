package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Class Inventory
 */
public class Inventory {
    private static final ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static final ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     * addPart method adds a provided part to the allParts List.
     * @param newPart Part object
     */
    public static void addPart(Part newPart){
        allParts.add(newPart);
    }

    /**
     * addProduct method adds a provied product to the allProducts list.
     * @param newProduct Product object
     */
    public static void addProduct(Product newProduct){
        allProducts.add(newProduct);
    }

    /**
     * lookupPart method finds a part from a provided part ID in allParts.
     * @param partId int id of a part
     * @return null if not in list, Part if in list
     */
    public static Part lookupPart(int partId){
        Part partIdFound = null;

        for(Part part : allParts){
            if(part.getId() == partId){
                partIdFound = part;
            }
        }
        return partIdFound;
    }

    /**
     * loopProduct finds a product by ID in allProducts.
     * @param productID int product ID of product
     * @return the product if found, if not returns null
     */
    public static Product lookupProduct(int productID) {
        Product productIdFound = null;  // null Product

        for(Product product : allProducts){ //loop through all products
            if(product.getId() == productID){ //is the id in the set
                productIdFound= product; //if yes then the return product to that product
            }
        }
        return productIdFound;
    }

    /**
     * lookupPart find a part with the provided name.
     * @param partName String containing a part name
     * @return a list of parts matching the string provided
     */
    public static ObservableList<Part> lookupPart(String partName){
        ObservableList<Part> foundPart =  FXCollections.observableArrayList(); // empty observable list

        // loop through all parts looking for parts that match the provided string
        for(Part part : allParts){
            if(part.getName().toLowerCase().contains(partName.toLowerCase())){ //does the part contain the string
                foundPart.add(part); // if so add it to the list
            }
        }
        return foundPart; // return the list
    }

    /**
     * lookupProduct finds all products that match a provided string.
     * @param productName String containing a product name
     * @return ObservableList of Products
     */
    public static ObservableList<Product> lookupProduct(String productName){
        ObservableList<Product> foundProduct =  FXCollections.observableArrayList(); // new list to return

        //loop through all products
        for(Product product : allProducts){
            //if the name of the product contains the in put stream
            if(product.getName().toLowerCase().contains(productName.toLowerCase())){
                foundProduct.add(product); // if yes add that product to the list
            }
        }
        return foundProduct;
    }

    /**
     * updatePart method updates a part at a provided index with a provided part.
     * @param index int index of a part
     * @param selectedPart Part info to replace the selected part
     */
    public static void updatePart(int index, Part selectedPart){
        allParts.set(index, selectedPart);
    }

    /**
     * updateProduct method updates a product at a provided index with a provided product.
     * @param index int index of a part
     * @param newProduct Product info to replace the selected product
     */
    public static void updateProduct(int index, Product newProduct){
        allProducts.set(index,newProduct);
    }

    /**
     * deletePart removes the passed part from the allParts listed.
     * @param selectedPart Part that is removed
     * @return return true if part is in the list and removed, false if the part is not in the list
     */
    public static boolean deletePart(Part selectedPart){
        if(allParts.contains(selectedPart)){
            allParts.remove(selectedPart);
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * deletePart removes the passed product from the allProducts listed.
     * @param selectedProduct Product that is removed
     * @return return true if Product is in the list and removed, false if the product is not in the list
     */
    public static boolean deleteProduct(Product selectedProduct){
        if(allProducts.contains(selectedProduct)){
            allProducts.remove(selectedProduct);
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Getter for allParts.
     * @return Part ObservableList
     */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    /**
     * Getter for allProducts.
     * @return Product ObservableList
     */
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }
}