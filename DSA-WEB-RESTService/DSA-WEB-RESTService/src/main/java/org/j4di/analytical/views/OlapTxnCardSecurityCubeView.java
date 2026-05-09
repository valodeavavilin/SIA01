package org.j4di.analytical.views;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

@Getter
@Entity
@Immutable
@IdClass(OlapTxnCardSecurityCubeViewId.class)
@Table(name = "OLAP_VIEW_TXN_CARD_SECURITY_CUBE_V")
public class OlapTxnCardSecurityCubeView {

    @Id
    @Column(name = "chip_group")
    private String chipGroup;

    @Id
    @Column(name = "darkweb_group")
    private String darkwebGroup;

    @Column(name = "total_amount")
    private Double totalAmount;

    @Column(name = "txn_count")
    private Long txnCount;

    @Column(name = "avg_amount")
    private Double avgAmount;
}