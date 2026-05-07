package org.datasource.jdbc.views.transactions;

import org.datasource.jdbc.JDBCDataSourceConnector;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class TransactionsViewBuilder {

    private static final Logger logger = Logger.getLogger(TransactionsViewBuilder.class.getName());

    private static final int DEFAULT_LIMIT = 1000;
    private static final int MAX_LIMIT = 10000;

    private static final String SQL_TRANSACTIONS_SELECT =
            "SELECT txn_id, date, client_id, card_id, amount, use_chip, merchant_id, mcc, errors " +
                    "FROM payments.transactions_clean " +
                    "ORDER BY MD5(CAST(txn_id AS TEXT) || 'IIS_SAMPLE_2026') " +
                    "LIMIT ?";

    private List<TransactionsView> transactionsViewList = new ArrayList<>();

    private final JDBCDataSourceConnector jdbcConnector;

    public TransactionsViewBuilder(JDBCDataSourceConnector jdbcConnector) {
        this.jdbcConnector = jdbcConnector;
    }

    public List<TransactionsView> getTransactionsViewList() {
        return this.transactionsViewList;
    }

    /**
     * Default build method.
     * Keeps compatibility with existing code.
     */
    public TransactionsViewBuilder build() {
        return build(DEFAULT_LIMIT);
    }

    /**
     * Build transactions view with safe limit.
     */
    public TransactionsViewBuilder build(int limit) {
        int safeLimit = normalizeLimit(limit);

        logger.info(">>> Building TransactionsView with limit = " + safeLimit);

        transactionsViewList = new ArrayList<>();

        try (Connection jdbcConnection = jdbcConnector.getConnection();
             PreparedStatement selectStmt = jdbcConnection.prepareStatement(SQL_TRANSACTIONS_SELECT)) {

            selectStmt.setInt(1, safeLimit);

            try (ResultSet rs = selectStmt.executeQuery()) {
                while (rs.next()) {
                    Long txnId = rs.getLong("txn_id");
                    String txnDate = rs.getTimestamp("date") != null
                            ? rs.getTimestamp("date").toString()
                            : null;

                    Integer clientId = rs.getInt("client_id");
                    Integer cardId = rs.getInt("card_id");
                    Double amount = rs.getDouble("amount");
                    String useChip = rs.getString("use_chip");
                    Integer merchantId = rs.getInt("merchant_id");
                    Integer mcc = rs.getInt("mcc");
                    String errors = rs.getString("errors");

                    transactionsViewList.add(
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
            }

        } catch (Exception ex) {
            logger.severe("Error while building TransactionsView: " + ex.getMessage());
            ex.printStackTrace();
        }

        return this;
    }

    private int normalizeLimit(int limit) {
        if (limit <= 0) {
            return DEFAULT_LIMIT;
        }

        return Math.min(limit, MAX_LIMIT);
    }
}