package com.mcalvaro.records;
 
public record ProductRecord(int productId, String name, double price, boolean status) implements IProductMachine {


    // Constructor with optional data
    public ProductRecord(String name) {
        this(0,name, 250.00, true);
    }
    //

    // Constructor more params
    public ProductRecord( String name, Double price) {
        this(0, name, price, true);
    }

    // TODO: Custom functions
    public String nameLowerCase() {

        return name.toLowerCase();
    }

    // TODO: static methods
    public static void printYearOfProduct() {
        System.out.println("2024");
    }

    // TODO: implements interface
    @Override
    public void printCountryMachine() {
        System.out.println("Bolivia");
    }
    //

    // TODO: Override toString
     @Override
     public String toString() {
         return "Name: " + name;
     }
    

    // TODO: static attributes

    public static final String DEFAULT_PRODUCT = "COMPUTER";
}
