package org.j4di.analytical.views;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.Immutable;
import java.sql.Timestamp;

@Getter
@Entity
@Immutable
@IdClass(WvTxnClientFirstLastTopViewId.class)
@Table(name = "WV_TXN_CLIENT_FIRST_LAST_TOP_V")
public class WvTxnClientFirstLastTopView {

    @Id
    @Column(name = "client_id")
    private Long clientId;

    @Id
    @Column(name = "txn_timestamp")
    private Timestamp txnTimestamp;

    @Id
    @Column(name = "txn_id")
    private Long txnId;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "first_amount_client")
    private Double firstAmountClient;

    @Column(name = "last_amount_client")
    private Double lastAmountClient;

    @Column(name = "top_txn_rank_per_client")
    private Long topTxnRankPerClient;
}