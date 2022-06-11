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
 * Mod Part Class is the controller for the mod part screen.
 */
public class ModPart implements Initializable {

    //FXML fx ids
    @FXML public ToggleGroup partToggle;
    @FXML public TextField modId;
    @FXML public TextField modName;
    @FXML public TextField modInv;
    @FXML public TextField modPrice;
    @FXML public TextField modMax;
    @FXML public TextField modMin;
    @FXML public RadioButton inHouseSelected;
    @FXML public TextField modMachineIdText;
    @FXML public RadioButton outsourceSelected;
    @FXML public Label modMachineIdLabel;

    int partIndex; //incoming part number
    Stage stage;
    Parent scene;

    /**
     *initialize method initializes the Mod part page.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    /**
     * incomingPart method accepts a part from and sets the corresponding text fields to the part's value.
     * @param part to get values from to populate the page
     */
    public void incomingPart(Part part){
        partIndex = Inventory.getAllParts().indexOf(part); // sets the partIndex numberso we know what part to mod
        if(part instanceof InHouse){ // is this a InHouse part?
            inHouseSelected.setSelected(true); // Yes set the radio button to index
            outsourceSelected.setSelected(false);// and set the outsourced button to not selected
            inHouseRadio(); // changes the label to in house
            modMachineIdText.setText(Integer.toString(((InHouse) part).getMachineId())); //retrieves the machine id
        }
        else if (part instanceof Outsourced){ // is this a outsourced part?
            outsourceSelected.setSelected(true); // yes select the outsourced radio button
            inHouseSelected.setSelected(false); // unselect the in house radio button
            outSourcedRadio(); // sets the label to outsourced
            modMachineIdText.setText(((Outsourced) part).getCompanyName()); // retrieves the company name
        }

        modId.setText(Integer.toString(part.getId())); // sets the id
        modName.setText(part.getName()); // sets the name
        modInv.setText(Integer.toString(part.getStock())); // sets the inventory amount
        modPrice.setText(Double.toString(part.getPrice())); // sets the price
        modMax.setText(Integer.toString(part.getMax())); // sets the max amount
        modMin.setText(Integer.toString(part.getMin())); // sets the min amount
    }

    /**
     * onSave method saves the changes to the part.
     * RUNTIME ERROR the save function was overwriting the wrong part. Issue was caused by id being set to partIndex not
     * the ID that was in the text box.
     * @param actionEvent button press save
     * @throws IOException
     */
    @FXML public void onSave(ActionEvent actionEvent) throws IOException {
        //try block to check if correct text is entered
        try{
            int id = Integer.parseInt(modId.getText()); // id from text box
            String name = modName.getText(); // name from text box
            int inv = Integer.parseInt(modInv.getText()); // inventory from text box
            double price = Double.parseDouble(modPrice.getText()); // price from text box
            int max = Integer.parseInt(modMax.getText()); // max from text
            int min = Integer.parseInt(modMin.getText());// min from text box
            String machineID = modMachineIdText.getText(); // machineID from text

            //max needs to be greater then min
            if (max < min){
                Main.errorMessage("Error", "Minimum must have a value less than maximum");
                return;
            }

            // max needs to be greater than inv
            // and ivn needs to be greater then min
            if (inv > max || inv < min){
                Main.errorMessage("Error", "Inventory must be lower then Maximum and Higher then Minimum");
                return;
            }

            // is the inHouse radio selected
            if (inHouseSelected.isSelected()){
                // yes InHouse part
                InHouse newPart = new InHouse(id,name,price,inv,min,max,Integer.parseInt(machineID));
                Inventory.updatePart(partIndex,newPart);
            }
            else{
                //outsourced is selected create new outsourced product
                Outsourced newPart = new Outsourced(id,name,price,inv,min,max,machineID);
                Inventory.updatePart(partIndex,newPart);
            }
            returnToMain(actionEvent); //return to main
        }
        catch (NumberFormatException e){ // catch for if the wrong text was typed into the text boxes
            e.printStackTrace();
            Main.errorMessage("Alert", "Please enter correct values");
        }
    }

    /**
     * onCancel method asks the user if they would like to exit the screen and return to main.
     * @param actionEvent button click passed to a return to main function
     */
    @FXML void onCancel(ActionEvent actionEvent) throws IOException {
        if(Main.cancelMessage()){
            returnToMain(actionEvent);
        }
    }

    /**
     * inHouseRadio method sets the label to "Machine ID".
     */
    @FXML public void inHouseRadio() {modMachineIdLabel.setText("Machine ID");}

    /**
     * outSourcedRadio method sets the label to "Company Name".
     */
    @FXML public void outSourcedRadio() {modMachineIdLabel.setText("Company Name");}

    /**
     * returnToMain method opens the main inventory page.
     * @param actionEvent is a button event to return to main
     * @throws IOException
     */
    @FXML public void returnToMain(ActionEvent actionEvent) throws IOException{
        stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/Main.fxml")));
        stage.setScene(new Scene(scene));
        stage.show();
    }
}