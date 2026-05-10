package org.j4di.visualisation.olap.dim.views.cards;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DIM_CARD_V(

        @JsonAlias({"cardId", "card_id"})
        Long cardId,

        @JsonAlias({"clientId", "client_id"})
        Long clientId,

        @JsonAlias({"cardBrand", "card_brand"})
        String cardBrand,

        @JsonAlias({"cardType", "card_type"})
        String cardType,

        @JsonAlias({"creditLimit", "credit_limit"})
        Double creditLimit,

        @JsonAlias({"hasChip", "has_chip"})
        Boolean hasChip,

        @JsonAlias({"cardOnDarkWeb", "card_on_dark_web"})
        Boolean cardOnDarkWeb,

        @JsonAlias({"acctOpenDate", "acct_open_date"})
        String acctOpenDate,

        String expires,

        @JsonAlias({"numCardsIssued", "num_cards_issued"})
        Long numCardsIssued,

        @JsonAlias({"yearPinLastChanged", "year_pin_last_changed"})
        Integer yearPinLastChanged,

        @JsonAlias({"creditLimitGroup", "credit_limit_group"})
        String creditLimitGroup,

        @JsonAlias({"chipGroup", "chip_group"})
        String chipGroup,

        @JsonAlias({"darkwebGroup", "darkweb_group"})
        String darkwebGroup
) {
}