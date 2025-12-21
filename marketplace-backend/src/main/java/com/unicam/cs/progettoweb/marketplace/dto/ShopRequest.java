package com.unicam.cs.progettoweb.marketplace.dto;

import com.unicam.cs.progettoweb.marketplace.model.enums.ShopCategory;
import lombok.Data;

@Data
public class ShopRequest {
    private String name;
    private ShopCategory category;
}
