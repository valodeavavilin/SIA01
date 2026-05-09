package org.j4di.analytical.views;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface AdvPivotTxnSourceMonthViewRepository
        extends JpaRepository<AdvPivotTxnSourceMonthView, AdvPivotTxnSourceMonthViewId> {

    @Query("SELECT o FROM AdvPivotTxnSourceMonthView o")
    List<AdvPivotTxnSourceMonthView> getAdvPivotTxnSourceMonthView();
}