package org.j4di.visualisation.olap.dim.views.clients;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.j4di.visualisation.MainView;
import org.j4di.visualisation.rest.AnalyticsRestClient;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;

@PageTitle("DataGrid_DIM_CLIENT_V")
@Route(value = "DataGrid_DIM_CLIENT_V", layout = MainView.class)
public class DataGrid_DIM_CLIENT_V extends VerticalLayout {

    private final AnalyticsRestClient analyticsRestClient;
    private final Grid<DIM_CLIENT_V> dataGrid = new Grid<>(DIM_CLIENT_V.class, false);

    public DataGrid_DIM_CLIENT_V(AnalyticsRestClient analyticsRestClient) {
        this.analyticsRestClient = analyticsRestClient;

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        add(new H2("Dimension: DIM_CLIENT_V"));

        initDataGrid();
        initDataSet();

        add(dataGrid);
    }

    private void initDataGrid() {
        dataGrid.setSizeFull();

        dataGrid.addColumn(DIM_CLIENT_V::clientId).setHeader("Client ID").setAutoWidth(true);
        dataGrid.addColumn(DIM_CLIENT_V::gender).setHeader("Gender").setAutoWidth(true);
        dataGrid.addColumn(DIM_CLIENT_V::currentAge).setHeader("Current Age").setAutoWidth(true);
        dataGrid.addColumn(DIM_CLIENT_V::ageGroup).setHeader("Age Group").setAutoWidth(true);
        dataGrid.addColumn(DIM_CLIENT_V::creditScore).setHeader("Credit Score").setAutoWidth(true);
        dataGrid.addColumn(DIM_CLIENT_V::creditScoreGroup).setHeader("Credit Score Group").setAutoWidth(true);
        dataGrid.addColumn(DIM_CLIENT_V::yearlyIncome).setHeader("Yearly Income").setAutoWidth(true);
        dataGrid.addColumn(DIM_CLIENT_V::incomeGroup).setHeader("Income Group").setAutoWidth(true);
        dataGrid.addColumn(DIM_CLIENT_V::totalDebt).setHeader("Total Debt").setAutoWidth(true);
        dataGrid.addColumn(DIM_CLIENT_V::debtGroup).setHeader("Debt Group").setAutoWidth(true);
        dataGrid.addColumn(DIM_CLIENT_V::numCreditCards).setHeader("No. Cards").setAutoWidth(true);
        dataGrid.addColumn(DIM_CLIENT_V::txCount).setHeader("Transactions").setAutoWidth(true);
        dataGrid.addColumn(DIM_CLIENT_V::riskTotalAmount).setHeader("Risk Total Amount").setAutoWidth(true);
    }

    private void initDataSet() {
        List<DIM_CLIENT_V> dataList = analyticsRestClient.getList(
                "/dimensional/client",
                new ParameterizedTypeReference<List<DIM_CLIENT_V>>() {}
        );

        dataGrid.setItems(dataList);
    }
}