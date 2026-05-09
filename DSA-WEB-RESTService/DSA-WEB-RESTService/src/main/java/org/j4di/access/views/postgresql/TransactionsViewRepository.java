package org.j4di.access.views.postgresql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TransactionsViewRepository extends JpaRepository<TransactionsView, Long> {

    @Query("SELECT o FROM TransactionsView o")
    List<TransactionsView> getTransactionsView();
}