package org.j4di.analytical.views;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

@Getter
@Entity
@Immutable
@Table(name = "WV_CARD_TOTAL_RANK_V")
public class WvCardTotalRankView {

    @Id
    @Column(name = "card_id")
    private Long cardId;

    @Column(name = "total_amount")
    private Double totalAmount;

    @Column(name = "rank_card")
    private Long rankCard;

    @Column(name = "dense_rank_card")
    private Long denseRankCard;

    @Column(name = "row_number_card")
    private Long rowNumberCard;
}