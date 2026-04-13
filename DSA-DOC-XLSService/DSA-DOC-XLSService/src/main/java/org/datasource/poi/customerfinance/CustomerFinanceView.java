package org.datasource.poi.customerfinance;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class CustomerFinanceView {
    private Long clientId;
    private Double perCapitaIncome;
    private Double yearlyIncome;
    private Double totalDebt;
    private Integer creditScore;
    private Integer numCreditCards;
}