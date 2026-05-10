package org.j4di.visualisation.olap.analytical.views.txn_card_security_cube;

import com.storedobject.chart.CategoryData;
import com.storedobject.chart.Data;
import com.storedobject.chart.Label;
import com.storedobject.chart.Legend;
import com.storedobject.chart.PieChart;
import com.storedobject.chart.SOChart;
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

@PageTitle("SOChart_OLAP_VIEW_TXN_CARD_SECURITY_CUBE_V")
@Route(value = "SOChart_OLAP_VIEW_TXN_CARD_SECURITY_CUBE_V", layout = MainView.class)
public class SOChart_OLAP_VIEW_TXN_CARD_SECURITY_CUBE_V extends VerticalLayout {

    private final AnalyticsRestClient analyticsRestClient;

    private List<OLAP_VIEW_TXN_CARD_SECURITY_CUBE_V> dataList;

    private final Grid<OLAP_VIEW_TXN_CARD_SECURITY_CUBE_V> dataGrid =
            new Grid<>(OLAP_VIEW_TXN_CARD_SECURITY_CUBE_V.class, false);

    private final SOChart soChart = new SOChart();

    public SOChart_OLAP_VIEW_TXN_CARD_SECURITY_CUBE_V(AnalyticsRestClient analyticsRestClient) {
        this.analyticsRestClient = analyticsRestClient;

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        add(new H2("OLAP_VIEW_TXN_CARD_SECURITY_CUBE_V - Transaction Amount by Card Security"));

        initDataSet();
        initGrid();
        initChart();

        add(createResponsiveChartGridLayout());
    }

    private void initDataSet() {
        dataList = analyticsRestClient.getList(
                "/analytical/card-security-cube",
                new ParameterizedTypeReference<List<OLAP_VIEW_TXN_CARD_SECURITY_CUBE_V>>() {}
        );
    }

    private void initGrid() {
        dataGrid.addColumn(OLAP_VIEW_TXN_CARD_SECURITY_CUBE_V::chipGroup)
                .setHeader("Chip Group")
                .setAutoWidth(true);

        dataGrid.addColumn(OLAP_VIEW_TXN_CARD_SECURITY_CUBE_V::darkwebGroup)
                .setHeader("Dark Web Group")
                .setAutoWidth(true);

        dataGrid.addColumn(OLAP_VIEW_TXN_CARD_SECURITY_CUBE_V::totalAmount)
                .setHeader("Total Amount")
                .setAutoWidth(true);

        dataGrid.addColumn(OLAP_VIEW_TXN_CARD_SECURITY_CUBE_V::txnCount)
                .setHeader("Transaction Count")
                .setAutoWidth(true);

        dataGrid.addColumn(OLAP_VIEW_TXN_CARD_SECURITY_CUBE_V::avgAmount)
                .setHeader("Average Amount")
                .setAutoWidth(true);

        dataGrid.setItems(dataList);
    }

    private void initChart() {
        CategoryData categories = new CategoryData();
        Data amounts = new Data();

        for (OLAP_VIEW_TXN_CARD_SECURITY_CUBE_V row : dataList) {
            categories.add(row.chipGroup() + " / " + row.darkwebGroup());
            amounts.add(row.totalAmount() != null ? row.totalAmount() : 0.0);
        }

        PieChart pieChart = new PieChart(categories, amounts);
        pieChart.setName("Total Amount");

        Label label = pieChart.getLabel(true);
        label.setFormatter("{b}: {d}%");

        Legend legend = new Legend();

        soChart.add(pieChart, legend);
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