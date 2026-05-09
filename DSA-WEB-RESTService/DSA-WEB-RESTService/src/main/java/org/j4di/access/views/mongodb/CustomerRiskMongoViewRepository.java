package org.j4di.access.views.mongodb;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CustomerRiskMongoViewRepository extends JpaRepository<CustomerRiskMongoView, Long> {

    @Query("SELECT o FROM CustomerRiskMongoView o")
    List<CustomerRiskMongoView> getCustomerRiskMongoView();
}