package org.datasource.jdbc.views.transactions;

import lombok.Value;

@Value
public class TransactionsView {
    private Long txnId;
    private String txnDate;
    private Integer clientId;
    private Integer cardId;
    private Double amount;
    private String useChip;
    private Integer merchantId;
    private Integer mcc;
    private String errors;
}