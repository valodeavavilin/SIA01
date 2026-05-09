package org.j4di.access.views.postgresql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CardsJpaViewRepository extends JpaRepository<CardsJpaView, Long> {

    @Query("SELECT o FROM CardsJpaView o")
    List<CardsJpaView> getCardsJpaView();
}