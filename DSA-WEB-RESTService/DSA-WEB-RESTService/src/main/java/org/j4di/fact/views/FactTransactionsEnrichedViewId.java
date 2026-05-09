package org.j4di.fact.views;

import java.io.Serializable;
import java.util.Objects;

public class FactTransactionsEnrichedViewId implements Serializable {

    private Long txnId;
    private String sourceSystem;

    public FactTransactionsEnrichedViewId() {
    }

    public FactTransactionsEnrichedViewId(Long txnId, String sourceSystem) {
        this.txnId = txnId;
        this.sourceSystem = sourceSystem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FactTransactionsEnrichedViewId that)) return false;
        return Objects.equals(txnId, that.txnId)
                && Objects.equals(sourceSystem, that.sourceSystem);
    }

    @Override
    public int hashCode() {
        return Objects.hash(txnId, sourceSystem);
    }
}