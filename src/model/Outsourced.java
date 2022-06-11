package model;

/**
 * Class Outsourced is an extension of Class Part
 * It adds the attribute company name
 */
public class Outsourced extends Part{

    private String companyName;

    /** Constructor.
     * @param id ID of Part
     * @param companyName Name of company which OutSources the Part
     * @param max Max value of Part
     * @param min Min value of Part
     * @param name Name of Part
     * @param price Price of Part
     * @param stock Inventory amount of Part */
    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName){
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }


    /** setter for CompanyName.
     * @param companyName Name of company which outsources the part
     */
    public void setCompanyName(String companyName) {this.companyName = companyName;}

    /** getter for companyName.
     * @return Returns the companyName
     */
    public String getCompanyName() {return companyName;}
}
