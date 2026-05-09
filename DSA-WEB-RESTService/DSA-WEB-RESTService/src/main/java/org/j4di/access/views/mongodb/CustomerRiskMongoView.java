package org.j4di.access.views.mongodb;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

@Getter
@Entity
@Immutable
@Table(name = "customer_risk_mongo_view")
public class CustomerRiskMongoView {

    @Id
    @Column(name = "client_id")
    private Long clientId;

    @Column(name = "credit_score")
    private Integer creditScore;

    @Column(name = "yearly_income")
    private Double yearlyIncome;

    @Column(name = "total_debt")
    private Double totalDebt;

    @Column(name = "num_credit_cards")
    private Integer numCreditCards;

    @Column(name = "tx_count")
    private Long txCount;

    @Column(name = "total_amount")
    private Double totalAmount;

    @Column(name = "top_mcc")
    private Long topMcc;
}