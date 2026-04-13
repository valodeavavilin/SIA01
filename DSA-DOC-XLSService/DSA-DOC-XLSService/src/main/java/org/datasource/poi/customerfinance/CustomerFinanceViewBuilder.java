package org.datasource.poi.customerfinance;

import org.datasource.poi.XLSXResourceFileDataSourceConnector;
import org.datasource.poi.XLSXViewBuilder;
import org.springframework.stereotype.Service;

@Service
public class CustomerFinanceViewBuilder extends XLSXViewBuilder<CustomerFinanceView> {

    public CustomerFinanceViewBuilder(XLSXResourceFileDataSourceConnector dataSourceConnector) throws Exception {
        super(dataSourceConnector);

        this.setWorksheet("customer_finance");
        this.setRowHeaderNumber(0);
        this.setViewAdapter(tuple ->
                new CustomerFinanceView(
                        toLong(tuple.get("client_id")),
                        parseMoney(tuple.get("per_capita_income")),
                        parseMoney(tuple.get("yearly_income")),
                        parseMoney(tuple.get("total_debt")),
                        toInteger(tuple.get("credit_score")),
                        toInteger(tuple.get("num_credit_cards"))
                )
        );
    }

    private Long toLong(Object value) {
        if (value == null) return null;
        if (value instanceof Number number) {
            return number.longValue();
        }
        return Long.parseLong(value.toString().trim());
    }

    private Integer toInteger(Object value) {
        if (value == null) return null;
        if (value instanceof Number number) {
            return number.intValue();
        }
        return Integer.parseInt(value.toString().trim());
    }

    private Double parseMoney(Object value) {
        if (value == null) return null;

        String text = value.toString().trim()
                .replace("$", "")
                .replace(",", "");

        if (text.isEmpty()) {
            return null;
        }

        return Double.parseDouble(text);
    }
}