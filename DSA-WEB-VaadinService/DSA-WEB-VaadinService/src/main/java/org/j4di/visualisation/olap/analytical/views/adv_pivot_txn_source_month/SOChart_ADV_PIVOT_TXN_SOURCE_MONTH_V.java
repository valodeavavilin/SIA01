package org.j4di.visualisation.olap.analytical.views.adv_pivot_txn_source_month;

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

import java.util.Comparator;
import java.util.List;

@PageTitle("SOChart_ADV_PIVOT_TXN_SOURCE_MONTH_V")
@Route(value = "SOChart_ADV_PIVOT_TXN_SOURCE_MONTH_V", layout = MainView.class)
public class SOChart_ADV_PIVOT_TXN_SOURCE_MONTH_V extends VerticalLayout {

    private final AnalyticsRestClient analyticsRestClient;

    private List<ADV_PIVOT_TXN_SOURCE_MONTH_V> dataList;

    private final Grid<ADV_PIVOT_TXN_SOURCE_MONTH_V> dataGrid =
            new Grid<>(ADV_PIVOT_TXN_SOURCE_MONTH_V.class, false);

    private final SOChart soChart = new SOChart();

    public SOChart_ADV_PIVOT_TXN_SOURCE_MONTH_V(AnalyticsRestClient analyticsRestClient) {
        this.analyticsRestClient = analyticsRestClient;

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        add(new H2("ADV_PIVOT_TXN_SOURCE_MONTH_V - Monthly Amount by Source"));

        initDataSet();
        initGrid();
        initChart();

        add(createResponsiveChartGridLayout());
    }

    private void initDataSet() {
        dataList = analyticsRestClient.getList(
                "/advanced/pivot-txn-source-month",
                new ParameterizedTypeReference<List<ADV_PIVOT_TXN_SOURCE_MONTH_V>>() {}
        );
    }

    private void initGrid() {
        dataGrid.addColumn(ADV_PIVOT_TXN_SOURCE_MONTH_V::txnYear)
                .setHeader("Year")
                .setAutoWidth(true);

        dataGrid.addColumn(ADV_PIVOT_TXN_SOURCE_MONTH_V::txnMonth)
                .setHeader("Month")
                .setAutoWidth(true);

        dataGrid.addColumn(ADV_PIVOT_TXN_SOURCE_MONTH_V::postgresTotalAmount)
                .setHeader("PostgreSQL Total")
                .setAutoWidth(true);

        dataGrid.addColumn(ADV_PIVOT_TXN_SOURCE_MONTH_V::mongoTotalAmount)
                .setHeader("MongoDB Total")
                .setAutoWidth(true);

        dataGrid.addColumn(ADV_PIVOT_TXN_SOURCE_MONTH_V::totalAmount)
                .setHeader("Total Amount")
                .setAutoWidth(true);

        dataGrid.setItems(dataList);
    }

    private void initChart() {
        List<ADV_PIVOT_TXN_SOURCE_MONTH_V> chartData = dataList.stream()
                .sorted(Comparator
                        .comparing(ADV_PIVOT_TXN_SOURCE_MONTH_V::txnYear)
                        .thenComparing(ADV_PIVOT_TXN_SOURCE_MONTH_V::txnMonth))
                .toList();

        CategoryData categories = new CategoryData();
        Data postgresAmounts = new Data();
        Data mongoAmounts = new Data();

        for (ADV_PIVOT_TXN_SOURCE_MONTH_V row : chartData) {
            categories.add(row.txnYear() + "-" + String.format("%02d", row.txnMonth()));
            postgresAmounts.add(row.postgresTotalAmount() != null ? row.postgresTotalAmount() : 0.0);
            mongoAmounts.add(row.mongoTotalAmount() != null ? row.mongoTotalAmount() : 0.0);
        }

        BarChart postgresBar = new BarChart(categories, postgresAmounts);
        postgresBar.setName("PostgreSQL");

        BarChart mongoBar = new BarChart(categories, mongoAmounts);
        mongoBar.setName("MongoDB");

        RectangularCoordinate coordinate = new RectangularCoordinate(
                new XAxis(DataType.CATEGORY),
                new YAxis(DataType.NUMBER)
        );

        postgresBar.plotOn(coordinate);
        mongoBar.plotOn(coordinate);

        soChart.add(coordinate, postgresBar, mongoBar);
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