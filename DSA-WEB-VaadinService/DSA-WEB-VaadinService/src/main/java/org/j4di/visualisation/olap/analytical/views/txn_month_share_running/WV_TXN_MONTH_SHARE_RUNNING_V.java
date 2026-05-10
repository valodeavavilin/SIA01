package org.j4di.visualisation.olap.analytical.views.txn_month_share_running;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record WV_TXN_MONTH_SHARE_RUNNING_V(

        @JsonAlias({"txnYear", "txn_year"})
        Integer txnYear,

        @JsonAlias({"txnMonth", "txn_month"})
        Integer txnMonth,

        @JsonAlias({"txnTimestamp", "txn_timestamp"})
        String txnTimestamp,

        @JsonAlias({"txnId", "txn_id"})
        Long txnId,

        @JsonAlias({"clientId", "client_id"})
        Long clientId,

        Double amount,

        @JsonAlias({"totalMonthAmount", "total_month_amount"})
        Double totalMonthAmount,

        @JsonAlias({"pctOfMonthTotal", "pct_of_month_total"})
        Double pctOfMonthTotal,

        @JsonAlias({"runningTotalMonth", "running_total_month"})
        Double runningTotalMonth
) {
}