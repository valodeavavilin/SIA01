package org.j4di.dimensional.views;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

import java.sql.Date;

@Getter
@Entity
@Immutable
@Table(name = "DIM_TIME_V")
public class DimTimeView {

    @Id
    @Column(name = "txn_date")
    private Date txnDate;

    @Column(name = "txn_year")
    private Integer txnYear;

    @Column(name = "txn_month")
    private Integer txnMonth;

    @Column(name = "txn_day")
    private Integer txnDay;

    @Column(name = "txn_quarter")
    private Integer txnQuarter;

    @Column(name = "year_month")
    private String yearMonth;

    @Column(name = "month_name")
    private String monthName;

    @Column(name = "weekday_name")
    private String weekdayName;
}