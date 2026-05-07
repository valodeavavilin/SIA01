package org.datasource.jdbc.views.cardsecurity;

import org.datasource.jdbc.JDBCDataSourceConnector;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class CardSecurityViewBuilder {
    private static Logger logger = Logger.getLogger(CardSecurityViewBuilder.class.getName());

    private String SQL_CARD_SECURITY_SELECT =
            "SELECT card_id, has_chip, year_pin_last_changed, card_on_dark_web " +
                    "FROM payments.card_security ORDER BY card_id";

    private List<CardSecurityView> cardSecurityViewList = new ArrayList<>();

    public List<CardSecurityView> getCardSecurityViewList() {
        return this.cardSecurityViewList;
    }

    public CardSecurityViewBuilder build() {
        logger.info(">>> Building CardSecurityView");
        try (Connection jdbcConnection = jdbcConnector.getConnection()) {
            Statement selectStmt = jdbcConnection.createStatement();
            ResultSet rs = selectStmt.executeQuery(SQL_CARD_SECURITY_SELECT);

            cardSecurityViewList = new ArrayList<>();
            while (rs.next()) {
                Integer cardId = rs.getInt("card_id");
                Boolean hasChip = rs.getBoolean("has_chip");
                Integer yearPinLastChanged = rs.getInt("year_pin_last_changed");
                Boolean cardOnDarkWeb = rs.getBoolean("card_on_dark_web");

                this.cardSecurityViewList.add(
                        new CardSecurityView(cardId, hasChip, yearPinLastChanged, cardOnDarkWeb)
                );
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return this;
    }

    private JDBCDataSourceConnector jdbcConnector;

    public CardSecurityViewBuilder(JDBCDataSourceConnector jdbcConnector) {
        this.jdbcConnector = jdbcConnector;
    }
}