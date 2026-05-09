package org.j4di.fact.views;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

import java.sql.Date;
import java.sql.Timestamp;

@Getter
@Entity
@Immutable
@IdClass(FactTransactionsEnrichedViewId.class)
@Table(name = "FACT_TRANSACTIONS_ENRICHED_V")
public class FactTransactionsEnrichedView {

    @Id
    @Column(name = "txn_id")
    private Long txnId;

    @Column(name = "txn_timestamp")
    private Timestamp txnTimestamp;

    @Column(name = "txn_date")
    private Date txnDate;

    @Column(name = "client_id")
    private Long clientId;

    @Column(name = "card_id")
    private Long cardId;

    @Column(name = "merchant_id")
    private Long merchantId;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "mcc")
    private Long mcc;

    @Column(name = "use_chip")
    private String useChip;

    @Column(name = "channel")
    private String channel;

    @Column(name = "errors")
    private String errors;

    @Id
    @Column(name = "source_system")
    private String sourceSystem;

    @Column(name = "gender")
    private String gender;

    @Column(name = "age_group")
    private String ageGroup;

    @Column(name = "credit_score_group")
    private String creditScoreGroup;

    @Column(name = "income_group")
    private String incomeGroup;

    @Column(name = "debt_group")
    private String debtGroup;

    @Column(name = "card_brand")
    private String cardBrand;

    @Column(name = "card_type")
    private String cardType;

    @Column(name = "credit_limit_group")
    private String creditLimitGroup;

    @Column(name = "chip_group")
    private String chipGroup;

    @Column(name = "darkweb_group")
    private String darkwebGroup;

    @Column(name = "state_group")
    private String stateGroup;

    @Column(name = "city_group")
    private String cityGroup;

    @Column(name = "geo_hierarchy")
    private String geoHierarchy;

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