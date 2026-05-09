package org.j4di.fact.views;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FactTransactionsViewRepository
        extends JpaRepository<FactTransactionsView, FactTransactionsViewId> {

    @Query("SELECT o FROM FactTransactionsView o")
    List<FactTransactionsView> getFactTransactionsView();
}