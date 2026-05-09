package org.j4di.analytical.views;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface AdvTxnRiskStatisticsViewRepository
        extends JpaRepository<AdvTxnRiskStatisticsView, String> {

    @Query("SELECT o FROM AdvTxnRiskStatisticsView o")
    List<AdvTxnRiskStatisticsView> getAdvTxnRiskStatisticsView();
}