package org.j4di.analytical.views;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface WvTxnMonthShareRunningViewRepository
        extends JpaRepository<WvTxnMonthShareRunningView, WvTxnMonthShareRunningViewId> {

    @Query("SELECT o FROM WvTxnMonthShareRunningView o")
    List<WvTxnMonthShareRunningView> getWvTxnMonthShareRunningView();
}