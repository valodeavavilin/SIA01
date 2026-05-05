package org.datasource.mongodb.views.customerrisk;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.datasource.mongodb.MongoDataSourceConnector;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerRiskViewBuilder {

    private final MongoDataSourceConnector dataSourceConnector;

    private List<CustomerRiskView> viewList;

    public CustomerRiskViewBuilder(MongoDataSourceConnector dataSourceConnector) {
        this.dataSourceConnector = dataSourceConnector;
    }

    public CustomerRiskViewBuilder build() {
        MongoDatabase db = dataSourceConnector.getMongoDatabase();

        MongoCollection<CustomerRiskView> collection =
                db.getCollection("customer_risk_docs", CustomerRiskView.class);

        this.viewList = collection.find().into(new ArrayList<>());

        return this;
    }

    public List<CustomerRiskView> getViewList() {
        return viewList;
    }
}