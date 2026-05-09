package org.j4di.analytical.views;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface OlapTxnCardSecurityCubeViewRepository
        extends JpaRepository<OlapTxnCardSecurityCubeView, OlapTxnCardSecurityCubeViewId> {

    @Query("SELECT o FROM OlapTxnCardSecurityCubeView o")
    List<OlapTxnCardSecurityCubeView> getOlapTxnCardSecurityCubeView();
}