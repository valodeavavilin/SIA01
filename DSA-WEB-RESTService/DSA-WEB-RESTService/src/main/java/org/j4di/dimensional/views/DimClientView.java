package org.j4di.dimensional.views;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

@Getter
@Entity
@Immutable
@Table(name = "DIM_CLIENT_V")
public class DimClientView {

    @Id
    @Column(name = "client_id")
    private Long clientId;

    @Column(name = "gender")
    private String gender;

    @Column(name = "current_age")
    private Integer currentAge;

    @Column(name = "retirement_age")
    private Integer retirementAge;

    @Column(name = "birth_year")
    private Integer birthYear;

    @Column(name = "birth_month")
    private Integer birthMonth;

    @Column(name = "address")
    private String address;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "per_capita_income")
    private Double perCapitaIncome;

    @Column(name = "yearly_income")
    private Double yearlyIncome;

    @Column(name = "total_debt")
    private Double totalDebt;

    @Column(name = "credit_score")
    private Integer creditScore;

    @Column(name = "num_credit_cards")
    private Integer numCreditCards;

    @Column(name = "tx_count")
    private Long txCount;

    @Column(name = "risk_total_amount")
    private Double riskTotalAmount;

    @Column(name = "top_mcc")
    private Long topMcc;

    @Column(name = "age_group")
    private String ageGroup;

    @Column(name = "credit_score_group")
    private String creditScoreGroup;

    @Column(name = "income_group")
    private String incomeGroup;

    @Column(name = "debt_group")
    private String debtGroup;
}