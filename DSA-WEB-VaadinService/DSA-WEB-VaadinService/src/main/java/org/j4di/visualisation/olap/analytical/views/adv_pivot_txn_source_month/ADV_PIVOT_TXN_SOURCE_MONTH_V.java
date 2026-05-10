package org.j4di.visualisation.olap.analytical.views.adv_pivot_txn_source_month;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ADV_PIVOT_TXN_SOURCE_MONTH_V(

        @JsonAlias({"txnYear", "txn_year"})
        Integer txnYear,

        @JsonAlias({"txnMonth", "txn_month"})
        Integer txnMonth,

        @JsonAlias({"postgresTotalAmount", "postgres_total_amount"})
        Double postgresTotalAmount,

        @JsonAlias({"mongoTotalAmount", "mongo_total_amount"})
        Double mongoTotalAmount,

        @JsonAlias({"totalAmount", "total_amount"})
        Double totalAmount
) {
}