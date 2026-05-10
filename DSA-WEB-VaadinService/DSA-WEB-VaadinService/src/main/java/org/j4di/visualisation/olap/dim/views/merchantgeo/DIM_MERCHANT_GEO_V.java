package org.j4di.visualisation.olap.dim.views.merchantgeo;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DIM_MERCHANT_GEO_V(

        @JsonAlias({"merchantId", "merchant_id"})
        Long merchantId,

        @JsonAlias({"merchantState", "merchant_state"})
        String merchantState,

        @JsonAlias({"merchantCity", "merchant_city"})
        String merchantCity,

        String zip,

        @JsonAlias({"stateGroup", "state_group"})
        String stateGroup,

        @JsonAlias({"cityGroup", "city_group"})
        String cityGroup,

        @JsonAlias({"geoHierarchy", "geo_hierarchy"})
        String geoHierarchy
) {
}