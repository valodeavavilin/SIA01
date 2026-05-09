package org.j4di.access.views.postgresql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CardSecurityViewRepository extends JpaRepository<CardSecurityView, Long> {

    @Query("SELECT o FROM CardSecurityView o")
    List<CardSecurityView> getCardSecurityView();
}