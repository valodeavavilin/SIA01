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
@Table(name = "card_security_view")
public class CardSecurityView {

    @Id
    @Column(name = "cardId")
    private Long cardId;

    @Column(name = "hasChip")
    private Boolean hasChip;

    @Column(name = "yearPinLastChanged")
    private Integer yearPinLastChanged;

    @Column(name = "cardOnDarkWeb")
    private Boolean cardOnDarkWeb;
}