package org.j4di.access.views.postgresql;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

@Getter
@Entity
@Immutable
@Table(name = "transactions_view")
public class TransactionsView {

    @Id
    @Column(name = "txnId")
    private Long txnId;

    @Column(name = "txnDate")
    private String txnDate;

    @Column(name = "clientId")
    private Long clientId;

    @Column(name = "cardId")
    private Long cardId;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "useChip")
    private String useChip;

    @Column(name = "merchantId")
    private Long merchantId;

    @Column(name = "mcc")
    private Long mcc;

    @Column(name = "errors")
    private String errors;
}