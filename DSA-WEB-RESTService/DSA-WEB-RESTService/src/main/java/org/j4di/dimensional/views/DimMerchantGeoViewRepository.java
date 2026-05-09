package org.j4di.dimensional.views;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DimMerchantGeoViewRepository extends JpaRepository<DimMerchantGeoView, Long> {

    @Query("SELECT o FROM DimMerchantGeoView o")
    List<DimMerchantGeoView> getDimMerchantGeoView();
}