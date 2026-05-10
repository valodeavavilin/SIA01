package org.j4di.visualisation.olap.fact.views.transactions;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record FACT_TRANSACTIONS_ENRICHED_V(

        @JsonAlias({"txnId", "txn_id"})
        Long txnId,

        @JsonAlias({"txnTimestamp", "txn_timestamp"})
        String txnTimestamp,

        @JsonAlias({"txnDate", "txn_date"})
        String txnDate,

        @JsonAlias({"clientId", "client_id"})
        Long clientId,

        @JsonAlias({"cardId", "card_id"})
        Long cardId,

        @JsonAlias({"merchantId", "merchant_id"})
        Long merchantId,

        Double amount,

        Long mcc,

        @JsonAlias({"useChip", "use_chip"})
        String useChip,

        String channel,

        String errors,

        @JsonAlias({"sourceSystem", "source_system"})
        String sourceSystem,

        String gender,

        @JsonAlias({"ageGroup", "age_group"})
        String ageGroup,

        @JsonAlias({"creditScoreGroup", "credit_score_group"})
        String creditScoreGroup,

        @JsonAlias({"incomeGroup", "income_group"})
        String incomeGroup,

        @JsonAlias({"debtGroup", "debt_group"})
        String debtGroup,

        @JsonAlias({"cardBrand", "card_brand"})
        String cardBrand,

        @JsonAlias({"cardType", "card_type"})
        String cardType,

        @JsonAlias({"creditLimitGroup", "credit_limit_group"})
        String creditLimitGroup,

        @JsonAlias({"chipGroup", "chip_group"})
        String chipGroup,

        @JsonAlias({"darkwebGroup", "darkweb_group"})
        String darkwebGroup,

        @JsonAlias({"stateGroup", "state_group"})
        String stateGroup,

        @JsonAlias({"cityGroup", "city_group"})
        String cityGroup,

        @JsonAlias({"geoHierarchy", "geo_hierarchy"})
        String geoHierarchy,

        @JsonAlias({"txnYear", "txn_year"})
        Integer txnYear,

        @JsonAlias({"txnMonth", "txn_month"})
        Integer txnMonth,

        @JsonAlias({"txnDay", "txn_day"})
        Integer txnDay,

        @JsonAlias({"txnQuarter", "txn_quarter"})
        Integer txnQuarter,

        @JsonAlias({"yearMonth", "year_month"})
        String yearMonth,

        @JsonAlias({"monthName", "month_name"})
        String monthName,

        @JsonAlias({"weekdayName", "weekday_name"})
        String weekdayName
) {
}