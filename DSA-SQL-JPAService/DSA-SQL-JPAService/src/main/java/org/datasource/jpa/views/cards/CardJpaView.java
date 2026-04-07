package org.datasource.jpa.views.cards;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;
import java.util.Date;

@Value
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Entity
@Table(name = "cards", schema = "payments")
@NamedQuery(name = "CardJpaView.findAll", query = "SELECT c FROM CardJpaView c")
public class CardJpaView implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "card_id")
    private Integer cardId;

    @Column(name = "client_id")
    private Integer clientId;

    @Column(name = "card_brand")
    private String cardBrand;

    @Column(name = "card_type")
    private String cardType;

    @Column(name = "expires")
    private Date expires;

    @Column(name = "acct_open_date")
    private Date acctOpenDate;

    @Column(name = "num_cards_issued")
    private Integer numCardsIssued;
}