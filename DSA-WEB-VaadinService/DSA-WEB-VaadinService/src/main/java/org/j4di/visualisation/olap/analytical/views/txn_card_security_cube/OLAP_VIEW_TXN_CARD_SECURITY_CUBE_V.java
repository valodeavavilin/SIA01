package org.j4di.visualisation.olap.analytical.views.txn_card_security_cube;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OLAP_VIEW_TXN_CARD_SECURITY_CUBE_V(

        @JsonAlias({"chipGroup", "chip_group"})
        String chipGroup,

        @JsonAlias({"darkwebGroup", "darkweb_group"})
        String darkwebGroup,

        @JsonAlias({"totalAmount", "total_amount"})
        Double totalAmount,

        @JsonAlias({"txnCount", "txn_count"})
        Long txnCount,

        @JsonAlias({"avgAmount", "avg_amount"})
        Double avgAmount
) {
}