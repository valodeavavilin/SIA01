package org.j4di.analytical.views;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface OlapTxnYearStateSourceGsetsViewRepository
        extends JpaRepository<OlapTxnYearStateSourceGsetsView, OlapTxnYearStateSourceGsetsViewId> {

    @Query("SELECT o FROM OlapTxnYearStateSourceGsetsView o")
    List<OlapTxnYearStateSourceGsetsView> getOlapTxnYearStateSourceGsetsView();
}