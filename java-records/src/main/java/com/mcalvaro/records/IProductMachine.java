package com.mcalvaro.records;
 
public interface IProductMachine {
 
    void printCountryMachine();

    // // TODO: default methods
    default void printYearMachine() {
        System.out.println("BOLIVIA - 2024");
    }
}
