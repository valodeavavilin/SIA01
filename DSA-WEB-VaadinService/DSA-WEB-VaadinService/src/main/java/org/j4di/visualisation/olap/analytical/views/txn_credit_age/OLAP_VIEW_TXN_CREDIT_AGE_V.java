package org.j4di.visualisation.olap.analytical.views.txn_credit_age;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OLAP_VIEW_TXN_CREDIT_AGE_V(

        @JsonAlias({"creditScoreGroup", "credit_score_group"})
        String creditScoreGroup,

        @JsonAlias({"ageGroup", "age_group"})
        String ageGroup,

        @JsonAlias({"totalAmount", "total_amount"})
        Double totalAmount,

        @JsonAlias({"txnCount", "txn_count"})
        Long txnCount
) {
}