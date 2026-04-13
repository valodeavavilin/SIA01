package org.datasource.poi.customers;

import org.datasource.poi.XLSXResourceFileDataSourceConnector;
import org.datasource.poi.XLSXViewBuilder;
import org.springframework.stereotype.Service;

@Service
public class CustomerViewBuilder extends XLSXViewBuilder<CustomerView> {

    public CustomerViewBuilder(XLSXResourceFileDataSourceConnector dataSourceConnector) throws Exception {
        super(dataSourceConnector);

        this.setWorksheet("customers");
        this.setRowHeaderNumber(0);
        this.setViewAdapter(tuple ->
                new CustomerView(
                        toLong(tuple.get("client_id")),
                        toInteger(tuple.get("current_age")),
                        toInteger(tuple.get("retirement_age")),
                        toInteger(tuple.get("birth_year")),
                        toInteger(tuple.get("birth_month")),
                        toStringValue(tuple.get("gender")),
                        toStringValue(tuple.get("address")),
                        toDouble(tuple.get("latitude")),
                        toDouble(tuple.get("longitude"))
                )
        );
    }

    private Long toLong(Object value) {
        if (value == null) return null;
        return ((Number) value).longValue();
    }

    private Integer toInteger(Object value) {
        if (value == null) return null;
        return ((Number) value).intValue();
    }

    private Double toDouble(Object value) {
        if (value == null) return null;
        return ((Number) value).doubleValue();
    }

    private String toStringValue(Object value) {
        return value != null ? value.toString() : null;
    }
}