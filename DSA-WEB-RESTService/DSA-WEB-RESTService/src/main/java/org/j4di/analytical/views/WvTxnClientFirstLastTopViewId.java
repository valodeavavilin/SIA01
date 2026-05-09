package org.j4di.analytical.views;

import lombok.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class WvTxnClientFirstLastTopViewId implements Serializable {
    private Long clientId;
    private Timestamp txnTimestamp;
    private Long txnId;
}