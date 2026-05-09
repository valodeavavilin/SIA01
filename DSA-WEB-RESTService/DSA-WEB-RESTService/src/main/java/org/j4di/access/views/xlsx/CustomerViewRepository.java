package org.j4di.access.views.xlsx;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CustomerViewRepository extends JpaRepository<CustomerView, Long> {

    @Query("SELECT o FROM CustomerView o")
    List<CustomerView> getCustomerView();
}