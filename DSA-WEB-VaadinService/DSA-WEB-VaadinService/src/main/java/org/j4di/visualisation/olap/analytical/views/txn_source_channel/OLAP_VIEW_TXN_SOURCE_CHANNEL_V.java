package org.j4di.visualisation.olap.analytical.views.txn_source_channel;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OLAP_VIEW_TXN_SOURCE_CHANNEL_V(

        @JsonAlias({"sourceSystem", "source_system"})
        String sourceSystem,

        String channel,

        @JsonAlias({"totalAmount", "total_amount"})
        Double totalAmount,

        @JsonAlias({"txnCount", "txn_count"})
        Long txnCount
) {
}