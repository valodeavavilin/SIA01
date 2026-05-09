package org.j4di.dimensional.views;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DimCardViewRepository extends JpaRepository<DimCardView, Long> {

    @Query("SELECT o FROM DimCardView o")
    List<DimCardView> getDimCardView();
}