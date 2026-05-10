package org.j4di.visualisation.olap.analytical.views.merchant_state_rank;

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

@PageTitle("SOChart_WV_MERCHANT_STATE_RANK_V")
@Route(value = "SOChart_WV_MERCHANT_STATE_RANK_V", layout = MainView.class)
public class SOChart_WV_MERCHANT_STATE_RANK_V extends VerticalLayout {

    private final AnalyticsRestClient analyticsRestClient;

    private List<WV_MERCHANT_STATE_RANK_V> dataList;

    private final Grid<WV_MERCHANT_STATE_RANK_V> dataGrid =
            new Grid<>(WV_MERCHANT_STATE_RANK_V.class, false);

    private final SOChart soChart = new SOChart();

    public SOChart_WV_MERCHANT_STATE_RANK_V(AnalyticsRestClient analyticsRestClient) {
        this.analyticsRestClient = analyticsRestClient;

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        add(new H2("WV_MERCHANT_STATE_RANK_V - Top Merchants by State"));

        initDataSet();
        initGrid();
        initChart();

        add(createResponsiveChartGridLayout());
    }

    private void initDataSet() {
        dataList = analyticsRestClient.getList(
                "/window/merchant-state-rank",
                new ParameterizedTypeReference<List<WV_MERCHANT_STATE_RANK_V>>() {}
        );
    }

    private void initGrid() {
        dataGrid.addColumn(WV_MERCHANT_STATE_RANK_V::merchantId)
                .setHeader("Merchant ID")
                .setAutoWidth(true);

        dataGrid.addColumn(WV_MERCHANT_STATE_RANK_V::stateGroup)
                .setHeader("State Group")
                .setAutoWidth(true);

        dataGrid.addColumn(WV_MERCHANT_STATE_RANK_V::cityGroup)
                .setHeader("City Group")
                .setAutoWidth(true);

        dataGrid.addColumn(WV_MERCHANT_STATE_RANK_V::totalAmount)
                .setHeader("Total Amount")
                .setAutoWidth(true);

        dataGrid.addColumn(WV_MERCHANT_STATE_RANK_V::txnCount)
                .setHeader("Transaction Count")
                .setAutoWidth(true);

        dataGrid.addColumn(WV_MERCHANT_STATE_RANK_V::rankInState)
                .setHeader("Rank in State")
                .setAutoWidth(true);

        dataGrid.addColumn(WV_MERCHANT_STATE_RANK_V::denseRankInState)
                .setHeader("Dense Rank in State")
                .setAutoWidth(true);

        dataGrid.addColumn(WV_MERCHANT_STATE_RANK_V::rowNumberInState)
                .setHeader("Row Number in State")
                .setAutoWidth(true);

        dataGrid.setItems(dataList);
    }

    private void initChart() {
        List<WV_MERCHANT_STATE_RANK_V> topMerchants = dataList.stream()
                .sorted(Comparator.comparing(
                        WV_MERCHANT_STATE_RANK_V::totalAmount,
                        Comparator.nullsLast(Double::compareTo)
                ).reversed())
                .limit(10)
                .toList();

        CategoryData categories = new CategoryData();
        Data totals = new Data();

        for (WV_MERCHANT_STATE_RANK_V row : topMerchants) {
            categories.add("M" + row.merchantId() + " / " + row.stateGroup());
            totals.add(row.totalAmount() != null ? row.totalAmount() : 0.0);
        }

        BarChart barChart = new BarChart(categories, totals);
        barChart.setName("Top 10 Merchants");

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