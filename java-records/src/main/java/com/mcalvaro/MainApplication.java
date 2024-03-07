package com.mcalvaro;

import com.mcalvaro.records.CategoryRecord;
import com.mcalvaro.records.PremiumProduct;
import com.mcalvaro.records.ProductRecord;

public class MainApplication {
    public static void main(String[] args) {

        ProductRecord pr1 = new ProductRecord( 1, "Corne Keyboard", 150, true );
        System.out.println(pr1);
        System.out.println(pr1.nameLowerCase());
        System.out.println(pr1.productId());

        pr1.printCountryMachine();

        //TODO: Record with optional params
        ProductRecord pr2 = new ProductRecord("Sofle Keyboard");
        System.out.println(pr2);

        //TODO: Record with optional params
        ProductRecord pr3 = new ProductRecord("Sofle Keyboard", 350.00);
        System.out.println(pr3);
        System.out.println(pr3.nameLowerCase());

        // TODO: Get attributo
        System.out.println(pr1.name());
        //

        // TODO: static methods
        ProductRecord.printYearOfProduct();
        //

        // TODO: interfaces
        pr1.printCountryMachine();
        pr1.printYearMachine();
        //

        // TODO: Nested records
        PremiumProduct pp = new PremiumProduct(12, "Helix Keyboard", new CategoryRecord(1, "Keyboards"));

        System.out.println(pp);
        
        // TODO: static attributes
        System.out.println(ProductRecord.DEFAULT_PRODUCT);

    }
}
