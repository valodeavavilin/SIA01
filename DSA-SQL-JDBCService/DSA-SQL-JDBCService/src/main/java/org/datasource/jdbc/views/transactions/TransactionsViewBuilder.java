package org.datasource.jdbc.views.transactions;

import org.datasource.jdbc.JDBCDataSourceConnector;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class TransactionsViewBuilder {
    private static Logger logger = Logger.getLogger(TransactionsViewBuilder.class.getName());

    private String SQL_TRANSACTIONS_SELECT =
            "SELECT txn_id, date, client_id, card_id, amount, use_chip, merchant_id, mcc, errors " +
                    "FROM payments.transactions_clean ORDER BY txn_id";

    private List<TransactionsView> transactionsViewList = new ArrayList<>();

    public List<TransactionsView> getTransactionsViewList() {
        return this.transactionsViewList;
    }

    public TransactionsViewBuilder build() {
        logger.info(">>> Building TransactionsView");
        try (Connection jdbcConnection = jdbcConnector.getConnection()) {
            Statement selectStmt = jdbcConnection.createStatement();
            ResultSet rs = selectStmt.executeQuery(SQL_TRANSACTIONS_SELECT);

            transactionsViewList = new ArrayList<>();
            while (rs.next()) {
                Long txnId = rs.getLong("txn_id");
                String txnDate = rs.getTimestamp("date") != null ? rs.getTimestamp("date").toString() : null;
                Integer clientId = rs.getInt("client_id");
                Integer cardId = rs.getInt("card_id");
                Double amount = rs.getDouble("amount");
                String useChip = rs.getString("use_chip");
                Integer merchantId = rs.getInt("merchant_id");
                Integer mcc = rs.getInt("mcc");
                String errors = rs.getString("errors");

                this.transactionsViewList.add(
                        new TransactionsView(
                                txnId,
                                txnDate,
                                clientId,
                                cardId,
                                amount,
                                useChip,
                                merchantId,
                                mcc,
                                errors
                        )
                );
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return this;
    }

    private JDBCDataSourceConnector jdbcConnector;

    public TransactionsViewBuilder(JDBCDataSourceConnector jdbcConnector) {
        this.jdbcConnector = jdbcConnector;
    }
}