package org.j4di.analytical.views;

import lombok.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class WvCreditMonthPerformanceViewId implements Serializable {
    private Integer txnYear;
    private Integer txnMonth;
    private String creditScoreGroup;
}