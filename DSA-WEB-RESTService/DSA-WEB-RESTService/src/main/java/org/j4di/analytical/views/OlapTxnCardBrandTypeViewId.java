package org.j4di.analytical.views;

import lombok.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class OlapTxnCardBrandTypeViewId implements Serializable {
    private String cardBrand;
    private String cardType;
}