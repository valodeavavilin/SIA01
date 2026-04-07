package org.datasource.jdbc.views.cardlimits;

import org.datasource.jdbc.JDBCDataSourceConnector;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class CardLimitsViewBuilder {
    private static Logger logger = Logger.getLogger(CardLimitsViewBuilder.class.getName());

    private String SQL_CARD_LIMITS_SELECT =
            "SELECT card_id, credit_limit FROM payments.card_limits ORDER BY card_id";

    private List<CardLimitsView> cardLimitsViewList = new ArrayList<>();

    public List<CardLimitsView> getCardLimitsViewList() {
        return this.cardLimitsViewList;
    }

    public CardLimitsViewBuilder build() {
        logger.info(">>> Building CardLimitsView");
        try (Connection jdbcConnection = jdbcConnector.getConnection()) {
            Statement selectStmt = jdbcConnection.createStatement();
            ResultSet rs = selectStmt.executeQuery(SQL_CARD_LIMITS_SELECT);

            cardLimitsViewList = new ArrayList<>();
            while (rs.next()) {
                Integer cardId = rs.getInt("card_id");
                Double creditLimit = rs.getDouble("credit_limit");

                this.cardLimitsViewList.add(new CardLimitsView(cardId, creditLimit));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return this;
    }

    private JDBCDataSourceConnector jdbcConnector;

    public CardLimitsViewBuilder(JDBCDataSourceConnector jdbcConnector) {
        this.jdbcConnector = jdbcConnector;
    }
}