package org.j4di.access.views.xlsx;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CustomerFinanceViewRepository extends JpaRepository<CustomerFinanceView, Long> {

    @Query("SELECT o FROM CustomerFinanceView o")
    List<CustomerFinanceView> getCustomerFinanceView();
}