package org.j4di.analytical.views;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

@Getter
@Entity
@Immutable
@IdClass(WvCreditMonthPerformanceViewId.class)
@Table(name = "WV_CREDIT_MONTH_PERFORMANCE_V")
public class WvCreditMonthPerformanceView {

    @Id
    @Column(name = "txn_year")
    private Integer txnYear;

    @Id
    @Column(name = "txn_month")
    private Integer txnMonth;

    @Id
    @Column(name = "credit_score_group")
    private String creditScoreGroup;

    @Column(name = "total_amount")
    private Double totalAmount;

    @Column(name = "txn_count")
    private Long txnCount;

    @Column(name = "avg_total_per_score_group")
    private Double avgTotalPerScoreGroup;

    @Column(name = "total_month_amount")
    private Double totalMonthAmount;

    @Column(name = "pct_of_month_total")
    private Double pctOfMonthTotal;
}