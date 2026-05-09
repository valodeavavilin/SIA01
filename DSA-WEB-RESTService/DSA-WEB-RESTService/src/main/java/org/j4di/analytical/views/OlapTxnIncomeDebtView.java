package org.j4di.analytical.views;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

@Getter
@Entity
@Immutable
@IdClass(OlapTxnIncomeDebtViewId.class)
@Table(name = "OLAP_VIEW_TXN_INCOME_DEBT_V")
public class OlapTxnIncomeDebtView {

    @Id
    @Column(name = "income_group")
    private String incomeGroup;

    @Id
    @Column(name = "debt_group")
    private String debtGroup;

    @Column(name = "total_amount")
    private Double totalAmount;

    @Column(name = "txn_count")
    private Long txnCount;
}