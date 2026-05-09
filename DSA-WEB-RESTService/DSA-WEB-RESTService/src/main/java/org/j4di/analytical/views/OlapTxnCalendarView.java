package org.j4di.analytical.views;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

@Getter
@Entity
@Immutable
@IdClass(OlapTxnCalendarViewId.class)
@Table(name = "OLAP_VIEW_TXN_CALENDAR_V")
public class OlapTxnCalendarView {

    @Id
    @Column(name = "txn_year")
    private String txnYear;

    @Id
    @Column(name = "txn_month")
    private String txnMonth;

    @Id
    @Column(name = "txn_day")
    private String txnDay;

    @Column(name = "total_amount")
    private Double totalAmount;

    @Column(name = "txn_count")
    private Long txnCount;
}