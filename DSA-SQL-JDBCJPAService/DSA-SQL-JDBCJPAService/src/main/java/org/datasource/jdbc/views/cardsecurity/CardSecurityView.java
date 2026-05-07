package org.datasource.jdbc.views.cardsecurity;

import lombok.Value;

@Value
public class CardSecurityView {
    private Integer cardId;
    private Boolean hasChip;
    private Integer yearPinLastChanged;
    private Boolean cardOnDarkWeb;
}