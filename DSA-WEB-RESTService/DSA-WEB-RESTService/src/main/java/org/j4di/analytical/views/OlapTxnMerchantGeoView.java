package org.j4di.analytical.views;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

@Getter
@Entity
@Immutable
@IdClass(OlapTxnMerchantGeoViewId.class)
@Table(name = "OLAP_VIEW_TXN_MERCHANT_GEO_V")
public class OlapTxnMerchantGeoView {

    @Id
    @Column(name = "state_group")
    private String stateGroup;

    @Id
    @Column(name = "city_group")
    private String cityGroup;

    @Column(name = "total_amount")
    private Double totalAmount;

    @Column(name = "txn_count")
    private Long txnCount;
}