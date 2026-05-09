package org.j4di.analytical.views;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

@Getter
@Entity
@Immutable
@Table(name = "ADV_TXN_RISK_STATISTICS_V")
public class AdvTxnRiskStatisticsView {

    @Id
    @Column(name = "source_system")
    private String sourceSystem;

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

    @Column(name = "amount_variance_sample")
    private Double amountVarianceSample;

    @Column(name = "amount_variance_population")
    private Double amountVariancePopulation;

    @Column(name = "amount_stddev_sample")
    private Double amountStddevSample;

    @Column(name = "amount_stddev_population")
    private Double amountStddevPopulation;

    @Column(name = "corr_credit_score_amount")
    private Double corrCreditScoreAmount;

    @Column(name = "corr_yearly_income_amount")
    private Double corrYearlyIncomeAmount;

    @Column(name = "corr_total_debt_amount")
    private Double corrTotalDebtAmount;
}