package org.datasource.jdbc.views.cardlimits;

import lombok.Value;

@Value
public class CardLimitsView {
    private Integer cardId;
    private Double creditLimit;
}