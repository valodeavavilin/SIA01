package org.j4di.analytical.views;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.Immutable;
import java.sql.Timestamp;

@Getter
@Entity
@Immutable
@IdClass(WvTxnMonthShareRunningViewId.class)
@Table(name = "WV_TXN_MONTH_SHARE_RUNNING_V")
public class WvTxnMonthShareRunningView {

    @Id
    @Column(name = "txn_year")
    private Integer txnYear;

    @Id
    @Column(name = "txn_month")
    private Integer txnMonth;

    @Id
    @Column(name = "txn_timestamp")
    private Timestamp txnTimestamp;

    @Id
    @Column(name = "txn_id")
    private Long txnId;

    @Column(name = "client_id")
    private Long clientId;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "total_month_amount")
    private Double totalMonthAmount;

    @Column(name = "pct_of_month_total")
    private Double pctOfMonthTotal;

    @Column(name = "running_total_month")
    private Double runningTotalMonth;
}