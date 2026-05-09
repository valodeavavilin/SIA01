package org.j4di.integration.views;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IntTransactionsBaseViewRepository
        extends JpaRepository<IntTransactionsBaseView, IntTransactionsBaseViewId> {

    @Query("SELECT o FROM IntTransactionsBaseView o")
    List<IntTransactionsBaseView> getIntTransactionsBaseView();
}