package org.j4di.analytical.views;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

@Getter
@Entity
@Immutable
@IdClass(OlapTxnCardBrandTypeViewId.class)
@Table(name = "OLAP_VIEW_TXN_CARD_BRAND_TYPE_V")
public class OlapTxnCardBrandTypeView {

    @Id
    @Column(name = "card_brand")
    private String cardBrand;

    @Id
    @Column(name = "card_type")
    private String cardType;

    @Column(name = "total_amount")
    private Double totalAmount;

    @Column(name = "txn_count")
    private Long txnCount;
}