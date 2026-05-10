package org.j4di.visualisation.olap.fact.views.transactions;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.j4di.visualisation.MainView;
import org.j4di.visualisation.rest.AnalyticsRestClient;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;

@PageTitle("DataGrid_FACT_TRANSACTIONS_ENRICHED_V")
@Route(value = "DataGrid_FACT_TRANSACTIONS_ENRICHED_V", layout = MainView.class)
public class DataGrid_FACT_TRANSACTIONS_ENRICHED_V extends VerticalLayout {

    private final AnalyticsRestClient analyticsRestClient;
    private final Grid<FACT_TRANSACTIONS_ENRICHED_V> dataGrid =
            new Grid<>(FACT_TRANSACTIONS_ENRICHED_V.class, false);

    public DataGrid_FACT_TRANSACTIONS_ENRICHED_V(AnalyticsRestClient analyticsRestClient) {
        this.analyticsRestClient = analyticsRestClient;

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        add(new H2("Fact: FACT_TRANSACTIONS_ENRICHED_V"));

        initDataGrid();
        initDataSet();

        add(dataGrid);
    }

    private void initDataGrid() {
        dataGrid.setSizeFull();

        dataGrid.addColumn(FACT_TRANSACTIONS_ENRICHED_V::txnId)
                .setHeader("Transaction ID")
                .setAutoWidth(true);

        dataGrid.addColumn(FACT_TRANSACTIONS_ENRICHED_V::sourceSystem)
                .setHeader("Source")
                .setAutoWidth(true);

        dataGrid.addColumn(FACT_TRANSACTIONS_ENRICHED_V::txnTimestamp)
                .setHeader("Timestamp")
                .setAutoWidth(true);

        dataGrid.addColumn(FACT_TRANSACTIONS_ENRICHED_V::clientId)
                .setHeader("Client ID")
                .setAutoWidth(true);

        dataGrid.addColumn(FACT_TRANSACTIONS_ENRICHED_V::cardId)
                .setHeader("Card ID")
                .setAutoWidth(true);

        dataGrid.addColumn(FACT_TRANSACTIONS_ENRICHED_V::amount)
                .setHeader("Amount")
                .setAutoWidth(true);

        dataGrid.addColumn(FACT_TRANSACTIONS_ENRICHED_V::channel)
                .setHeader("Channel")
                .setAutoWidth(true);

        dataGrid.addColumn(FACT_TRANSACTIONS_ENRICHED_V::cardBrand)
                .setHeader("Card Brand")
                .setAutoWidth(true);

        dataGrid.addColumn(FACT_TRANSACTIONS_ENRICHED_V::cardType)
                .setHeader("Card Type")
                .setAutoWidth(true);

        dataGrid.addColumn(FACT_TRANSACTIONS_ENRICHED_V::chipGroup)
                .setHeader("Chip Group")
                .setAutoWidth(true);

        dataGrid.addColumn(FACT_TRANSACTIONS_ENRICHED_V::darkwebGroup)
                .setHeader("Dark Web Group")
                .setAutoWidth(true);

        dataGrid.addColumn(FACT_TRANSACTIONS_ENRICHED_V::ageGroup)
                .setHeader("Age Group")
                .setAutoWidth(true);

        dataGrid.addColumn(FACT_TRANSACTIONS_ENRICHED_V::creditScoreGroup)
                .setHeader("Credit Score Group")
                .setAutoWidth(true);

        dataGrid.addColumn(FACT_TRANSACTIONS_ENRICHED_V::incomeGroup)
                .setHeader("Income Group")
                .setAutoWidth(true);

        dataGrid.addColumn(FACT_TRANSACTIONS_ENRICHED_V::debtGroup)
                .setHeader("Debt Group")
                .setAutoWidth(true);

        dataGrid.addColumn(FACT_TRANSACTIONS_ENRICHED_V::stateGroup)
                .setHeader("State Group")
                .setAutoWidth(true);

        dataGrid.addColumn(FACT_TRANSACTIONS_ENRICHED_V::cityGroup)
                .setHeader("City Group")
                .setAutoWidth(true);

        dataGrid.addColumn(FACT_TRANSACTIONS_ENRICHED_V::yearMonth)
                .setHeader("Year-Month")
                .setAutoWidth(true);
    }

    private void initDataSet() {
        List<FACT_TRANSACTIONS_ENRICHED_V> dataList = analyticsRestClient.getList(
                "/fact/transactions-enriched",
                new ParameterizedTypeReference<List<FACT_TRANSACTIONS_ENRICHED_V>>() {}
        );

        dataGrid.setItems(dataList);
    }
}