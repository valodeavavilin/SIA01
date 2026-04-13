package org.datasource.poi.customers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class CustomerView {
    private Long clientId;
    private Integer currentAge;
    private Integer retirementAge;
    private Integer birthYear;
    private Integer birthMonth;
    private String gender;
    private String address;
    private Double latitude;
    private Double longitude;
}