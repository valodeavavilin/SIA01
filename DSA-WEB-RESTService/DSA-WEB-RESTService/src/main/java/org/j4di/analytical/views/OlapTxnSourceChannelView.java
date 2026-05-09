package org.j4di.analytical.views;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

@Getter
@Entity
@Immutable
@IdClass(OlapTxnSourceChannelViewId.class)
@Table(name = "OLAP_VIEW_TXN_SOURCE_CHANNEL_V")
public class OlapTxnSourceChannelView {

    @Id
    @Column(name = "source_system")
    private String sourceSystem;

    @Id
    @Column(name = "channel")
    private String channel;

    @Column(name = "total_amount")
    private Double totalAmount;

    @Column(name = "txn_count")
    private Long txnCount;
}