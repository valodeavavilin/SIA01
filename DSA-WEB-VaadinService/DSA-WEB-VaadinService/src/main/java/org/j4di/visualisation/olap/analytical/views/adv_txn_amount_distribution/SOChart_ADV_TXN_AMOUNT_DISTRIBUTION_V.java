package org.j4di.visualisation.olap.analytical.views.adv_txn_amount_distribution;

import com.storedobject.chart.BarChart;
import com.storedobject.chart.CategoryData;
import com.storedobject.chart.Data;
import com.storedobject.chart.DataType;
import com.storedobject.chart.RectangularCoordinate;
import com.storedobject.chart.SOChart;
import com.storedobject.chart.XAxis;
import com.storedobject.chart.YAxis;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.j4di.visualisation.MainView;
import org.j4di.visualisation.rest.AnalyticsRestClient;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;

@PageTitle("SOChart_ADV_TXN_AMOUNT_DISTRIBUTION_V")
@Route(value = "SOChart_ADV_TXN_AMOUNT_DISTRIBUTION_V", layout = MainView.class)
public class SOChart_ADV_TXN_AMOUNT_DISTRIBUTION_V extends VerticalLayout {

    private final AnalyticsRestClient analyticsRestClient;

    private List<ADV_TXN_AMOUNT_DISTRIBUTION_V> dataList;

    private final Grid<ADV_TXN_AMOUNT_DISTRIBUTION_V> dataGrid =
            new Grid<>(ADV_TXN_AMOUNT_DISTRIBUTION_V.class, false);

    private final SOChart soChart = new SOChart();

    public SOChart_ADV_TXN_AMOUNT_DISTRIBUTION_V(AnalyticsRestClient analyticsRestClient) {
        this.analyticsRestClient = analyticsRestClient;

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        add(new H2("ADV_TXN_AMOUNT_DISTRIBUTION_V - Amount Distribution by Source and Card Brand"));

        initDataSet();
        initGrid();
        initChart();

        add(createResponsiveChartGridLayout());
    }

    private void initDataSet() {
        dataList = analyticsRestClient.getList(
                "/advanced/txn-amount-distribution",
                new ParameterizedTypeReference<List<ADV_TXN_AMOUNT_DISTRIBUTION_V>>() {}
        );
    }

    private void initGrid() {
        dataGrid.addColumn(ADV_TXN_AMOUNT_DISTRIBUTION_V::sourceSystem)
                .setHeader("Source System")
                .setAutoWidth(true);

        dataGrid.addColumn(ADV_TXN_AMOUNT_DISTRIBUTION_V::cardBrand)
                .setHeader("Card Brand")
                .setAutoWidth(true);

        dataGrid.addColumn(ADV_TXN_AMOUNT_DISTRIBUTION_V::txnCount)
                .setHeader("Transaction Count")
                .setAutoWidth(true);

        dataGrid.addColumn(ADV_TXN_AMOUNT_DISTRIBUTION_V::totalAmount)
                .setHeader("Total Amount")
                .setAutoWidth(true);

        dataGrid.addColumn(ADV_TXN_AMOUNT_DISTRIBUTION_V::avgAmount)
                .setHeader("Average")
                .setAutoWidth(true);

        dataGrid.addColumn(ADV_TXN_AMOUNT_DISTRIBUTION_V::minAmount)
                .setHeader("Minimum")
                .setAutoWidth(true);

        dataGrid.addColumn(ADV_TXN_AMOUNT_DISTRIBUTION_V::q1Amount)
                .setHeader("Q1")
                .setAutoWidth(true);

        dataGrid.addColumn(ADV_TXN_AMOUNT_DISTRIBUTION_V::medianAmount)
                .setHeader("Median")
                .setAutoWidth(true);

        dataGrid.addColumn(ADV_TXN_AMOUNT_DISTRIBUTION_V::q3Amount)
                .setHeader("Q3")
                .setAutoWidth(true);

        dataGrid.addColumn(ADV_TXN_AMOUNT_DISTRIBUTION_V::maxAmount)
                .setHeader("Maximum")
                .setAutoWidth(true);

        dataGrid.addColumn(ADV_TXN_AMOUNT_DISTRIBUTION_V::stddevAmount)
                .setHeader("StdDev")
                .setAutoWidth(true);

        dataGrid.setItems(dataList);
    }

    private void initChart() {
        CategoryData categories = new CategoryData();
        Data avgAmounts = new Data();
        Data medianAmounts = new Data();

        for (ADV_TXN_AMOUNT_DISTRIBUTION_V row : dataList) {
            categories.add(row.sourceSystem() + " / " + row.cardBrand());
            avgAmounts.add(row.avgAmount() != null ? row.avgAmount() : 0.0);
            medianAmounts.add(row.medianAmount() != null ? row.medianAmount() : 0.0);
        }

        BarChart avgBar = new BarChart(categories, avgAmounts);
        avgBar.setName("Average Amount");

        BarChart medianBar = new BarChart(categories, medianAmounts);
        medianBar.setName("Median Amount");

        RectangularCoordinate coordinate = new RectangularCoordinate(
                new XAxis(DataType.CATEGORY),
                new YAxis(DataType.NUMBER)
        );

        avgBar.plotOn(coordinate);
        medianBar.plotOn(coordinate);

        soChart.add(coordinate, avgBar, medianBar);
    }

    private FlexLayout createResponsiveChartGridLayout() {
        soChart.setSize("100%", "500px");

        dataGrid.setWidthFull();
        dataGrid.setHeight("420px");

        VerticalLayout chartPanel = new VerticalLayout(soChart);
        chartPanel.setPadding(false);
        chartPanel.setSpacing(false);
        chartPanel.setWidthFull();
        chartPanel.getStyle()
                .set("flex", "1 1 520px")
                .set("min-width", "320px")
                .set("max-width", "850px");

        VerticalLayout gridPanel = new VerticalLayout(new H3("Chart Data"), dataGrid);
        gridPanel.setPadding(false);
        gridPanel.setSpacing(false);
        gridPanel.setWidthFull();
        gridPanel.getStyle()
                .set("flex", "1 1 520px")
                .set("min-width", "320px")
                .set("max-width", "850px");

        FlexLayout responsiveLayout = new FlexLayout(chartPanel, gridPanel);
        responsiveLayout.setWidthFull();
        responsiveLayout.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        responsiveLayout.getStyle()
                .set("gap", "var(--lumo-space-l)")
                .set("align-items", "flex-start");

        return responsiveLayout;
    }
}