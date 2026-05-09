package org.j4di.dimensional.views;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DimClientViewRepository extends JpaRepository<DimClientView, Long> {

    @Query("SELECT o FROM DimClientView o")
    List<DimClientView> getDimClientView();
}