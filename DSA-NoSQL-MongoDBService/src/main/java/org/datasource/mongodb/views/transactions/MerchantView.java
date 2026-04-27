package org.datasource.mongodb.views.transactions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class MerchantView implements Serializable {
    private Long id;
    private String city;
    private String state;
    private String zip;
}