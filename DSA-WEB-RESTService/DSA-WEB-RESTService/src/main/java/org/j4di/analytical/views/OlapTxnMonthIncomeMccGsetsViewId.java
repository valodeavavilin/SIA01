package org.j4di.analytical.views;

import lombok.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class OlapTxnMonthIncomeMccGsetsViewId implements Serializable {
    private String txnMonth;
    private String incomeGroup;
    private String mcc;
}