package org.j4di.integration.views;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IntCardProfileViewRepository
        extends JpaRepository<IntCardProfileView, Long> {

    @Query("SELECT o FROM IntCardProfileView o")
    List<IntCardProfileView> getIntCardProfileView();
}