package org.datasource.mongodb.views.transactions;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class MongoTransactionView implements Serializable {

    @BsonProperty("txn_id")
    @JsonProperty("txn_id")
    private Long txnId;

    private String ts;

    @BsonProperty("client_id")
    @JsonProperty("client_id")
    private Long clientId;

    @BsonProperty("card_id")
    @JsonProperty("card_id")
    private Long cardId;

    private Double amount;
    private String channel;
    private MerchantView merchant;
    private Integer mcc;
    private String errors;
}