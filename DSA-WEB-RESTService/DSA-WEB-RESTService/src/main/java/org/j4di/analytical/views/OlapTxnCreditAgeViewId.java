package org.j4di.analytical.views;

import lombok.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class OlapTxnCreditAgeViewId implements Serializable {
    private String creditScoreGroup;
    private String ageGroup;
}