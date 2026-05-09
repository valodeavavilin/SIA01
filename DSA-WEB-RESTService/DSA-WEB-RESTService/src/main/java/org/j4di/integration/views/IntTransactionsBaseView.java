package org.j4di.integration.views;

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
@IdClass(IntTransactionsBaseViewId.class)
@Table(name = "INT_TRANSACTIONS_BASE_V")
public class IntTransactionsBaseView {

    @Id
    @Column(name = "txn_id")
    private Long txnId;

    @Column(name = "txn_raw_date")
    private String txnRawDate;

    @Column(name = "txn_timestamp")
    private Timestamp txnTimestamp;

    @Column(name = "txn_date")
    private Date txnDate;

    @Column(name = "client_id")
    private Long clientId;

    @Column(name = "card_id")
    private Long cardId;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "transaction_channel")
    private String transactionChannel;

    @Column(name = "merchant_id")
    private Long merchantId;

    @Column(name = "mcc")
    private Long mcc;

    @Column(name = "errors")
    private String errors;

    @Id
    @Column(name = "source_system")
    private String sourceSystem;
}