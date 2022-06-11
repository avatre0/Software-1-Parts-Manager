package model;

/**
* Class InHouse is an extension of Class Part
 * It extends with adding the machineId attribute
 */
public class InHouse extends Part{

    private int machineId;

    /** Constructor.
     * @param id ID of Part
     * @param machineId MachineID of Part
     * @param max Max value of Part
     * @param min Min value of Part
     * @param name Name of Part
     * @param price Price of Part
     * @param stock Inventory amount of Part */
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId){
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }

    /**
     * Setter for Machine ID.
     * @param machineId Int Machine ID
     */
    public void setMachineId(int machineId) {this.machineId = machineId;}

    /**
     * Getter for Machine ID.
     * @return int machineID
     */
    public int getMachineId() {return machineId;}
}
