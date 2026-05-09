package org.j4di.analytical.views;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface OlapTxnStateBrandCubeViewRepository
        extends JpaRepository<OlapTxnStateBrandCubeView, OlapTxnStateBrandCubeViewId> {

    @Query("SELECT o FROM OlapTxnStateBrandCubeView o")
    List<OlapTxnStateBrandCubeView> getOlapTxnStateBrandCubeView();
}