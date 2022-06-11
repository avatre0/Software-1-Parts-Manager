package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Class Main is the controller for the main inventory screen.
 * FUTURE ENHANCEMENT create ability for program to save out to file or database so that the inventory can be saved between runs.
 */
public class Main implements Initializable {

    private static boolean firstTime = true; //tracks of the test data has been entered

    Stage stage;
    Parent scene;

    //FXML id's for the main.fxml
    @FXML public TextField partSearchText;
    @FXML public TextField productSearchText;
    @FXML public TableView<Part> partsTable;
    @FXML public TableColumn<Part, Integer> colPartId;
    @FXML public TableColumn<Part, String> colPartName;
    @FXML public TableColumn<Part, Integer> colPartInventory;
    @FXML public TableColumn<Part, Double> colPartPrice;
    @FXML public TableView<Product> productsTable;
    @FXML public TableColumn<Product, Integer> colProductId;
    @FXML public TableColumn<Product, String> colProductName;
    @FXML public TableColumn<Product, Integer> colProductInventory;
    @FXML public TableColumn<Product, Double> colProductsPrice;

    /**
     *  initializes the page, sets up the test data on first run, and populates the part and product tables.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addTestData(); // adds the test data

        partsTable.setItems(Inventory.getAllParts()); //populates the parts table with the parts inventory

        colPartId.setCellValueFactory(new PropertyValueFactory<>("id")); // Sets the ID column to populate with ID's from the parts
        colPartName.setCellValueFactory(new PropertyValueFactory<>("name")); // Sets the name column to populate with names from the parts
        colPartInventory.setCellValueFactory(new PropertyValueFactory<>("stock")); // Sets the inventory amount column to populate with the inventory amount from the parts
        colPartPrice.setCellValueFactory(new PropertyValueFactory<>("price")); // Sets the price column to populate with part's prices

        productsTable.setItems(Inventory.getAllProducts()); //populates the product Table with the product inventory

        colProductId.setCellValueFactory(new PropertyValueFactory<>("id")); // Sets the id column to populate with ID's from the products
        colProductName.setCellValueFactory(new PropertyValueFactory<>("name")); // Sets the name column to populate with ID's from the products
        colProductInventory.setCellValueFactory(new PropertyValueFactory<>("stock")); // Sets the inventory amount column to populate with the inventory amount from the products
        colProductsPrice.setCellValueFactory(new PropertyValueFactory<>("price")); // Sets the price column to populate with prices from the products
    }

    /**
     * Button action that opens the Add Part screen.
     * @param actionEvent is Add part button press
     * @throws IOException
     */
    @FXML public void onPartAdd(ActionEvent actionEvent) throws IOException {
        stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AddPart.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * Button action that opens the Mod Part screen.
     * Has a catch for NullPointerExceptions on the event that the user doesn't select a part to modify.
     * @param actionEvent is mod part button press
     * @throws IOException
     */
    @FXML public void onPartMod(ActionEvent actionEvent) throws IOException {
        //Try catch block to make sure the user has selected a part to modify
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/ModPart.fxml"));
            loader.load();

            ModPart mpController = loader.getController();
            mpController.incomingPart(partsTable.getSelectionModel().getSelectedItem()); //passes the select parts values to the mod part controller
            stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }
        // catch for the case where the user doesn't select a part to modify
        // pops up an alert asking them make a selection
        catch (NullPointerException e){
            errorMessage("Error","Please make a part selection.");
        }
    }

    /**
     * Button action that deletes the selected part.
     * Has a catch for NullPointerExceptions on the event that the user doesn't select a part to delete.
     * It also will confirm the delete action.
     * FUTURE ENHANCEMENT delete the selected part's from all associated products or display an error if a part is part
     * of a product.
     * @param actionEvent is part delete button press
     */
    @FXML public void onPartDelete(ActionEvent actionEvent) {
        Part part = partsTable.getSelectionModel().getSelectedItem(); //selected part from the table
        if(part != null){ //if there is a part selected
            if(confirmDelete("part")) { //confirm delete
                Inventory.deletePart(partsTable.getSelectionModel().getSelectedItem()); //deletes the part
            }
        }else{// if a part is not selected display an error asking the user to make a selection
            errorMessage("Error","Please make a part selection.");
        }
    }

    /**
     * Button action that opens the Add Product screen.
     * @param actionEvent is product add button press
     * @throws IOException
     */
    @FXML public void onProductAdd(ActionEvent actionEvent) throws IOException {
        stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AddProduct.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * Button action that opens the Mod Product screen.
     * Has a catch for NullPointerExceptions on the event that the user doesn't select a part to modify.
     * @param actionEvent product mod button press
     * @throws IOException
     */
    @FXML public void onProductMod(ActionEvent actionEvent) throws IOException {
        //Try to pass selected product to ModProduct Controller
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/ModProduct.fxml"));
            loader.load();

            ModProduct mpController = loader.getController();
            mpController.incomingProduct(productsTable.getSelectionModel().getSelectedItem()); // pass Product to mod product controller
            stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }
        //Catch for if no product is selected, produces an alert to select a product
        catch (NullPointerException e){
            errorMessage("Error","Please make a product selection.");
        }
    }

    /**
     * Button action that deletes the selected product.
     * Has a test for no product selected.
     * It also checks if the product has associated parts.
     * It also will confirm the delete action.
     * @param actionEvent product delete button press
     */
    @FXML public void onProductDelete(ActionEvent actionEvent) {
        Product product = productsTable.getSelectionModel().getSelectedItem(); //selected product
        if(product != null){ // if there is a product selected
            if(product.getAllAssociatedParts().isEmpty()) { //if the product doesn't have parts
                if(confirmDelete("part")) { // confirms delete
                    Inventory.deleteProduct(productsTable.getSelectionModel().getSelectedItem()); //poof deleted
                }
            }else{ // alert for product having parts
                errorMessage("Error","Please remote all parts from a product to delete it.");
            }
        }else{ // alert for no selection
            errorMessage("Error","Please make a product selection.");
        }
    }

    /**
     * Button action to exit the program.
     * @param actionEvent exit button press
     */
    @FXML public void onExit(ActionEvent actionEvent) {System.exit(0);}

    /**
     * partSearch Method searches the part's table from text entered into the text box above the parts table.
     * It then will display matching parts in the parts table.
     * It can search by ID or Name ( part of a name works).
     * @param actionEvent part search box text entered
     * */
    @FXML public void partSearch(ActionEvent actionEvent) {
        String partSearch = partSearchText.getText().trim(); //text from the search box and trims the blank space if any
        if(partSearch.matches("")){ //if the box is blank
            partsTable.setItems(Inventory.getAllParts()); // fill the box with all parts from the inventory
        }
        else if(isNumeric(partSearch)){ // calls isNumeric to see if text is a number or not. If is number this runs
                int id = Integer.parseInt(partSearch); // parses string to int
                ObservableList<Part> part = FXCollections.observableArrayList(); //Create an empty temp list
                part.add(Inventory.lookupPart(id));// adds the part to the temp list by id
                partsTable.setItems(part); // display's the part in the table
        }else{ // text isnt a number
            partsTable.setItems((ObservableList<Part>) Inventory.lookupPart(partSearch)); //displays part found by the name
        }
    }

    /**
     * productSearch Method searches the part's table from text entered into the text box above the product table.
     * It then will display matching parts in the product table.
     * It can search by ID or Name ( part of a name works).
     * @param actionEvent product search box text entered
     * */
    @FXML void productSearch(ActionEvent actionEvent) {
        String productSearch = productSearchText.getText().trim(); //text from the search box and trims the blank space if any
        if(productSearch.matches("")){//if the box is blank
            productsTable.setItems(Inventory.getAllProducts());// fill the box with all products from the inventory
        }
        else if(isNumeric(productSearch)){ // is the text a number
            int id = Integer.parseInt(productSearch);// if yes converts string to int
            ObservableList<Product> product = FXCollections.observableArrayList(); //Create an empty temp list
            product.add(Inventory.lookupProduct(id));//adds the searched product by id to the temp list
            productsTable.setItems(product);//displays the list
        }else {//text is not a number
            productsTable.setItems((ObservableList<Product>) Inventory.lookupProduct(productSearch)); //displays all matching products
        }
    }

    /**
     * confirmDelete Method displays an alert to confirm an delete. This is used by onPartDelete and onProductDelete.
     * Takes in a string to change the wording of the message.
     * @param string String to change the wording of the confirmation message
     * @return Boolean true if yes false if no
     */
    public static boolean confirmDelete(String string){
        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE); //Create buttons
        ButtonType no = new ButtonType("No",ButtonBar.ButtonData.CANCEL_CLOSE);

        Alert alert = new Alert(Alert.AlertType.WARNING,"Cancel",yes,no);// new alert with yes no buttons
        alert.setTitle("Confirm");
        alert.setContentText("Are you sure you want to delete the selected " + string);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.orElse(no) == yes){
            return true; // user selected yes
        }
        return false;// user selected no
    }

    /**
     * isNumeric tests a string to see if it's a number or not.
     * @param numOrString String to test if it is a number or not
     * @return true if number false if not a number or null
     */
    public static boolean isNumeric(String numOrString){
        if (numOrString == null){ // does the string contain something
            return false;
        }
        try{ // is the string a number
            int num = Integer.parseInt(numOrString);
        }catch (NumberFormatException e){ // if error then the string is not a number
            return false; // not a number
        }
        return true; // is a number
    }

    /**
     * errorMessage method creates alerts with supplied strings.
     * @param errorTitle String for the title of the alert
     * @param errorBody String for the body of the alert
     */
    public static void errorMessage(String errorTitle, String errorBody){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(errorTitle);
        alert.setContentText(errorBody);
        alert.showAndWait();
    }

    /**
     * cancelMessage checks if the user does want to exit a page.
     * @return if the user selected yes or no
     */
    public static boolean cancelMessage(){

        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("No",ButtonBar.ButtonData.CANCEL_CLOSE);

        Alert alert = new Alert(Alert.AlertType.WARNING,"test",yes,no);
        alert.setTitle("Cancel");
        alert.setContentText("Are you sure you want to exit?");
        Optional<ButtonType> result = alert.showAndWait();
        return result.orElse(no) == yes;
    }

    /**
     * addTestData populates the Inventory on first run.
     */
    private void addTestData(){
        //Test Objects
        if(!firstTime){
            return;
        }
        firstTime = false;
        InHouse wheel = new InHouse(1,"Wheel",10.99,10,1,20,1);
        Inventory.addPart(wheel);
        Outsourced peddle = new Outsourced(2,"Peddle",10.11,20,1,30,"VeryGood");
        Inventory.addPart(peddle);

        Product bike = new Product(1,"Cool Bike", 100,12,1,20);
        bike.addAssociatedPart(wheel);
        bike.addAssociatedPart(peddle);
        Inventory.addProduct(bike);

        Product uniCycle = new Product(2,"Uni Cycle",111,10,1,20);
        Inventory.addProduct(uniCycle);
    }
}