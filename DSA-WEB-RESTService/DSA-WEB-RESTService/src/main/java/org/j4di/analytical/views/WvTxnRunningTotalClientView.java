package org.j4di.analytical.views;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.Immutable;
import java.sql.Timestamp;

@Getter
@Entity
@Immutable
@IdClass(WvTxnRunningTotalClientViewId.class)
@Table(name = "WV_TXN_RUNNING_TOTAL_CLIENT_V")
public class WvTxnRunningTotalClientView {

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

    @Column(name = "running_total_client")
    private Double runningTotalClient;
}