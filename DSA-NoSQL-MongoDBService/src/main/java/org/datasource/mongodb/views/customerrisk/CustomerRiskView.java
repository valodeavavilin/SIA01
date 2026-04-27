package org.datasource.mongodb.views.customerrisk;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class CustomerRiskView implements Serializable {

    @BsonProperty("client_id")
    @JsonProperty("client_id")
    private Long clientId;

    @BsonProperty("credit_score")
    @JsonProperty("credit_score")
    private Integer creditScore;

    @BsonProperty("yearly_income")
    @JsonProperty("yearly_income")
    private String yearlyIncome;

    @BsonProperty("total_debt")
    @JsonProperty("total_debt")
    private String totalDebt;

    @BsonProperty("num_credit_cards")
    @JsonProperty("num_credit_cards")
    private Integer numCreditCards;

    @BsonProperty("tx_count")
    @JsonProperty("tx_count")
    private Double txCount;

    @BsonProperty("total_amount")
    @JsonProperty("total_amount")
    private Double totalAmount;

    @BsonProperty("top_mcc")
    @JsonProperty("top_mcc")
    private Double topMcc;

    private List<CustomerRiskCardView> cards;
}