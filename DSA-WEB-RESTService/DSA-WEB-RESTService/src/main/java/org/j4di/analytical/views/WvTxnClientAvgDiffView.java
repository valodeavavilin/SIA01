package org.j4di.analytical.views;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.Immutable;
import java.sql.Timestamp;

@Getter
@Entity
@Immutable
@IdClass(WvTxnClientAvgDiffViewId.class)
@Table(name = "WV_TXN_CLIENT_AVG_DIFF_V")
public class WvTxnClientAvgDiffView {

    @Id
    @Column(name = "client_id")
    private Long clientId;

    @Id
    @Column(name = "txn_id")
    private Long txnId;

    @Id
    @Column(name = "txn_timestamp")
    private Timestamp txnTimestamp;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "avg_amount_per_client")
    private Double avgAmountPerClient;

    @Column(name = "diff_from_client_avg")
    private Double diffFromClientAvg;
}