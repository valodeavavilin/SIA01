package org.j4di.visualisation.olap.analytical.views.adv_txn_risk_statistics;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ADV_TXN_RISK_STATISTICS_V(

        @JsonAlias({"sourceSystem", "source_system"})
        String sourceSystem,

        @JsonAlias({"txnCount", "txn_count"})
        Long txnCount,

        @JsonAlias({"totalAmount", "total_amount"})
        Double totalAmount,

        @JsonAlias({"avgAmount", "avg_amount"})
        Double avgAmount,

        @JsonAlias({"minAmount", "min_amount"})
        Double minAmount,

        @JsonAlias({"maxAmount", "max_amount"})
        Double maxAmount,

        @JsonAlias({"amountVarianceSample", "amount_variance_sample"})
        Double amountVarianceSample,

        @JsonAlias({"amountVariancePopulation", "amount_variance_population"})
        Double amountVariancePopulation,

        @JsonAlias({"amountStddevSample", "amount_stddev_sample"})
        Double amountStddevSample,

        @JsonAlias({"amountStddevPopulation", "amount_stddev_population"})
        Double amountStddevPopulation,

        @JsonAlias({"corrCreditScoreAmount", "corr_credit_score_amount"})
        Double corrCreditScoreAmount,

        @JsonAlias({"corrYearlyIncomeAmount", "corr_yearly_income_amount"})
        Double corrYearlyIncomeAmount,

        @JsonAlias({"corrTotalDebtAmount", "corr_total_debt_amount"})
        Double corrTotalDebtAmount
) {
}