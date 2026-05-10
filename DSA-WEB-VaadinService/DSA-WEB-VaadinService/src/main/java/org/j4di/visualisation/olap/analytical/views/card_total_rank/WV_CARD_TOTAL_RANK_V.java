package org.j4di.visualisation.olap.analytical.views.card_total_rank;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record WV_CARD_TOTAL_RANK_V(

        @JsonAlias({"cardId", "card_id"})
        Long cardId,

        @JsonAlias({"totalAmount", "total_amount"})
        Double totalAmount,

        @JsonAlias({"rankCard", "rank_card"})
        Long rankCard,

        @JsonAlias({"denseRankCard", "dense_rank_card"})
        Long denseRankCard,

        @JsonAlias({"rowNumberCard", "row_number_card"})
        Long rowNumberCard
) {
}