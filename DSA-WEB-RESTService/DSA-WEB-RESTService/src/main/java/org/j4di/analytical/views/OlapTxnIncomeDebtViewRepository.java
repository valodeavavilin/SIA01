package org.j4di.analytical.views;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface OlapTxnIncomeDebtViewRepository
        extends JpaRepository<OlapTxnIncomeDebtView, OlapTxnIncomeDebtViewId> {

    @Query("SELECT o FROM OlapTxnIncomeDebtView o")
    List<OlapTxnIncomeDebtView> getOlapTxnIncomeDebtView();
}