package model;

/**
 * Created by chadirahme on 2020-05-26.
 */
public class VATCodeModel {

    private int vatKey;
    private String vatCode;
    private String vatDescription;
    private String vatType;
    private String isActive;
    private double vatPercentage;
    private double vatCalcualtedFromAmt;
    private int vatServiceItem;


    public VATCodeModel(){

    }

    public VATCodeModel(int vatKey,int vatServiceItem,double vatPercentage,double vatCalcualtedFromAmt){
        this.vatKey=vatKey;
        this.vatServiceItem=vatServiceItem;
        this.vatPercentage=vatPercentage;
        this.vatCalcualtedFromAmt=vatCalcualtedFromAmt;
    }

    public int getVatKey() {
        return vatKey;
    }

    public void setVatKey(int vatKey) {
        this.vatKey = vatKey;
    }

    public String getVatCode() {
        return vatCode;
    }

    public void setVatCode(String vatCode) {
        this.vatCode = vatCode;
    }

    public String getVatDescription() {
        return vatDescription;
    }

    public void setVatDescription(String vatDescription) {
        this.vatDescription = vatDescription;
    }

    public String getVatType() {
        return vatType;
    }

    public void setVatType(String vatType) {
        this.vatType = vatType;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public double getVatPercentage() {
        return vatPercentage;
    }

    public void setVatPercentage(double vatPercentage) {
        this.vatPercentage = vatPercentage;
    }

    public double getVatCalcualtedFromAmt() {
        return vatCalcualtedFromAmt;
    }

    public void setVatCalcualtedFromAmt(double vatCalcualtedFromAmt) {
        this.vatCalcualtedFromAmt = vatCalcualtedFromAmt;
    }

    public int getVatServiceItem() {
        return vatServiceItem;
    }

    public void setVatServiceItem(int vatServiceItem) {
        this.vatServiceItem = vatServiceItem;
    }

}
