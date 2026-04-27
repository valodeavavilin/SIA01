package org.datasource.mongodb.views.transactions;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.datasource.mongodb.MongoDataSourceConnector;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MongoTransactionViewBuilder {

    private final MongoDataSourceConnector dataSourceConnector;

    private List<MongoTransactionView> viewList;

    public MongoTransactionViewBuilder(MongoDataSourceConnector dataSourceConnector) {
        this.dataSourceConnector = dataSourceConnector;
    }

    public MongoTransactionViewBuilder build() {
        MongoDatabase db = dataSourceConnector.getMongoDatabase();

        MongoCollection<MongoTransactionView> collection =
                db.getCollection("transaction_docs", MongoTransactionView.class);

        this.viewList = collection.find()
                .limit(500)
                .into(new ArrayList<>());
        return this;
    }

    public List<MongoTransactionView> getViewList() {
        return viewList;
    }
}