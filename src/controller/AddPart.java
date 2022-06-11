package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;
import model.Part;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Add Part screen's controller
 */
public class AddPart implements Initializable {

    //FX ID names
    @FXML public ToggleGroup partToggle;
    @FXML public Label inOutLabel;
    @FXML public TextField idForm;
    @FXML public TextField addName;
    @FXML public TextField addInv;
    @FXML public TextField addPrice;
    @FXML public TextField addMax;
    @FXML public TextField addMin;
    @FXML public TextField addMachineID;
    @FXML public RadioButton inHouseSelected;
    @FXML public RadioButton outsourcedSelected;

    Stage stage;
    Parent scene;

    /**
     *method initialize initializes the add part screen.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {}

    /**
     * onCancel method is the handler for the cancel button.
     * displays a message verifying that you do want to exit the screen.
     * @param actionEvent cancel button pressed
     */
    @FXML void onCancel(ActionEvent actionEvent) throws IOException {
        if(Main.cancelMessage()){
            returnToMain(actionEvent);
        }
    }

    /**
     * onSave saves the new part's data to the inventory.
     * It also verifies that the data is correct.
     * RUNTIME ERROR part ID was not incrementing correctly. Added a +1 statement to the ID calculation to fix this issue.
     * @param actionEvent button save to save the new part
     * @throws IOException
     */
    @FXML public void onSave(ActionEvent actionEvent) throws IOException {
        int id = 0; //new ID number
        Part newPart; // New empty part
        for (Part part : Inventory.getAllParts()){ // loop checks for existing ID numbers
            if(part.getId() >= id){
                id = part.getId() + 1; // found the highest id and adds one to it
            }
        }

        //Try block to catch if string is put into the numbers sections
        try{
            //block of code gets info from the Add Part screen
            String name = addName.getText(); // part name
            int inv = Integer.parseInt(addInv.getText()); // inventory amount
            double price = Double.parseDouble(addPrice.getText()); // price
            int max = Integer.parseInt(addMax.getText());// max amount
            int min = Integer.parseInt(addMin.getText());// min amount
            String machineID = addMachineID.getText();// machineID or Company Name

            // Max has to be larger then min
            if (max < min){
                // calls an alert generator
                Main.errorMessage("Error", "Minimum must have a value less then maximum");
                return;
            }

            //Max has to be larger than the inventory
            //and inventory has to be larger the min
            if (inv > max || inv < min){
                // calls an alert generator
                Main.errorMessage("Error", "Inventory must be lower then Maximum and Higher then Minimum");
                return;
            }

            //if the in house radio button is selected
            if (inHouseSelected.isSelected()){
                //converts the machineID into a int from a string and creates a new part
                newPart = new InHouse(id,name,price,inv,min,max,Integer.parseInt(machineID));
                Inventory.addPart(newPart); //add that part to the inventory
            }
            else{ // the outsourced radio button is selected
                newPart = new Outsourced(id,name,price,inv,min,max,machineID);
                Inventory.addPart(newPart);//add part to the inventory
            }
            returnToMain(actionEvent);// returns to the main inventory screen
        }
        // catch if any of the conversions have the wrong data type supplied
        catch (NumberFormatException e){
            // calls and alert generator
            Main.errorMessage("Alert", "Please enter correct values");
        }
    }

    /**
     * If the radio button in house is selected changes the label to say "Machine ID".
     */
    @FXML public void inHouseRadio() {inOutLabel.setText("Machine ID");}

    /**
     * If the radio button outsourced is selected changes the label to say "Company Name".
     */
    @FXML public void outsourcedRadio() {inOutLabel.setText("Company Name");}

    /**
     * returnToMain method returns to the main inventory screen.
     * @param actionEvent passed from calling source, in this class button presses
     * @throws IOException
     */
    @FXML public void returnToMain(ActionEvent actionEvent) throws IOException{
        stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/Main.fxml")));
        stage.setScene(new Scene(scene));
        stage.show();
    }
}