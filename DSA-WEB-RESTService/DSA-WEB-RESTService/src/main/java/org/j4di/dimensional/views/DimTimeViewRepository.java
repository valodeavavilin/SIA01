package org.j4di.dimensional.views;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Date;
import java.util.List;

public interface DimTimeViewRepository extends JpaRepository<DimTimeView, Date> {

    @Query("SELECT o FROM DimTimeView o")
    List<DimTimeView> getDimTimeView();
}