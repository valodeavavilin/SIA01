package org.j4di.visualisation.olap.analytical.views.adv_txn_amount_distribution;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ADV_TXN_AMOUNT_DISTRIBUTION_V(

        @JsonAlias({"sourceSystem", "source_system"})
        String sourceSystem,

        @JsonAlias({"cardBrand", "card_brand"})
        String cardBrand,

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

        @JsonAlias({"q1Amount", "q1_amount"})
        Double q1Amount,

        @JsonAlias({"medianAmount", "median_amount"})
        Double medianAmount,

        @JsonAlias({"q3Amount", "q3_amount"})
        Double q3Amount,

        @JsonAlias({"stddevAmount", "stddev_amount"})
        Double stddevAmount
) {
}