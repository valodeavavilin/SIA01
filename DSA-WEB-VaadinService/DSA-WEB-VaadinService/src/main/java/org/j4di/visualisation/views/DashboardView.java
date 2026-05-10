package org.j4di.visualisation.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.j4di.visualisation.MainView;

import org.j4di.visualisation.olap.analytical.views.txn_source_channel.SOChart_OLAP_VIEW_TXN_SOURCE_CHANNEL_V;
import org.j4di.visualisation.olap.analytical.views.txn_card_security_cube.SOChart_OLAP_VIEW_TXN_CARD_SECURITY_CUBE_V;
import org.j4di.visualisation.olap.analytical.views.txn_credit_age.SOChart_OLAP_VIEW_TXN_CREDIT_AGE_V;

import org.j4di.visualisation.olap.analytical.views.txn_month_share_running.SOChart_WV_TXN_MONTH_SHARE_RUNNING_V;
import org.j4di.visualisation.olap.analytical.views.card_total_rank.SOChart_WV_CARD_TOTAL_RANK_V;
import org.j4di.visualisation.olap.analytical.views.merchant_state_rank.SOChart_WV_MERCHANT_STATE_RANK_V;

import org.j4di.visualisation.olap.analytical.views.adv_pivot_txn_source_month.SOChart_ADV_PIVOT_TXN_SOURCE_MONTH_V;
import org.j4di.visualisation.olap.analytical.views.adv_txn_amount_distribution.SOChart_ADV_TXN_AMOUNT_DISTRIBUTION_V;
import org.j4di.visualisation.olap.analytical.views.adv_txn_risk_statistics.SOChart_ADV_TXN_RISK_STATISTICS_V;

@PageTitle("DSA Analytics Dashboard")
@Route(value = "", layout = MainView.class)
public class DashboardView extends VerticalLayout {

    public DashboardView() {
        setSizeFull();
        setPadding(true);
        setSpacing(true);

        add(createHeroSection());
        add(createKpiCards());
        add(createSectionTitle("OLAP Analytical Views", "Multidimensional aggregations over integrated transaction data"));
        add(createCardsGrid(
                createChartCard(
                        VaadinIcon.BAR_CHART,
                        "OLAP",
                        "Source & Channel Analysis",
                        "Compare transaction amounts by source system and transaction channel. Useful to show PostgreSQL and MongoDB integration.",
                        SOChart_OLAP_VIEW_TXN_SOURCE_CHANNEL_V.class
                ),
                createChartCard(
                        VaadinIcon.PIE_CHART,
                        "OLAP / CUBE",
                        "Card Security Analysis",
                        "Analyze transaction amounts by chip status and dark web exposure using cube-style aggregations.",
                        SOChart_OLAP_VIEW_TXN_CARD_SECURITY_CUBE_V.class
                ),
                createChartCard(
                        VaadinIcon.BAR_CHART,
                        "OLAP",
                        "Credit Score & Age Segments",
                        "Explore transaction behavior by customer age group and credit score category.",
                        SOChart_OLAP_VIEW_TXN_CREDIT_AGE_V.class
                )
        ));

        add(createSectionTitle("Window Analytical Views", "Rankings, running totals and ordered analytical calculations"));
        add(createCardsGrid(
                createChartCard(
                        VaadinIcon.LINE_BAR_CHART,
                        "WINDOW",
                        "Monthly Running Totals",
                        "Track monthly totals and running transaction amounts using window functions.",
                        SOChart_WV_TXN_MONTH_SHARE_RUNNING_V.class
                ),
                createChartCard(
                        VaadinIcon.BAR_CHART,
                        "WINDOW / RANK",
                        "Top Cards by Amount",
                        "Identify the highest value cards using rank, dense rank and row number.",
                        SOChart_WV_CARD_TOTAL_RANK_V.class
                ),
                createChartCard(
                        VaadinIcon.BAR_CHART,
                        "WINDOW / RANK",
                        "Top Merchants by State",
                        "Rank merchants by transaction value within geographical groups.",
                        SOChart_WV_MERCHANT_STATE_RANK_V.class
                )
        ));

        add(createSectionTitle("Advanced Analytical Views", "Pivoting, statistical distribution and risk correlation analysis"));
        add(createCardsGrid(
                createChartCard(
                        VaadinIcon.SPLINE_CHART,
                        "ADVANCED / PIVOT",
                        "Source Pivot by Month",
                        "Compare PostgreSQL and MongoDB monthly totals in a pivot-style analytical view.",
                        SOChart_ADV_PIVOT_TXN_SOURCE_MONTH_V.class
                ),
                createChartCard(
                        VaadinIcon.BAR_CHART,
                        "ADVANCED / STATS",
                        "Amount Distribution",
                        "Compare average, median, quartiles and standard deviation by source and card brand.",
                        SOChart_ADV_TXN_AMOUNT_DISTRIBUTION_V.class
                ),
                createChartCard(
                        VaadinIcon.CHART,
                        "ADVANCED / RISK",
                        "Risk Correlations",
                        "Analyze correlations between transaction amount and financial risk indicators.",
                        SOChart_ADV_TXN_RISK_STATISTICS_V.class
                )
        ));
    }

    private Component createHeroSection() {
        Div hero = new Div();

        H1 title = new H1("DSA Analytics Dashboard");
        title.getStyle()
                .set("margin", "0")
                .set("font-size", "2.2rem")
                .set("font-weight", "700");

        Paragraph subtitle = new Paragraph(
                "IIS Data Service Architecture project - P2 J4DI - " +
                        "Sava Ștefania-Andreea & Vavilin Vladimir & Șufaru Eduard"
        );
        subtitle.getStyle()
                .set("max-width", "900px")
                .set("font-size", "1.05rem")
                .set("color", "var(--lumo-secondary-text-color)");

        hero.add(title, subtitle);
        hero.getStyle()
                .set("padding", "var(--lumo-space-xl)")
                .set("border-radius", "24px")
                .set("background", "linear-gradient(135deg, var(--lumo-primary-color-10pct), var(--lumo-contrast-5pct))")
                .set("box-shadow", "var(--lumo-box-shadow-s)")
                .set("margin-bottom", "var(--lumo-space-l)");

        return hero;
    }

    private Component createKpiCards() {
        Div grid = new Div();
        grid.getStyle()
                .set("display", "grid")
                .set("grid-template-columns", "repeat(auto-fit, minmax(220px, 1fr))")
                .set("gap", "var(--lumo-space-m)")
                .set("width", "100%")
                .set("margin-bottom", "var(--lumo-space-l)");

        grid.add(
                createKpiCard("4", "Dimension Views", "Client, Card, Time and Merchant Geography"),
                createKpiCard("1", "Fact View", "Enriched transaction fact view"),
                createKpiCard("9/20", "Analytical Charts", "OLAP, Window and Advanced analytics"),
                createKpiCard("5", "Spring Boot Services", "Java4DI layered architecture")
        );

        return grid;
    }

    private Component createKpiCard(String value, String title, String description) {
        Div card = new Div();

        Span valueSpan = new Span(value);
        valueSpan.getStyle()
                .set("font-size", "2rem")
                .set("font-weight", "700")
                .set("color", "var(--lumo-primary-text-color)");

        Span titleSpan = new Span(title);
        titleSpan.getStyle()
                .set("display", "block")
                .set("font-weight", "600")
                .set("margin-top", "var(--lumo-space-xs)");

        Paragraph desc = new Paragraph(description);
        desc.getStyle()
                .set("margin", "var(--lumo-space-xs) 0 0 0")
                .set("color", "var(--lumo-secondary-text-color)")
                .set("font-size", "0.9rem");

        card.add(valueSpan, titleSpan, desc);
        card.getStyle()
                .set("padding", "var(--lumo-space-l)")
                .set("border-radius", "18px")
                .set("background", "var(--lumo-base-color)")
                .set("box-shadow", "var(--lumo-box-shadow-xs)")
                .set("border", "1px solid var(--lumo-contrast-10pct)");

        return card;
    }

    private Component createSectionTitle(String title, String subtitle) {
        Div section = new Div();

        H2 h2 = new H2(title);
        h2.getStyle()
                .set("margin-bottom", "0")
                .set("margin-top", "var(--lumo-space-l)");

        Paragraph p = new Paragraph(subtitle);
        p.getStyle()
                .set("margin-top", "var(--lumo-space-xs)")
                .set("color", "var(--lumo-secondary-text-color)");

        section.add(h2, p);
        return section;
    }

    private Component createCardsGrid(Component... cards) {
        Div grid = new Div();

        grid.getStyle()
                .set("display", "grid")
                .set("grid-template-columns", "repeat(auto-fit, minmax(290px, 1fr))")
                .set("gap", "var(--lumo-space-m)")
                .set("width", "100%");

        grid.add(cards);
        return grid;
    }

    private Component createChartCard(
            VaadinIcon icon,
            String badge,
            String title,
            String description,
            Class<? extends Component> targetView
    ) {
        Div card = new Div();

        Span badgeSpan = new Span(badge);
        badgeSpan.getStyle()
                .set("font-size", "0.75rem")
                .set("font-weight", "700")
                .set("letter-spacing", "0.04em")
                .set("color", "var(--lumo-primary-text-color)")
                .set("background", "var(--lumo-primary-color-10pct)")
                .set("border-radius", "999px")
                .set("padding", "4px 10px");

        Span iconSpan = new Span(icon.create());
        iconSpan.getStyle()
                .set("font-size", "1.6rem")
                .set("color", "var(--lumo-primary-text-color)");

        H2 cardTitle = new H2(title);
        cardTitle.getStyle()
                .set("font-size", "1.15rem")
                .set("margin", "var(--lumo-space-m) 0 var(--lumo-space-xs) 0");

        Paragraph desc = new Paragraph(description);
        desc.getStyle()
                .set("color", "var(--lumo-secondary-text-color)")
                .set("font-size", "0.95rem")
                .set("min-height", "70px");

        Button openButton = new Button("Open chart", VaadinIcon.ARROW_RIGHT.create());
        openButton.addClickListener(event -> UI.getCurrent().navigate(targetView));

        card.add(badgeSpan, iconSpan, cardTitle, desc, openButton);

        card.getStyle()
                .set("padding", "var(--lumo-space-l)")
                .set("border-radius", "22px")
                .set("background", "var(--lumo-base-color)")
                .set("box-shadow", "var(--lumo-box-shadow-s)")
                .set("border", "1px solid var(--lumo-contrast-10pct)")
                .set("cursor", "pointer")
                .set("transition", "transform 160ms ease, box-shadow 160ms ease");

        card.addClickListener(event -> UI.getCurrent().navigate(targetView));

        return card;
    }
}