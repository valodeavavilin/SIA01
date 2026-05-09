package org.j4di.access.views.postgresql;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

@Getter
@Entity
@Immutable
@Table(name = "cards_jpa_view")
public class CardsJpaView {

    @Id
    @Column(name = "cardId")
    private Long cardId;

    @Column(name = "clientId")
    private Long clientId;

    @Column(name = "cardBrand")
    private String cardBrand;

    @Column(name = "cardType")
    private String cardType;

    @Column(name = "expires")
    private String expires;

    @Column(name = "acctOpenDate")
    private String acctOpenDate;

    @Column(name = "numCardsIssued")
    private Long numCardsIssued;
}