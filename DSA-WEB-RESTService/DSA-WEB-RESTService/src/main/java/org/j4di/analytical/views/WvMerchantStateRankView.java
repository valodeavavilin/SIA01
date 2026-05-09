package org.j4di.analytical.views;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

@Getter
@Entity
@Immutable
@Table(name = "WV_MERCHANT_STATE_RANK_V")
public class WvMerchantStateRankView {

    @Id
    @Column(name = "merchant_id")
    private Long merchantId;

    @Column(name = "state_group")
    private String stateGroup;

    @Column(name = "city_group")
    private String cityGroup;

    @Column(name = "total_amount")
    private Double totalAmount;

    @Column(name = "txn_count")
    private Long txnCount;

    @Column(name = "rank_in_state")
    private Long rankInState;

    @Column(name = "dense_rank_in_state")
    private Long denseRankInState;

    @Column(name = "row_number_in_state")
    private Long rowNumberInState;
}