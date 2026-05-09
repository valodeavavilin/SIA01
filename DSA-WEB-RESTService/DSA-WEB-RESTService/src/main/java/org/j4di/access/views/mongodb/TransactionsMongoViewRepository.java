package org.j4di.access.views.mongodb;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TransactionsMongoViewRepository extends JpaRepository<TransactionsMongoView, Long> {

    @Query("SELECT o FROM TransactionsMongoView o")
    List<TransactionsMongoView> getTransactionsMongoView();
}