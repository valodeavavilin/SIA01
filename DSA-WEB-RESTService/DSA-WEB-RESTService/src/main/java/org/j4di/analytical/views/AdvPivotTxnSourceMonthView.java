package org.j4di.analytical.views;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

@Getter
@Entity
@Immutable
@IdClass(AdvPivotTxnSourceMonthViewId.class)
@Table(name = "ADV_PIVOT_TXN_SOURCE_MONTH_V")
public class AdvPivotTxnSourceMonthView {

    @Id
    @Column(name = "txn_year")
    private Integer txnYear;

    @Id
    @Column(name = "txn_month")
    private Integer txnMonth;

    @Column(name = "postgres_total_amount")
    private Double postgresTotalAmount;

    @Column(name = "mongo_total_amount")
    private Double mongoTotalAmount;

    @Column(name = "total_amount")
    private Double totalAmount;
}