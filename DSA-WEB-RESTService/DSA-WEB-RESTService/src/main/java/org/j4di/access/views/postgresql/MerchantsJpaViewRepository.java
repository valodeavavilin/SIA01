package org.j4di.access.views.postgresql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MerchantsJpaViewRepository extends JpaRepository<MerchantsJpaView, Long> {

    @Query("SELECT o FROM MerchantsJpaView o")
    List<MerchantsJpaView> getMerchantsJpaView();
}