package org.j4di.analytical.views;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface WvTxnClientAvgDiffViewRepository
        extends JpaRepository<WvTxnClientAvgDiffView, WvTxnClientAvgDiffViewId> {

    @Query("SELECT o FROM WvTxnClientAvgDiffView o")
    List<WvTxnClientAvgDiffView> getWvTxnClientAvgDiffView();
}