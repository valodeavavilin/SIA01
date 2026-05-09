package org.j4di.analytical.views;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

@Getter
@Entity
@Immutable
@IdClass(OlapTxnStateBrandCubeViewId.class)
@Table(name = "OLAP_VIEW_TXN_STATE_BRAND_CUBE_V")
public class OlapTxnStateBrandCubeView {

    @Id
    @Column(name = "state_group")
    private String stateGroup;

    @Id
    @Column(name = "card_brand")
    private String cardBrand;

    @Column(name = "total_amount")
    private Double totalAmount;

    @Column(name = "txn_count")
    private Long txnCount;
}