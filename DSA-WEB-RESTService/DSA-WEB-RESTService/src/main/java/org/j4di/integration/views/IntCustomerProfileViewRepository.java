package org.j4di.integration.views;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IntCustomerProfileViewRepository
        extends JpaRepository<IntCustomerProfileView, Long> {

    @Query("SELECT o FROM IntCustomerProfileView o")
    List<IntCustomerProfileView> getIntCustomerProfileView();
}