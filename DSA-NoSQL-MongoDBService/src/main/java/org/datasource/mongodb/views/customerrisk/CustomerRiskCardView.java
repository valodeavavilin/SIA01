package org.datasource.mongodb.views.customerrisk;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class CustomerRiskCardView implements Serializable {

    @BsonProperty("card_id")
    @JsonProperty("card_id")
    private Long cardId;

    @BsonProperty("card_brand")
    @JsonProperty("card_brand")
    private String cardBrand;

    @BsonProperty("card_type")
    @JsonProperty("card_type")
    private String cardType;

    @BsonProperty("has_chip")
    @JsonProperty("has_chip")
    private String hasChip;

    @BsonProperty("credit_limit")
    @JsonProperty("credit_limit")
    private String creditLimit;
}