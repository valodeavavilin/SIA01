package org.j4di.access.views.xlsx;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

@Getter
@Entity
@Immutable
@Table(name = "CUSTOMER_FINANCE_VIEW")
public class CustomerFinanceView {

    @Id
    @Column(name = "clientId")
    private Long clientId;

    @Column(name = "perCapitaIncome")
    private Double perCapitaIncome;

    @Column(name = "yearlyIncome")
    private Double yearlyIncome;

    @Column(name = "totalDebt")
    private Double totalDebt;

    @Column(name = "creditScore")
    private Integer creditScore;

    @Column(name = "numCreditCards")
    private Integer numCreditCards;
}