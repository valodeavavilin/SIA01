package org.j4di.analytical.views;

import lombok.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class OlapTxnStateBrandCubeViewId implements Serializable {
    private String stateGroup;
    private String cardBrand;
}