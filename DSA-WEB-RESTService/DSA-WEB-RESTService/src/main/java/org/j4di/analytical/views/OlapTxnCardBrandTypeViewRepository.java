package org.j4di.analytical.views;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface OlapTxnCardBrandTypeViewRepository
        extends JpaRepository<OlapTxnCardBrandTypeView, OlapTxnCardBrandTypeViewId> {

    @Query("SELECT o FROM OlapTxnCardBrandTypeView o")
    List<OlapTxnCardBrandTypeView> getOlapTxnCardBrandTypeView();
}