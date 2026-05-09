package org.j4di.integration.views;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

@Getter
@Entity
@Immutable
@Table(name = "INT_CARD_PROFILE_V")
public class IntCardProfileView {

    @Id
    @Column(name = "card_id")
    private Long cardId;

    @Column(name = "client_id")
    private Long clientId;

    @Column(name = "card_brand")
    private String cardBrand;

    @Column(name = "card_type")
    private String cardType;

    @Column(name = "expires")
    private String expires;

    @Column(name = "acct_open_date")
    private String acctOpenDate;

    @Column(name = "num_cards_issued")
    private Long numCardsIssued;

    @Column(name = "credit_limit")
    private Double creditLimit;

    @Column(name = "has_chip")
    private Boolean hasChip;

    @Column(name = "year_pin_last_changed")
    private Integer yearPinLastChanged;

    @Column(name = "card_on_dark_web")
    private Boolean cardOnDarkWeb;
}