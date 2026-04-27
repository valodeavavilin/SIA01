package org.datasource;

import org.datasource.mongodb.views.customerrisk.CustomerRiskView;
import org.datasource.mongodb.views.customerrisk.CustomerRiskViewBuilder;
import org.datasource.mongodb.views.transactions.MongoTransactionView;
import org.datasource.mongodb.views.transactions.MongoTransactionViewBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;
/*
 * REST Service URLs:
 *
 * Ping:
 * http://localhost:8093/DSA-NoSQL-MongoDBService/rest/mongodb/ping
 *
 * MongoDB Transactions View:
 * http://localhost:8093/DSA-NoSQL-MongoDBService/rest/mongodb/MongoTransactionView
 *
 * MongoDB Customer Risk View:
 * http://localhost:8093/DSA-NoSQL-MongoDBService/rest/mongodb/CustomerRiskView
 */

@RestController
@RequestMapping("/mongodb")
public class RESTViewServiceMongoDB {

    private static final Logger logger = Logger.getLogger(RESTViewServiceMongoDB.class.getName());

    @Autowired
    private MongoTransactionViewBuilder mongoTransactionViewBuilder;

    @Autowired
    private CustomerRiskViewBuilder customerRiskViewBuilder;

    @RequestMapping(
            value = "/ping",
            method = RequestMethod.GET,
            produces = {MediaType.TEXT_PLAIN_VALUE}
    )
    @ResponseBody
    public String pingDataSource() {
        logger.info(">>>> REST MongoDB Data Source is Up!");
        return "PING response from DSA-NoSQL-MongoDBService!";
    }

    @RequestMapping(
            value = "/MongoTransactionView",
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    @ResponseBody
    public List<MongoTransactionView> getMongoTransactionView() {
        return mongoTransactionViewBuilder.build().getViewList();
    }

    @RequestMapping(
            value = "/CustomerRiskView",
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    @ResponseBody
    public List<CustomerRiskView> getCustomerRiskView() {
        return customerRiskViewBuilder.build().getViewList();
    }
}