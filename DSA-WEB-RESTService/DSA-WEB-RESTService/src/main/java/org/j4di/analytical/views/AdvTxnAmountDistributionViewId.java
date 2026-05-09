package org.j4di.analytical.views;

import lombok.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class AdvTxnAmountDistributionViewId implements Serializable {
    private String sourceSystem;
    private String cardBrand;
}