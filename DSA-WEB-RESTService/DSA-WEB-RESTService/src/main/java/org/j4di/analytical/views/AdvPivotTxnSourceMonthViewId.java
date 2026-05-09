package org.j4di.analytical.views;

import lombok.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class AdvPivotTxnSourceMonthViewId implements Serializable {
    private Integer txnYear;
    private Integer txnMonth;
}