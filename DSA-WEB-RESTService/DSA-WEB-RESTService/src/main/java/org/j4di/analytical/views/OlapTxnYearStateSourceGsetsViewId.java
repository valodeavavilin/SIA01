package org.j4di.analytical.views;

import lombok.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class OlapTxnYearStateSourceGsetsViewId implements Serializable {
    private String txnYear;
    private String stateGroup;
    private String sourceSystem;
}