package org.j4di.visualisation;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.RouterLink;
import org.j4di.visualisation.olap.analytical.views.adv_pivot_txn_source_month.SOChart_ADV_PIVOT_TXN_SOURCE_MONTH_V;
import org.j4di.visualisation.olap.analytical.views.adv_txn_amount_distribution.SOChart_ADV_TXN_AMOUNT_DISTRIBUTION_V;
import org.j4di.visualisation.olap.analytical.views.adv_txn_risk_statistics.SOChart_ADV_TXN_RISK_STATISTICS_V;
import org.j4di.visualisation.olap.analytical.views.card_total_rank.SOChart_WV_CARD_TOTAL_RANK_V;
import org.j4di.visualisation.olap.analytical.views.merchant_state_rank.SOChart_WV_MERCHANT_STATE_RANK_V;
import org.j4di.visualisation.olap.analytical.views.txn_card_security_cube.SOChart_OLAP_VIEW_TXN_CARD_SECURITY_CUBE_V;
import org.j4di.visualisation.olap.analytical.views.txn_credit_age.SOChart_OLAP_VIEW_TXN_CREDIT_AGE_V;
import org.j4di.visualisation.olap.analytical.views.txn_month_share_running.SOChart_WV_TXN_MONTH_SHARE_RUNNING_V;
import org.j4di.visualisation.olap.analytical.views.txn_source_channel.SOChart_OLAP_VIEW_TXN_SOURCE_CHANNEL_V;
import org.j4di.visualisation.olap.dim.views.cards.DataGrid_DIM_CARD_V;
import org.j4di.visualisation.olap.dim.views.merchantgeo.DataGrid_DIM_MERCHANT_GEO_V;
import org.j4di.visualisation.olap.dim.views.time.DataGrid_DIM_TIME_V;
import org.j4di.visualisation.olap.fact.views.transactions.DataGrid_FACT_TRANSACTIONS_ENRICHED_V;
import org.j4di.visualisation.views.DashboardView;
import org.j4di.visualisation.olap.dim.views.clients.DataGrid_DIM_CLIENT_V;

public class MainView extends AppLayout {

    public MainView() {
        addToNavbar(createHeader());

        getElement()
                .getStyle()
                .set("--vaadin-app-layout-drawer-width", "330px");

        addToDrawer(createMenu());
    }

    private HorizontalLayout createHeader() {
        H1 title = new H1("DSA Analytics Dashboard");

        title.getStyle()
                .set("font-size", "var(--lumo-font-size-l)")
                .set("margin", "0");

        HorizontalLayout header = new HorizontalLayout(
                new DrawerToggle(),
                title
        );

        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.setWidth("100%");

        return header;
    }

    private Tabs createMenu() {
        Tabs tabs = new Tabs();

        tabs.add(createTab(
                VaadinIcon.DASHBOARD,
                "Dashboard",
                DashboardView.class
        ));

        tabs.add(createTab(
                VaadinIcon.GRID,
                "DIM_CLIENT_V",
                DataGrid_DIM_CLIENT_V.class
        ));

        tabs.add(createTab(
                VaadinIcon.GRID,
                "DIM_CARD_V",
                DataGrid_DIM_CARD_V.class
        ));

        tabs.add(createTab(
                VaadinIcon.GRID,
                "DIM_TIME_V",
                DataGrid_DIM_TIME_V.class
        ));

        tabs.add(createTab(
                VaadinIcon.GRID,
                "DIM_MERCHANT_GEO_V",
                DataGrid_DIM_MERCHANT_GEO_V.class
        ));

        tabs.add(createTab(
                VaadinIcon.TABLE,
                "FACT_TRANSACTIONS_ENRICHED_V",
                DataGrid_FACT_TRANSACTIONS_ENRICHED_V.class
        ));

        tabs.add(createTab(
                VaadinIcon.BAR_CHART,
                "OLAP_SOURCE_CHANNEL",
                SOChart_OLAP_VIEW_TXN_SOURCE_CHANNEL_V.class
        ));

        tabs.add(createTab(
                VaadinIcon.PIE_CHART,
                "OLAP_CARD_SECURITY",
                SOChart_OLAP_VIEW_TXN_CARD_SECURITY_CUBE_V.class
        ));

        tabs.add(createTab(
                VaadinIcon.BAR_CHART_H,
                "OLAP_CREDIT_AGE",
                SOChart_OLAP_VIEW_TXN_CREDIT_AGE_V.class
        ));

        tabs.add(createTab(
                VaadinIcon.LINE_BAR_CHART,
                "WV_MONTH_SHARE_RUNNING",
                SOChart_WV_TXN_MONTH_SHARE_RUNNING_V.class
        ));

        tabs.add(createTab(
                VaadinIcon.BAR_CHART,
                "WV_CARD_TOTAL_RANK",
                SOChart_WV_CARD_TOTAL_RANK_V.class
        ));

        tabs.add(createTab(
                VaadinIcon.BAR_CHART,
                "WV_MERCHANT_STATE_RANK",
                SOChart_WV_MERCHANT_STATE_RANK_V.class
        ));

        tabs.add(createTab(
                VaadinIcon.SPLINE_CHART,
                "ADV_PIVOT_SOURCE_MONTH",
                SOChart_ADV_PIVOT_TXN_SOURCE_MONTH_V.class
        ));

        tabs.add(createTab(
                VaadinIcon.BAR_CHART,
                "ADV_AMOUNT_DISTRIBUTION",
                SOChart_ADV_TXN_AMOUNT_DISTRIBUTION_V.class
        ));

        tabs.add(createTab(
                VaadinIcon.CHART_GRID,
                "ADV_RISK_STATISTICS",
                SOChart_ADV_TXN_RISK_STATISTICS_V.class
        ));
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        return tabs;
    }

    private Tab createTab(VaadinIcon viewIcon, String viewName,
                          Class<? extends Component> viewClass) {

        Icon icon = viewIcon.create();

        icon.getStyle()
                .set("box-sizing", "border-box")
                .set("margin-inline-end", "var(--lumo-space-m)")
                .set("margin-inline-start", "var(--lumo-space-xs)")
                .set("padding", "var(--lumo-space-xs)");

        RouterLink link = new RouterLink();
        link.add(icon, new Span(viewName));
        link.setRoute(viewClass);
        link.setTabIndex(-1);

        return new Tab(link);
    }
}