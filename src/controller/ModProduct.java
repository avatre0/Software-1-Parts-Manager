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
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Class ModProduct is the controller for the ModProduct Page.
 */
public class ModProduct implements Initializable {

    //FXML FX IDs
    @FXML public TableView<Part> allPartsTable;
    @FXML public TableColumn<Part,Integer> colAllPartId;
    @FXML public TableColumn<Part,String> colAllPartName;
    @FXML public TableColumn<Part,Integer> colAllPartInventory;
    @FXML public TableColumn<Part,Double> colAllPartPrice;
    @FXML public TableView<Part> selectedPartsTable;
    @FXML public TableColumn<Part,Integer> colSelectedPartId;
    @FXML public TableColumn<Part,String> colSelectedPartsName;
    @FXML public TableColumn<Part,Integer> colSelectedPartsInventory;
    @FXML public TableColumn<Part,Double> colSelectedPartsPrice;
    @FXML public TextField id;
    @FXML public TextField productName;
    @FXML public TextField productInv;
    @FXML public TextField productPrice;
    @FXML public TextField productMax;
    @FXML public TextField productMin;
    @FXML public TextField partSearchText;

    Stage stage;
    Parent scene;

    int productIndex; // index of part selected
    ObservableList<Part> selectedParts = FXCollections.observableArrayList(); //empty parts list to add to selected parts

    /**
     * initialize method initializes the page.
     * It sets the two tables and table columns to the correct values.
     * RUNTIME ERROR was corrected when adding parts to the selected parts table. Parts were not showing up in the table when added.
     * the code was copied from the allParts table and was not updated to point to the selectedParts table so that when the
     * add button was pressed the table was not updated visually.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        allPartsTable.setItems(Inventory.getAllParts()); // puts all the parts into the all parts table

        colAllPartId.setCellValueFactory(new PropertyValueFactory<>("id")); // part id
        colAllPartName.setCellValueFactory(new PropertyValueFactory<>("name")); // part name
        colAllPartInventory.setCellValueFactory(new PropertyValueFactory<>("stock")); //part inventory
        colAllPartPrice.setCellValueFactory(new PropertyValueFactory<>("price")); // part price

        colSelectedPartId.setCellValueFactory(new PropertyValueFactory<>("id")); // part id
        colSelectedPartsName.setCellValueFactory(new PropertyValueFactory<>("name")); // part name
        colSelectedPartsInventory.setCellValueFactory(new PropertyValueFactory<>("stock")); // part inventory
        colSelectedPartsPrice.setCellValueFactory(new PropertyValueFactory<>("price")); // part price
    }

    /**
     * onCancel asks user if they want to exit the page.
     * @param actionEvent button event cancel button
     */
    @FXML void onCancel(ActionEvent actionEvent) throws IOException {
        if(Main.cancelMessage()){
            returnToMain(actionEvent);
        }
    }

    /**
     * onSave saves changes to the selected product.
     * @param actionEvent save button press
     */
    @FXML void onSave(ActionEvent actionEvent) throws IOException {
        Product newProduct; // new empty product
        // try block to catch wrong number conversion
        try{
            int modId = Integer.parseInt(id.getText()); // id
            String name = productName.getText(); // name from the text box
            int inv = Integer.parseInt(productInv.getText()); // inventory from the text box
            double price = Double.parseDouble(productInv.getText()); // price from the text box
            int max = Integer.parseInt(productMax.getText()); // max from the text box
            int min = Integer.parseInt(productMin.getText()); // min from the text box

            // max needs to be larger then min
            if (max < min ){
                Main.errorMessage("Error","Minimum must have a value less then maximum");
            }

            // inv needs to be smaller than max
            // min needs to be larger than inv
            if (inv > max || inv < min){
                Main.errorMessage("Error","Inventory must be lower then Maximum and Higher then Minimum");
            }
            //create a new product with info from the page
            newProduct = new Product(modId,name,price,inv,min,max);
            //loops through all the selected parts
            for(Part part : selectedParts) {
                newProduct.addAssociatedPart(part); // then adds them to the new product
            }
            Inventory.updateProduct(productIndex,newProduct); //adds that product to the inventory
            returnToMain(actionEvent);

        }catch (NumberFormatException e) // catch for a text to number conversion error
        {
            Main.errorMessage("Alert","Please enter correct values");
        }
    }

    /**
     * addPart method adds the part selected from the allParts table to the selectedItems table.
     * @param actionEvent add part button selected
     */
    @FXML public void addPart(ActionEvent actionEvent) {
        Part part = allPartsTable.getSelectionModel().getSelectedItem(); //selected part from table
        if(part != null) { // is the part empty?
            selectedParts.add(part); // no add the part to the selected parts list
            selectedPartsTable.setItems(selectedParts); // refresh the selected parts table
        }else{ // no part selected display an error to select a part
            Main.errorMessage("Error", "Please select a part to add.");
        }
    }

    /**
     * removePart method activates on the remove part button press.
     * it removes the selected part from the selected part table.
     * @param actionEvent remove part button press
     */
    @FXML public void removePart(ActionEvent actionEvent) {
        Part part = selectedPartsTable.getSelectionModel().getSelectedItem();
        if(part != null){
            if(confirmRemove("part")) {
                selectedParts.remove(part);
                selectedPartsTable.setItems(selectedParts);
            }
        }else{
            Main.errorMessage("Error","Please select a part to remove");
        }
    }

    /**
     * searchPart method runs when text is entered into the part search box.
     * It limits the allParts table to what is searched for.
     * RUNTIME ERROR was corrected by updating the code copied from the Main class to point to the correct table.
     * @param actionEvent text entered into the box
     */
    @FXML public void searchPart(ActionEvent actionEvent) {
        String partSearch = partSearchText.getText().trim(); // reads text from search
        if(partSearch.matches("")){ // if search is empty
            allPartsTable.setItems(Inventory.getAllParts()); // refresh table
        }
        else if(Main.isNumeric(partSearch)){ //if the search term is a number
            int id = Integer.parseInt(partSearch); // converts the text to int
            ObservableList<Part> part = FXCollections.observableArrayList(); //Create an empty temp list
            part.add(Inventory.lookupPart(id)); // finds the part by ID and then adds it to the list
            allPartsTable.setItems(part); // show only found part
        }else { // search term is text
            allPartsTable.setItems(Inventory.lookupPart(partSearch)); // Show parts that match search term
        }
    }

    /**
     * returnToMain returns to the main page of the inventory app.
     * @param actionEvent button action to return to main
     * @throws IOException
     */
    @FXML public void returnToMain(ActionEvent actionEvent) throws IOException{

        stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/Main.fxml")));
        stage.setScene(new Scene(scene));
        stage.show();

    }

    /**
     * confirmRemove generates a alert to confirm to remove a part.
     * @param string text to edit the message
     * @return if the user selected yes or no
     */
    public static boolean confirmRemove(String string){
        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);// new yes no buttons
        ButtonType no = new ButtonType("No",ButtonBar.ButtonData.CANCEL_CLOSE);

        Alert alert = new Alert(Alert.AlertType.WARNING,"Cancel",yes,no);// new alert with the yes no buttons
        alert.setTitle("Confirm");
        alert.setContentText("Are you sure you want to remove the selected " + string);
        Optional<ButtonType> result = alert.showAndWait();
        return result.orElse(no) == yes; // did they click yes or no
    }

    /**
     * incomingProduct method accepts a part from and sets the corresponding text fields to the part's value.
     * @param product to get values from to populate the page
     */
    public void incomingProduct(Product product){

        productIndex = Inventory.getAllProducts().indexOf(product); // index of the selected product
        id.setText(Integer.toString(product.getId())); // id of the product
        productName.setText(product.getName()); // name of the product
        productInv.setText(Integer.toString(product.getStock())); // inventory of the product
        productPrice.setText(Double.toString(product.getPrice())); // price of the product
        productMax.setText(Integer.toString(product.getMax())); // max of the product
        productMin.setText(Integer.toString(product.getMin())); // min of the product
        selectedParts.addAll(product.getAllAssociatedParts()); // adds all the associated parts of the product to the selected parts observables list
        selectedPartsTable.setItems(selectedParts);// adds all the associated parts of the product to the table
    }
}