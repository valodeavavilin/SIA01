package org.j4di.analytical.views;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface OlapTxnCreditAgeViewRepository
        extends JpaRepository<OlapTxnCreditAgeView, OlapTxnCreditAgeViewId> {

    @Query("SELECT o FROM OlapTxnCreditAgeView o")
    List<OlapTxnCreditAgeView> getOlapTxnCreditAgeView();
}