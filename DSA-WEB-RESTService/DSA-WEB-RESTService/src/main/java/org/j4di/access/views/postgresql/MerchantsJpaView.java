package org.j4di.access.views.postgresql;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

@Getter
@Entity
@Immutable
@Table(name = "merchants_jpa_view")
public class MerchantsJpaView {

    @Id
    @Column(name = "merchantId")
    private Long merchantId;

    @Column(name = "merchantCity")
    private String merchantCity;

    @Column(name = "merchantState")
    private String merchantState;

    @Column(name = "zip")
    private String zip;
}