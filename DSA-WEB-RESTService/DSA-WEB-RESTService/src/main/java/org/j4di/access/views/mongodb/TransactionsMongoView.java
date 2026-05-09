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
@Table(name = "transactions_mongo_view")
public class TransactionsMongoView {

    @Id
    @Column(name = "txn_id")
    private Long txnId;

    @Column(name = "txn_timestamp")
    private String txnTimestamp;

    @Column(name = "client_id")
    private Long clientId;

    @Column(name = "card_id")
    private Long cardId;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "channel")
    private String channel;

    @Column(name = "merchant_id")
    private Long merchantId;

    @Column(name = "merchant_city")
    private String merchantCity;

    @Column(name = "merchant_state")
    private String merchantState;

    @Column(name = "merchant_zip")
    private String merchantZip;

    @Column(name = "mcc")
    private Long mcc;

    @Column(name = "errors")
    private String errors;
}