package org.j4di.analytical.views;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface WvCreditMonthPerformanceViewRepository
        extends JpaRepository<WvCreditMonthPerformanceView, WvCreditMonthPerformanceViewId> {

    @Query("SELECT o FROM WvCreditMonthPerformanceView o")
    List<WvCreditMonthPerformanceView> getWvCreditMonthPerformanceView();
}