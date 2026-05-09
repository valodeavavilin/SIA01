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
@Table(name = "card_limits_view")
public class CardLimitsView {

    @Id
    @Column(name = "cardId")
    private Long cardId;

    @Column(name = "creditLimit")
    private Double creditLimit;
}