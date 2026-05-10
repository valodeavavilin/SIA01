package org.j4di.visualisation.olap.analytical.views.merchant_state_rank;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record WV_MERCHANT_STATE_RANK_V(

        @JsonAlias({"merchantId", "merchant_id"})
        Long merchantId,

        @JsonAlias({"stateGroup", "state_group"})
        String stateGroup,

        @JsonAlias({"cityGroup", "city_group"})
        String cityGroup,

        @JsonAlias({"totalAmount", "total_amount"})
        Double totalAmount,

        @JsonAlias({"txnCount", "txn_count"})
        Long txnCount,

        @JsonAlias({"rankInState", "rank_in_state"})
        Long rankInState,

        @JsonAlias({"denseRankInState", "dense_rank_in_state"})
        Long denseRankInState,

        @JsonAlias({"rowNumberInState", "row_number_in_state"})
        Long rowNumberInState
) {
}