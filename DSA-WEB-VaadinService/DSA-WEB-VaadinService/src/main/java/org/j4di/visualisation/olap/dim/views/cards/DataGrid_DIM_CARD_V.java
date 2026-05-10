package org.j4di.visualisation.olap.dim.views.cards;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.j4di.visualisation.MainView;
import org.j4di.visualisation.rest.AnalyticsRestClient;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;

@PageTitle("DataGrid_DIM_CARD_V")
@Route(value = "DataGrid_DIM_CARD_V", layout = MainView.class)
public class DataGrid_DIM_CARD_V extends VerticalLayout {

    private final AnalyticsRestClient analyticsRestClient;
    private final Grid<DIM_CARD_V> dataGrid = new Grid<>(DIM_CARD_V.class, false);

    public DataGrid_DIM_CARD_V(AnalyticsRestClient analyticsRestClient) {
        this.analyticsRestClient = analyticsRestClient;

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        add(new H2("Dimension: DIM_CARD_V"));

        initDataGrid();
        initDataSet();

        add(dataGrid);
    }

    private void initDataGrid() {
        dataGrid.setSizeFull();

        dataGrid.addColumn(DIM_CARD_V::cardId).setHeader("Card ID").setAutoWidth(true);
        dataGrid.addColumn(DIM_CARD_V::clientId).setHeader("Client ID").setAutoWidth(true);
        dataGrid.addColumn(DIM_CARD_V::cardBrand).setHeader("Card Brand").setAutoWidth(true);
        dataGrid.addColumn(DIM_CARD_V::cardType).setHeader("Card Type").setAutoWidth(true);
        dataGrid.addColumn(DIM_CARD_V::creditLimit).setHeader("Credit Limit").setAutoWidth(true);
        dataGrid.addColumn(DIM_CARD_V::creditLimitGroup).setHeader("Credit Limit Group").setAutoWidth(true);
        dataGrid.addColumn(DIM_CARD_V::hasChip).setHeader("Has Chip").setAutoWidth(true);
        dataGrid.addColumn(DIM_CARD_V::chipGroup).setHeader("Chip Group").setAutoWidth(true);
        dataGrid.addColumn(DIM_CARD_V::cardOnDarkWeb).setHeader("On Dark Web").setAutoWidth(true);
        dataGrid.addColumn(DIM_CARD_V::darkwebGroup).setHeader("Dark Web Group").setAutoWidth(true);
        dataGrid.addColumn(DIM_CARD_V::acctOpenDate).setHeader("Account Open Date").setAutoWidth(true);
        dataGrid.addColumn(DIM_CARD_V::expires).setHeader("Expires").setAutoWidth(true);
        dataGrid.addColumn(DIM_CARD_V::numCardsIssued).setHeader("Cards Issued").setAutoWidth(true);
        dataGrid.addColumn(DIM_CARD_V::yearPinLastChanged).setHeader("PIN Last Changed").setAutoWidth(true);
    }

    private void initDataSet() {
        List<DIM_CARD_V> dataList = analyticsRestClient.getList(
                "/dimensional/card",
                new ParameterizedTypeReference<List<DIM_CARD_V>>() {}
        );

        dataGrid.setItems(dataList);
    }
}