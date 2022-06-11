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
import model.Inventory;
import model.Part;
import model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * AddProduct class is the controller for the Add Product screen.
 */
public class AddProduct implements Initializable {

    //FXML FX ID's
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
    @FXML public TextField productName;
    @FXML public TextField productInv;
    @FXML public TextField productPrice;
    @FXML public TextField productMax;
    @FXML public TextField productMin;
    @FXML public TextField partSearchText;

    Stage stage;
    Parent scene;

    ObservableList<Part> selectedParts = FXCollections.observableArrayList(); //empty parts list to add to selected parts

    /**
     * initialize method initializes the Add Product screen.
     * It populates the parts and sets the values on the tables.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        allPartsTable.setItems(Inventory.getAllParts());//gets all parts and adds them to the all parts table

        //sets the colum vales to part values
        colAllPartId.setCellValueFactory(new PropertyValueFactory<>("id")); //id
        colAllPartName.setCellValueFactory(new PropertyValueFactory<>("name")); // name
        colAllPartInventory.setCellValueFactory(new PropertyValueFactory<>("stock")); //inventory amount
        colAllPartPrice.setCellValueFactory(new PropertyValueFactory<>("price")); // price

        colSelectedPartId.setCellValueFactory(new PropertyValueFactory<>("id")); // id
        colSelectedPartsName.setCellValueFactory(new PropertyValueFactory<>("name")); //name
        colSelectedPartsInventory.setCellValueFactory(new PropertyValueFactory<>("stock"));//inventory amount
        colSelectedPartsPrice.setCellValueFactory(new PropertyValueFactory<>("price"));//price
    }

    /**
     * onCancel method check if you want to exit the screen before returning to the main page.
     * @param actionEvent button press cancel
     * */
    @FXML void onCancel(ActionEvent actionEvent) throws IOException {
        if(Main.cancelMessage()){
            returnToMain(actionEvent);
        }
    }

    /**
     * onSave reads the text fields and the parts selected in the selected parts table.
     * @param actionEvent button press save
     */
    @FXML void onSave(ActionEvent actionEvent) throws IOException {
        int id = 0; // init id to 0
        Product newProduct; // new empty product
        for (Product product : Inventory.getAllProducts()){ //loops through all products and finds the highest ID number
            if(product.getId() >= id){
                id = product.getId()+1; // adds one
            }
        }

        // try block to create new product
        try{
            String name = productName.getText(); // name text
            int inv = Integer.parseInt(productInv.getText());// inventory amount text to int
            double price = Double.parseDouble(productInv.getText()); // price text to double
            int max = Integer.parseInt(productMax.getText()); // max amount text to int
            int min = Integer.parseInt(productMin.getText()); // min amount text to int

            //Max needs to be larger then min
            if (max < min ){
                Main.errorMessage("Error","Minimum must have a value less then maximum");
            }

            //max needs to larger than inv
            //min needs to be smaller then inv
            if (inv > max || inv < min){
                Main.errorMessage("Error","Inventory must be lower then Maximum and Higher then Minimum");
            }
            // if everything checks out create a new product
            newProduct = new Product(id,name,price,inv,min,max);
            //adds all selected parts to the new product
            for(Part part : selectedParts) {
                newProduct.addAssociatedPart(part);
            }
            //adds the new product to the inventory
            Inventory.addProduct(newProduct);
            returnToMain(actionEvent); //return to main screen

        }catch (NumberFormatException e) //catch for if wrong type of text is tried to convert to int/double
        {
            Main.errorMessage("Alert","Please enter correct values");
        }
    }

    /**
     * addPart method adds selected part from the all part's table to the selected parts table.
     * @param actionEvent add button pressed
     */
    @FXML public void addPart(ActionEvent actionEvent) {
        Part part = allPartsTable.getSelectionModel().getSelectedItem(); // part selected from the table
        if(part != null) { // is a part selected
            selectedParts.add(part); // if yes then add the part to the selected part's table
            selectedPartsTable.setItems(selectedParts); // refresh the selected part's table
        }else{ // no part selected show an error
            Main.errorMessage("Error","Please select a part to add");
        }
    }

    /**
     * removePart method removes the selected part from the selectedPartTable
     * @param actionEvent remove part button clicked
     */
    @FXML public void removePart(ActionEvent actionEvent) {
        Part part = selectedPartsTable.getSelectionModel().getSelectedItem(); // get selected part
        if(part != null){ // is part selected
            if(confirmRemove("part")) { // if yes confirm the remove
                selectedParts.remove(part); // remove the part from the table
                selectedPartsTable.setItems(selectedParts); // refresh the table
            }
        }else{ // no part selected show an error
            Main.errorMessage("Error","Please select a part to remove");
        }
    }

    /**
     * searchPart method runs when text is entered into the part search box.
     * It limits the allParts table to what is searched for.
     * @param actionEvent text entered into the box
     */
    @FXML public void searchPart(ActionEvent actionEvent) {
        String partSearch = partSearchText.getText().trim(); // reads text from search
        if(partSearch.matches("")){ // if search is empty
            allPartsTable.setItems(Inventory.getAllParts()); // refresh table
        }
        else if(Main.isNumeric(partSearch)){ // if the search term is a number
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
}