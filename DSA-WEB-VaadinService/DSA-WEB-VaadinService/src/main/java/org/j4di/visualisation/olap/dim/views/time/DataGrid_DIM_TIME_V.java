package org.j4di.visualisation.olap.dim.views.time;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.j4di.visualisation.MainView;
import org.j4di.visualisation.rest.AnalyticsRestClient;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;

@PageTitle("DataGrid_DIM_TIME_V")
@Route(value = "DataGrid_DIM_TIME_V", layout = MainView.class)
public class DataGrid_DIM_TIME_V extends VerticalLayout {

    private final AnalyticsRestClient analyticsRestClient;
    private final Grid<DIM_TIME_V> dataGrid = new Grid<>(DIM_TIME_V.class, false);

    public DataGrid_DIM_TIME_V(AnalyticsRestClient analyticsRestClient) {
        this.analyticsRestClient = analyticsRestClient;

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        add(new H2("Dimension: DIM_TIME_V"));

        initDataGrid();
        initDataSet();

        add(dataGrid);
    }

    private void initDataGrid() {
        dataGrid.setSizeFull();

        dataGrid.addColumn(DIM_TIME_V::txnDate).setHeader("Transaction Date").setAutoWidth(true);
        dataGrid.addColumn(DIM_TIME_V::txnYear).setHeader("Year").setAutoWidth(true);
        dataGrid.addColumn(DIM_TIME_V::txnMonth).setHeader("Month").setAutoWidth(true);
        dataGrid.addColumn(DIM_TIME_V::txnDay).setHeader("Day").setAutoWidth(true);
        dataGrid.addColumn(DIM_TIME_V::txnQuarter).setHeader("Quarter").setAutoWidth(true);
        dataGrid.addColumn(DIM_TIME_V::yearMonth).setHeader("Year-Month").setAutoWidth(true);
        dataGrid.addColumn(DIM_TIME_V::monthName).setHeader("Month Name").setAutoWidth(true);
        dataGrid.addColumn(DIM_TIME_V::weekdayName).setHeader("Weekday").setAutoWidth(true);
    }

    private void initDataSet() {
        List<DIM_TIME_V> dataList = analyticsRestClient.getList(
                "/dimensional/time",
                new ParameterizedTypeReference<List<DIM_TIME_V>>() {}
        );

        dataGrid.setItems(dataList);
    }
}