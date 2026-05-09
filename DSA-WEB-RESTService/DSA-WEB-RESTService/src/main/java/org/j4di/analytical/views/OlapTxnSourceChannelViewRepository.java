package org.j4di.analytical.views;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface OlapTxnSourceChannelViewRepository
        extends JpaRepository<OlapTxnSourceChannelView, OlapTxnSourceChannelViewId> {

    @Query("SELECT o FROM OlapTxnSourceChannelView o")
    List<OlapTxnSourceChannelView> getOlapTxnSourceChannelView();
}