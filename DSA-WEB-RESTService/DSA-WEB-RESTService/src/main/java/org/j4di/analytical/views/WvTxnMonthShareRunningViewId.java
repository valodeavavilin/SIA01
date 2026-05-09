package org.j4di.analytical.views;

import lombok.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class WvTxnMonthShareRunningViewId implements Serializable {
    private Integer txnYear;
    private Integer txnMonth;
    private Timestamp txnTimestamp;
    private Long txnId;
}