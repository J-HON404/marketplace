package com.unicam.cs.progettoweb.marketplace.model.customer;

import com.unicam.cs.progettoweb.marketplace.model.account.Profile;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@Table(name = "customers")
public class Customer extends Profile {


}
