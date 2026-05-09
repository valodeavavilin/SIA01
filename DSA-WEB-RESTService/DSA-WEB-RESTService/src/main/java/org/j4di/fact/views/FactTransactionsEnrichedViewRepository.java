package org.j4di.fact.views;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FactTransactionsEnrichedViewRepository
        extends JpaRepository<FactTransactionsEnrichedView, FactTransactionsEnrichedViewId> {

    @Query("SELECT o FROM FactTransactionsEnrichedView o")
    List<FactTransactionsEnrichedView> getFactTransactionsEnrichedView();
}