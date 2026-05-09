package org.j4di.analytical.views;

import lombok.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class OlapTxnIncomeDebtViewId implements Serializable {
    private String incomeGroup;
    private String debtGroup;
}