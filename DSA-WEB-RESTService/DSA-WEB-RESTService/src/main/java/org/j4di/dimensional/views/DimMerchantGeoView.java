package org.j4di.dimensional.views;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

@Getter
@Entity
@Immutable
@Table(name = "DIM_MERCHANT_GEO_V")
public class DimMerchantGeoView {

    @Id
    @Column(name = "merchant_id")
    private Long merchantId;

    @Column(name = "merchant_state")
    private String merchantState;

    @Column(name = "merchant_city")
    private String merchantCity;

    @Column(name = "zip")
    private String zip;

    @Column(name = "state_group")
    private String stateGroup;

    @Column(name = "city_group")
    private String cityGroup;

    @Column(name = "geo_hierarchy")
    private String geoHierarchy;
}