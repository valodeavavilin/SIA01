package org.j4di.analytical.views;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface OlapTxnMonthIncomeMccGsetsViewRepository
        extends JpaRepository<OlapTxnMonthIncomeMccGsetsView, OlapTxnMonthIncomeMccGsetsViewId> {

    @Query("SELECT o FROM OlapTxnMonthIncomeMccGsetsView o")
    List<OlapTxnMonthIncomeMccGsetsView> getOlapTxnMonthIncomeMccGsetsView();
}