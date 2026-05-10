package org.j4di.visualisation.olap.analytical.views.txn_month_share_running;

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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@PageTitle("SOChart_WV_TXN_MONTH_SHARE_RUNNING_V")
@Route(value = "SOChart_WV_TXN_MONTH_SHARE_RUNNING_V", layout = MainView.class)
public class SOChart_WV_TXN_MONTH_SHARE_RUNNING_V extends VerticalLayout {

    private final AnalyticsRestClient analyticsRestClient;

    private List<WV_TXN_MONTH_SHARE_RUNNING_V> dataList;

    private final Grid<WV_TXN_MONTH_SHARE_RUNNING_V> dataGrid =
            new Grid<>(WV_TXN_MONTH_SHARE_RUNNING_V.class, false);

    private final SOChart soChart = new SOChart();

    public SOChart_WV_TXN_MONTH_SHARE_RUNNING_V(AnalyticsRestClient analyticsRestClient) {
        this.analyticsRestClient = analyticsRestClient;

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        add(new H2("WV_TXN_MONTH_SHARE_RUNNING_V - Monthly Transaction Totals"));

        initDataSet();
        initGrid();
        initChart();

        add(createResponsiveChartGridLayout());
    }

    private void initDataSet() {
        dataList = analyticsRestClient.getList(
                "/window/month-share-running",
                new ParameterizedTypeReference<List<WV_TXN_MONTH_SHARE_RUNNING_V>>() {}
        );
    }

    private void initGrid() {
        dataGrid.addColumn(WV_TXN_MONTH_SHARE_RUNNING_V::txnYear)
                .setHeader("Year")
                .setAutoWidth(true);

        dataGrid.addColumn(WV_TXN_MONTH_SHARE_RUNNING_V::txnMonth)
                .setHeader("Month")
                .setAutoWidth(true);

        dataGrid.addColumn(WV_TXN_MONTH_SHARE_RUNNING_V::txnTimestamp)
                .setHeader("Timestamp")
                .setAutoWidth(true);

        dataGrid.addColumn(WV_TXN_MONTH_SHARE_RUNNING_V::txnId)
                .setHeader("Transaction ID")
                .setAutoWidth(true);

        dataGrid.addColumn(WV_TXN_MONTH_SHARE_RUNNING_V::clientId)
                .setHeader("Client ID")
                .setAutoWidth(true);

        dataGrid.addColumn(WV_TXN_MONTH_SHARE_RUNNING_V::amount)
                .setHeader("Amount")
                .setAutoWidth(true);

        dataGrid.addColumn(WV_TXN_MONTH_SHARE_RUNNING_V::totalMonthAmount)
                .setHeader("Total Month Amount")
                .setAutoWidth(true);

        dataGrid.addColumn(WV_TXN_MONTH_SHARE_RUNNING_V::pctOfMonthTotal)
                .setHeader("% of Month Total")
                .setAutoWidth(true);

        dataGrid.addColumn(WV_TXN_MONTH_SHARE_RUNNING_V::runningTotalMonth)
                .setHeader("Running Total Month")
                .setAutoWidth(true);

        dataGrid.setItems(dataList);
    }

    private void initChart() {
        Map<String, Double> monthlyTotals = new LinkedHashMap<>();

        for (WV_TXN_MONTH_SHARE_RUNNING_V row : dataList) {
            String monthKey = row.txnYear() + "-" + String.format("%02d", row.txnMonth());
            monthlyTotals.putIfAbsent(
                    monthKey,
                    row.totalMonthAmount() != null ? row.totalMonthAmount() : 0.0
            );
        }

        CategoryData categories = new CategoryData();
        Data totals = new Data();

        monthlyTotals.forEach((month, total) -> {
            categories.add(month);
            totals.add(total);
        });

        BarChart barChart = new BarChart(categories, totals);
        barChart.setName("Monthly Total Amount");

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
}