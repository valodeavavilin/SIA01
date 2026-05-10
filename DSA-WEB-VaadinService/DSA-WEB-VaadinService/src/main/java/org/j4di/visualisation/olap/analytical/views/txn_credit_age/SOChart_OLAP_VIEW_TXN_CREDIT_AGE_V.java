package org.j4di.visualisation.olap.analytical.views.txn_credit_age;

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

@PageTitle("SOChart_OLAP_VIEW_TXN_CREDIT_AGE_V")
@Route(value = "SOChart_OLAP_VIEW_TXN_CREDIT_AGE_V", layout = MainView.class)
public class SOChart_OLAP_VIEW_TXN_CREDIT_AGE_V extends VerticalLayout {

    private final AnalyticsRestClient analyticsRestClient;

    private List<OLAP_VIEW_TXN_CREDIT_AGE_V> dataList;

    private final Grid<OLAP_VIEW_TXN_CREDIT_AGE_V> dataGrid =
            new Grid<>(OLAP_VIEW_TXN_CREDIT_AGE_V.class, false);

    private final SOChart soChart = new SOChart();

    public SOChart_OLAP_VIEW_TXN_CREDIT_AGE_V(AnalyticsRestClient analyticsRestClient) {
        this.analyticsRestClient = analyticsRestClient;

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        add(new H2("OLAP_VIEW_TXN_CREDIT_AGE_V - Transaction Amount by Credit Score and Age Group"));

        initDataSet();
        initGrid();
        initChart();

        add(createResponsiveChartGridLayout());
    }

    private void initDataSet() {
        dataList = analyticsRestClient.getList(
                "/analytical/credit-age",
                new ParameterizedTypeReference<List<OLAP_VIEW_TXN_CREDIT_AGE_V>>() {}
        );
    }

    private void initGrid() {
        dataGrid.addColumn(OLAP_VIEW_TXN_CREDIT_AGE_V::creditScoreGroup)
                .setHeader("Credit Score Group")
                .setAutoWidth(true);

        dataGrid.addColumn(OLAP_VIEW_TXN_CREDIT_AGE_V::ageGroup)
                .setHeader("Age Group")
                .setAutoWidth(true);

        dataGrid.addColumn(OLAP_VIEW_TXN_CREDIT_AGE_V::totalAmount)
                .setHeader("Total Amount")
                .setAutoWidth(true);

        dataGrid.addColumn(OLAP_VIEW_TXN_CREDIT_AGE_V::txnCount)
                .setHeader("Transaction Count")
                .setAutoWidth(true);

        dataGrid.setItems(dataList);
    }

    private void initChart() {
        List<OLAP_VIEW_TXN_CREDIT_AGE_V> chartData = dataList.stream()
                .filter(row -> !isSubtotal(row.creditScoreGroup()) && !isSubtotal(row.ageGroup()))
                .toList();

        if (chartData.isEmpty()) {
            chartData = dataList;
        }

        CategoryData categories = new CategoryData();
        Data amounts = new Data();

        for (OLAP_VIEW_TXN_CREDIT_AGE_V row : chartData) {
            categories.add(row.creditScoreGroup() + " / " + row.ageGroup());
            amounts.add(row.totalAmount() != null ? row.totalAmount() : 0.0);
        }

        BarChart barChart = new BarChart(categories, amounts);
        barChart.setName("Total Amount");

        RectangularCoordinate coordinate = new RectangularCoordinate(
                new XAxis(DataType.CATEGORY),
                new YAxis(DataType.NUMBER)
        );

        barChart.plotOn(coordinate);
        soChart.add(coordinate, barChart);
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

    private boolean isSubtotal(String value) {
        return value == null
                || value.isBlank()
                || value.contains("{ALL_")
                || value.toLowerCase().contains("subtotal");
    }
}