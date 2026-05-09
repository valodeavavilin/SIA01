package org.j4di.analytical.views;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface OlapTxnCalendarViewRepository
        extends JpaRepository<OlapTxnCalendarView, OlapTxnCalendarViewId> {

    @Query("SELECT o FROM OlapTxnCalendarView o")
    List<OlapTxnCalendarView> getOlapTxnCalendarView();
}