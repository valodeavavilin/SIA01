package org.datasource.jpa.views.merchants;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;

@Value
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Entity
@Table(name = "merchants", schema = "payments")
@NamedQuery(name = "MerchantJpaView.findAll", query = "SELECT m FROM MerchantJpaView m")
public class MerchantJpaView implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "merchant_id")
    private Integer merchantId;

    @Column(name = "merchant_city")
    private String merchantCity;

    @Column(name = "merchant_state")
    private String merchantState;

    @Column(name = "zip")
    private String zip;
}