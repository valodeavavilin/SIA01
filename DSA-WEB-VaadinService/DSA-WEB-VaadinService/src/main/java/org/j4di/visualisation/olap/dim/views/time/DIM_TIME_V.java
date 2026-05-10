package org.j4di.visualisation.olap.dim.views.time;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DIM_TIME_V(

        @JsonAlias({"txnDate", "txn_date"})
        LocalDate txnDate,

        @JsonAlias({"txnYear", "txn_year"})
        Integer txnYear,

        @JsonAlias({"txnMonth", "txn_month"})
        Integer txnMonth,

        @JsonAlias({"txnDay", "txn_day"})
        Integer txnDay,

        @JsonAlias({"txnQuarter", "txn_quarter"})
        Integer txnQuarter,

        @JsonAlias({"yearMonth", "year_month"})
        String yearMonth,

        @JsonAlias({"monthName", "month_name"})
        String monthName,

        @JsonAlias({"weekdayName", "weekday_name"})
        String weekdayName
) {
}