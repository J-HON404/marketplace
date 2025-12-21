package com.unicam.cs.progettoweb.marketplace.dto.product;

import java.time.LocalDate;

public class ProductRequest {
    public String name;
    public String description;
    public double price;
    public int quantity;
    public LocalDate availabilityDate;

}