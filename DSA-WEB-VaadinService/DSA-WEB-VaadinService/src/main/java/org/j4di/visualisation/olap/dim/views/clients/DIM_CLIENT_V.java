package org.j4di.visualisation.olap.dim.views.clients;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DIM_CLIENT_V(

        @JsonAlias({"clientId", "client_id"})
        Long clientId,

        String gender,

        @JsonAlias({"currentAge", "current_age"})
        Integer currentAge,

        @JsonAlias({"retirementAge", "retirement_age"})
        Integer retirementAge,

        @JsonAlias({"birthYear", "birth_year"})
        Integer birthYear,

        @JsonAlias({"birthMonth", "birth_month"})
        Integer birthMonth,

        String address,

        Double latitude,

        Double longitude,

        @JsonAlias({"perCapitaIncome", "per_capita_income"})
        Double perCapitaIncome,

        @JsonAlias({"yearlyIncome", "yearly_income"})
        Double yearlyIncome,

        @JsonAlias({"totalDebt", "total_debt"})
        Double totalDebt,

        @JsonAlias({"creditScore", "credit_score"})
        Integer creditScore,

        @JsonAlias({"numCreditCards", "num_credit_cards"})
        Integer numCreditCards,

        @JsonAlias({"txCount", "tx_count"})
        Long txCount,

        @JsonAlias({"riskTotalAmount", "risk_total_amount"})
        Double riskTotalAmount,

        @JsonAlias({"topMcc", "top_mcc"})
        Long topMcc,

        @JsonAlias({"ageGroup", "age_group"})
        String ageGroup,

        @JsonAlias({"creditScoreGroup", "credit_score_group"})
        String creditScoreGroup,

        @JsonAlias({"incomeGroup", "income_group"})
        String incomeGroup,

        @JsonAlias({"debtGroup", "debt_group"})
        String debtGroup
) {
}