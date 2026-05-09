package org.j4di.analytical.views;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

@Getter
@Entity
@Immutable
@IdClass(OlapTxnCreditAgeViewId.class)
@Table(name = "OLAP_VIEW_TXN_CREDIT_AGE_V")
public class OlapTxnCreditAgeView {

    @Id
    @Column(name = "credit_score_group")
    private String creditScoreGroup;

    @Id
    @Column(name = "age_group")
    private String ageGroup;

    @Column(name = "total_amount")
    private Double totalAmount;

    @Column(name = "txn_count")
    private Long txnCount;
}