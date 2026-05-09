package org.j4di.fact.views;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

import java.sql.Date;
import java.sql.Timestamp;

@Getter
@Entity
@Immutable
@IdClass(FactTransactionsViewId.class)
@Table(name = "FACT_TRANSACTIONS_V")
public class FactTransactionsView {

    @Id
    @Column(name = "txn_id")
    private Long txnId;

    @Column(name = "txn_timestamp")
    private Timestamp txnTimestamp;

    @Column(name = "txn_date")
    private Date txnDate;

    @Column(name = "client_id")
    private Long clientId;

    @Column(name = "card_id")
    private Long cardId;

    @Column(name = "merchant_id")
    private Long merchantId;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "mcc")
    private Long mcc;

    @Column(name = "use_chip")
    private String useChip;

    @Column(name = "channel")
    private String channel;

    @Column(name = "errors")
    private String errors;

    @Id
    @Column(name = "source_system")
    private String sourceSystem;
}