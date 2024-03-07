package com.mcalvaro.classes;

import java.util.Objects;

public class ProductClass {

    private final int productId;

    private final String name;

    private final double price;

    private final boolean status;

    public ProductClass(int productId, String name, double price, boolean status) {

        this.productId = productId;
        this.name = name;
        this.price = price;
        this.status = status;
    }

    public int getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public boolean isStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ProductClass that = (ProductClass) o;
        return productId == that.productId && Double.compare(price, that.price) == 0 && status == that.status
                && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, name, price, status);
    }

    @Override
    public String toString() {
        return "ProductClass{" +
                "productId=" + productId +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", status=" + status +
                '}';
    }
}
