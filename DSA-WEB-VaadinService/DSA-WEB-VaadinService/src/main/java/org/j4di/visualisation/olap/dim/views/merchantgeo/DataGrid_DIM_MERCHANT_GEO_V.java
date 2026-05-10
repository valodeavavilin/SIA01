package org.j4di.visualisation.olap.dim.views.merchantgeo;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.j4di.visualisation.MainView;
import org.j4di.visualisation.rest.AnalyticsRestClient;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;

@PageTitle("DataGrid_DIM_MERCHANT_GEO_V")
@Route(value = "DataGrid_DIM_MERCHANT_GEO_V", layout = MainView.class)
public class DataGrid_DIM_MERCHANT_GEO_V extends VerticalLayout {

    private final AnalyticsRestClient analyticsRestClient;
    private final Grid<DIM_MERCHANT_GEO_V> dataGrid = new Grid<>(DIM_MERCHANT_GEO_V.class, false);

    public DataGrid_DIM_MERCHANT_GEO_V(AnalyticsRestClient analyticsRestClient) {
        this.analyticsRestClient = analyticsRestClient;

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        add(new H2("Dimension: DIM_MERCHANT_GEO_V"));

        initDataGrid();
        initDataSet();

        add(dataGrid);
    }

    private void initDataGrid() {
        dataGrid.setSizeFull();

        dataGrid.addColumn(DIM_MERCHANT_GEO_V::merchantId).setHeader("Merchant ID").setAutoWidth(true);
        dataGrid.addColumn(DIM_MERCHANT_GEO_V::merchantState).setHeader("State").setAutoWidth(true);
        dataGrid.addColumn(DIM_MERCHANT_GEO_V::merchantCity).setHeader("City").setAutoWidth(true);
        dataGrid.addColumn(DIM_MERCHANT_GEO_V::zip).setHeader("ZIP").setAutoWidth(true);
        dataGrid.addColumn(DIM_MERCHANT_GEO_V::stateGroup).setHeader("State Group").setAutoWidth(true);
        dataGrid.addColumn(DIM_MERCHANT_GEO_V::cityGroup).setHeader("City Group").setAutoWidth(true);
        dataGrid.addColumn(DIM_MERCHANT_GEO_V::geoHierarchy).setHeader("Geo Hierarchy").setAutoWidth(true);
    }

    private void initDataSet() {
        List<DIM_MERCHANT_GEO_V> dataList = analyticsRestClient.getList(
                "/dimensional/merchant-geo",
                new ParameterizedTypeReference<List<DIM_MERCHANT_GEO_V>>() {}
        );

        dataGrid.setItems(dataList);
    }
}