package org.j4di.analytical.views;

import lombok.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class OlapTxnCardSecurityCubeViewId implements Serializable {
    private String chipGroup;
    private String darkwebGroup;
}