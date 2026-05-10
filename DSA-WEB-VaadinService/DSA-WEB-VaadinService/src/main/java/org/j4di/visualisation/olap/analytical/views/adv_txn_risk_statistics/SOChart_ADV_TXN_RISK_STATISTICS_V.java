package org.j4di.visualisation.olap.analytical.views.adv_txn_risk_statistics;

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

@PageTitle("SOChart_ADV_TXN_RISK_STATISTICS_V")
@Route(value = "SOChart_ADV_TXN_RISK_STATISTICS_V", layout = MainView.class)
public class SOChart_ADV_TXN_RISK_STATISTICS_V extends VerticalLayout {

    private final AnalyticsRestClient analyticsRestClient;

    private List<ADV_TXN_RISK_STATISTICS_V> dataList;

    private final Grid<ADV_TXN_RISK_STATISTICS_V> dataGrid =
            new Grid<>(ADV_TXN_RISK_STATISTICS_V.class, false);

    private final SOChart soChart = new SOChart();

    public SOChart_ADV_TXN_RISK_STATISTICS_V(AnalyticsRestClient analyticsRestClient) {
        this.analyticsRestClient = analyticsRestClient;

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        add(new H2("ADV_TXN_RISK_STATISTICS_V - Risk Correlations by Source"));

        initDataSet();
        initGrid();
        initChart();

        add(createResponsiveChartGridLayout());
    }

    private void initDataSet() {
        dataList = analyticsRestClient.getList(
                "/advanced/txn-risk-statistics",
                new ParameterizedTypeReference<List<ADV_TXN_RISK_STATISTICS_V>>() {}
        );
    }

    private void initGrid() {
        dataGrid.addColumn(ADV_TXN_RISK_STATISTICS_V::sourceSystem)
                .setHeader("Source System")
                .setAutoWidth(true);

        dataGrid.addColumn(ADV_TXN_RISK_STATISTICS_V::txnCount)
                .setHeader("Transaction Count")
                .setAutoWidth(true);

        dataGrid.addColumn(ADV_TXN_RISK_STATISTICS_V::totalAmount)
                .setHeader("Total Amount")
                .setAutoWidth(true);

        dataGrid.addColumn(ADV_TXN_RISK_STATISTICS_V::avgAmount)
                .setHeader("Average")
                .setAutoWidth(true);

        dataGrid.addColumn(ADV_TXN_RISK_STATISTICS_V::minAmount)
                .setHeader("Minimum")
                .setAutoWidth(true);

        dataGrid.addColumn(ADV_TXN_RISK_STATISTICS_V::maxAmount)
                .setHeader("Maximum")
                .setAutoWidth(true);

        dataGrid.addColumn(ADV_TXN_RISK_STATISTICS_V::amountVarianceSample)
                .setHeader("Variance Sample")
                .setAutoWidth(true);

        dataGrid.addColumn(ADV_TXN_RISK_STATISTICS_V::amountStddevSample)
                .setHeader("StdDev Sample")
                .setAutoWidth(true);

        dataGrid.addColumn(ADV_TXN_RISK_STATISTICS_V::corrCreditScoreAmount)
                .setHeader("Corr Credit Score")
                .setAutoWidth(true);

        dataGrid.addColumn(ADV_TXN_RISK_STATISTICS_V::corrYearlyIncomeAmount)
                .setHeader("Corr Yearly Income")
                .setAutoWidth(true);

        dataGrid.addColumn(ADV_TXN_RISK_STATISTICS_V::corrTotalDebtAmount)
                .setHeader("Corr Total Debt")
                .setAutoWidth(true);

        dataGrid.setItems(dataList);
    }

    private void initChart() {
        CategoryData categories = new CategoryData();
        Data corrCreditScore = new Data();
        Data corrYearlyIncome = new Data();
        Data corrTotalDebt = new Data();

        for (ADV_TXN_RISK_STATISTICS_V row : dataList) {
            categories.add(row.sourceSystem());
            corrCreditScore.add(row.corrCreditScoreAmount() != null ? row.corrCreditScoreAmount() : 0.0);
            corrYearlyIncome.add(row.corrYearlyIncomeAmount() != null ? row.corrYearlyIncomeAmount() : 0.0);
            corrTotalDebt.add(row.corrTotalDebtAmount() != null ? row.corrTotalDebtAmount() : 0.0);
        }

        BarChart creditScoreBar = new BarChart(categories, corrCreditScore);
        creditScoreBar.setName("Corr Credit Score / Amount");

        BarChart yearlyIncomeBar = new BarChart(categories, corrYearlyIncome);
        yearlyIncomeBar.setName("Corr Yearly Income / Amount");

        BarChart totalDebtBar = new BarChart(categories, corrTotalDebt);
        totalDebtBar.setName("Corr Total Debt / Amount");

        RectangularCoordinate coordinate = new RectangularCoordinate(
                new XAxis(DataType.CATEGORY),
                new YAxis(DataType.NUMBER)
        );

        creditScoreBar.plotOn(coordinate);
        yearlyIncomeBar.plotOn(coordinate);
        totalDebtBar.plotOn(coordinate);

        soChart.add(coordinate, creditScoreBar, yearlyIncomeBar, totalDebtBar);
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