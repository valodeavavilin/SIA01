package org.j4di.analytical.views;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

@Getter
@Entity
@Immutable
@IdClass(OlapTxnYearStateSourceGsetsViewId.class)
@Table(name = "OLAP_VIEW_TXN_YEAR_STATE_SOURCE_GSETS_V")
public class OlapTxnYearStateSourceGsetsView {

    @Id
    @Column(name = "txn_year")
    private String txnYear;

    @Id
    @Column(name = "state_group")
    private String stateGroup;

    @Id
    @Column(name = "source_system")
    private String sourceSystem;

    @Column(name = "total_amount")
    private Double totalAmount;

    @Column(name = "txn_count")
    private Long txnCount;
}