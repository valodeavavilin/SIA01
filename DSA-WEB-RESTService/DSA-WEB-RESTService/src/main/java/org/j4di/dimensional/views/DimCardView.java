package org.j4di.dimensional.views;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

@Getter
@Entity
@Immutable
@Table(name = "DIM_CARD_V")
public class DimCardView {

    @Id
    @Column(name = "card_id")
    private Long cardId;

    @Column(name = "client_id")
    private Long clientId;

    @Column(name = "card_brand")
    private String cardBrand;

    @Column(name = "card_type")
    private String cardType;

    @Column(name = "credit_limit")
    private Double creditLimit;

    @Column(name = "has_chip")
    private Boolean hasChip;

    @Column(name = "card_on_dark_web")
    private Boolean cardOnDarkWeb;

    @Column(name = "acct_open_date")
    private String acctOpenDate;

    @Column(name = "expires")
    private String expires;

    @Column(name = "num_cards_issued")
    private Long numCardsIssued;

    @Column(name = "year_pin_last_changed")
    private Integer yearPinLastChanged;

    @Column(name = "credit_limit_group")
    private String creditLimitGroup;

    @Column(name = "chip_group")
    private String chipGroup;

    @Column(name = "darkweb_group")
    private String darkwebGroup;
}