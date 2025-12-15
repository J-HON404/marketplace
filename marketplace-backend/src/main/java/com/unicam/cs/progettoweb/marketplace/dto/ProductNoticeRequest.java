package com.unicam.cs.progettoweb.marketplace.dto;

import com.unicam.cs.progettoweb.marketplace.model.enums.TypeProductNotice;
import java.time.LocalDate;

public class ProductNoticeRequest {
    public String text;
    public TypeProductNotice type;
    public LocalDate expireDate;
}
