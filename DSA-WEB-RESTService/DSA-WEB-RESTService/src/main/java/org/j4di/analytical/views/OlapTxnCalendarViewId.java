package org.j4di.analytical.views;

import lombok.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class OlapTxnCalendarViewId implements Serializable {
    private String txnYear;
    private String txnMonth;
    private String txnDay;
}