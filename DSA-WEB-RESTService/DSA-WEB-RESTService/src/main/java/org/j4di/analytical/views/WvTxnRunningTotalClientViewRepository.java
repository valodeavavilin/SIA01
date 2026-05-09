package org.j4di.analytical.views;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface WvTxnRunningTotalClientViewRepository
        extends JpaRepository<WvTxnRunningTotalClientView, WvTxnRunningTotalClientViewId> {

    @Query("SELECT o FROM WvTxnRunningTotalClientView o")
    List<WvTxnRunningTotalClientView> getWvTxnRunningTotalClientView();
}