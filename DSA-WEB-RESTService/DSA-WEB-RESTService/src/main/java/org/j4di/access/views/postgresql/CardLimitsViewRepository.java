package org.j4di.access.views.postgresql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CardLimitsViewRepository extends JpaRepository<CardLimitsView, Long> {

    @Query("SELECT o FROM CardLimitsView o")
    List<CardLimitsView> getCardLimitsView();
}