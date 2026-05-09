package org.j4di.analytical.views;

import lombok.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class WvTxnClientAvgDiffViewId implements Serializable {
    private Long clientId;
    private Long txnId;
    private Timestamp txnTimestamp;
}