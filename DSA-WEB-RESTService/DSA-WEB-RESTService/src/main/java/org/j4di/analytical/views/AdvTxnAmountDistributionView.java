package org.j4di.analytical.views;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

@Getter
@Entity
@Immutable
@IdClass(AdvTxnAmountDistributionViewId.class)
@Table(name = "ADV_TXN_AMOUNT_DISTRIBUTION_V")
public class AdvTxnAmountDistributionView {

    @Id
    @Column(name = "source_system")
    private String sourceSystem;

    @Id
    @Column(name = "card_brand")
    private String cardBrand;

    @Column(name = "txn_count")
    private Long txnCount;

    @Column(name = "total_amount")
    private Double totalAmount;

    @Column(name = "avg_amount")
    private Double avgAmount;

    @Column(name = "min_amount")
    private Double minAmount;

    @Column(name = "max_amount")
    private Double maxAmount;

    @Column(name = "q1_amount")
    private Double q1Amount;

    @Column(name = "median_amount")
    private Double medianAmount;

    @Column(name = "q3_amount")
    private Double q3Amount;

    @Column(name = "stddev_amount")
    private Double stddevAmount;
}