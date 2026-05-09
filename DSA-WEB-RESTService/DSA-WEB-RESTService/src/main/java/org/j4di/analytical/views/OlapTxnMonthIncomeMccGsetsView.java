package org.j4di.analytical.views;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

@Getter
@Entity
@Immutable
@IdClass(OlapTxnMonthIncomeMccGsetsViewId.class)
@Table(name = "OLAP_VIEW_TXN_MONTH_INCOME_MCC_GSETS_V")
public class OlapTxnMonthIncomeMccGsetsView {

    @Id
    @Column(name = "txn_month")
    private String txnMonth;

    @Id
    @Column(name = "income_group")
    private String incomeGroup;

    @Id
    @Column(name = "mcc")
    private String mcc;

    @Column(name = "total_amount")
    private Double totalAmount;

    @Column(name = "txn_count")
    private Long txnCount;

    @Column(name = "avg_amount")
    private Double avgAmount;
}