package org.j4di.analytical.views;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface WvTxnClientFirstLastTopViewRepository
        extends JpaRepository<WvTxnClientFirstLastTopView, WvTxnClientFirstLastTopViewId> {

    @Query("SELECT o FROM WvTxnClientFirstLastTopView o")
    List<WvTxnClientFirstLastTopView> getWvTxnClientFirstLastTopView();
}