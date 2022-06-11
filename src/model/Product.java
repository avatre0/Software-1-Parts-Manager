package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Class Product.
 */
public class Product {

    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    /** Constructor.
     * @param id int id
     * @param name string name
     * @param price double price
     * @param stock int stock/inventory
     * @param min int minimum
     * @param max int maximum
     */
    public Product(int id, String name, double price, int stock, int min, int max){
        this.id = id;
        this.name = name;
        this.stock = stock;
        this.price = price;
        this.min = min;
        this.max = max;
    }

    /**
     * addAssociatedPart method adds the passed part to the product.
     * @param part Part to add to the product
     */
    public void addAssociatedPart(Part part){
        this.associatedParts.add(part);
    }

    /**
     * deleteAssociatedPart deletes a part from the product.
     * @param selectedAssociatedPart Part to delete
     * @return bool if part was deleted or not
     */
    public boolean deleteAssociatedPart(Part selectedAssociatedPart){
        if(this.associatedParts.contains(selectedAssociatedPart)){
            this.associatedParts.remove(selectedAssociatedPart);
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * getAllAssociatedParts returns a ObservableList Part of all parts associated with the product.
     * @return ObservableList of Parts
     */
    public ObservableList<Part> getAllAssociatedParts(){
        return this.associatedParts;
    }

    /**
     * setter for ID.
     * @param id int id
     */
    public void setId(int id) {this.id = id;}

    /**
     * setter for name.
     * @param name string name
     */
    public void setName(String name) {this.name = name;}

    /**
     * setter for price.
     * @param price double price
     */
    public void setPrice(double price) {this.price = price;}

    /**
     * setter for stock.
     * @param stock int stock
     */
    public void setStock(int stock) {this.stock = stock;}

    /**
     * setter for min.
     * @param min int min
     */
    public void setMin(int min) {this.min = min;}

    /**
     * setter for max.
     * @param max int max
     */
    public void setMax(int max) {this.max = max;}

    /**
     * getter for ID.
     * @return it ID
     */
    public int getId() {return id;}

    /**
     * getter for name.
     * @return string Name
     */
    public String getName() {return name;}

    /**
     * getter for price.
     * @return double price
     */
    public double getPrice() {return price;}

    /**
     * getter for stock.
     * @return int stock
     */
    public int getStock() {return stock;}

    /**
     * getter for min.
     * @return int min
     */
    public int getMin() {return min;}

    /**
     * getter for max.
     * @return int max
     */
    public int getMax() {return max;}
}