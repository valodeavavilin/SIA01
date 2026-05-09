package org.j4di.access.views.xlsx;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

@Getter
@Entity
@Immutable
@Table(name = "CUSTOMER_VIEW")
public class CustomerView {

    @Id
    @Column(name = "clientId")
    private Long clientId;

    @Column(name = "currentAge")
    private Integer currentAge;

    @Column(name = "retirementAge")
    private Integer retirementAge;

    @Column(name = "birthYear")
    private Integer birthYear;

    @Column(name = "birthMonth")
    private Integer birthMonth;

    @Column(name = "gender")
    private String gender;

    @Column(name = "address")
    private String address;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;
}