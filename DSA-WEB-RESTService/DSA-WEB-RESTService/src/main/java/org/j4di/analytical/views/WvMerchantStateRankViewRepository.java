package org.j4di.analytical.views;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface WvMerchantStateRankViewRepository
        extends JpaRepository<WvMerchantStateRankView, Long> {

    @Query("SELECT o FROM WvMerchantStateRankView o")
    List<WvMerchantStateRankView> getWvMerchantStateRankView();
}