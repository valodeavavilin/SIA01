package org.spark.service.rest;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.spark.service.SparkSQLService;
import org.spark.service.exception.RESTSQLWorkflowException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/*	REST Service URL:
	http://localhost:9990/DSA-SparkSQL-Service/rest/view/{VIEW_NAME}
	http://localhost:9990/DSA-SparkSQL-Service/rest/STRUCT/{VIEW_NAME}
	http://localhost:9990/DSA-SparkSQL-Service/rest/ping
 */
@RestController @RequestMapping("/rest")
public class AutoRESTViewService {
    private static Logger logger = Logger.getLogger(AutoRESTViewService.class.getName());
    //
    @Value( "${sparksql.autorest.mode}")
    private String sparksqlAutorestMode;
    //
    private final SparkSQLService sparkSQLService;
    // Auto-Autowired
    public AutoRESTViewService(SparkSQLService sparkSQLService) {
        this.sparkSQLService = sparkSQLService;
        //
    }
    @PostConstruct
    private void init(){
        if ("on".equals(sparksqlAutorestMode.toLowerCase().trim())
                || "restricted".equals(sparksqlAutorestMode.toLowerCase().trim())) {
            loadViewDefs();
            this.sparksqlAutorestMode = sparksqlAutorestMode.toLowerCase().trim();
            logger.info("DEBUG: AutoRESTViewService: viewMap: " + viewMap);
        }
        else
            this.sparksqlAutorestMode = "off";
        logger.info("DEBUG: views AUTORESTing policy: " + sparksqlAutorestMode);
    }
    //
    @RequestMapping(value = "/ping", method = RequestMethod.GET,
            produces = {MediaType.TEXT_PLAIN_VALUE})
    @ResponseBody
    public String pingDataSource() {
        if ("off".equals(sparksqlAutorestMode))
            throw new RESTSQLWorkflowException("SparkSQL AUTO REST Service is not available!");
        //
        return "PING response from SparkSQLRESTService!";
    }
    //
    @RequestMapping(value = "/view/**", method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE})
    @ResponseBody
    public String get_ViewDataSet(HttpServletRequest request) throws Exception {
        if ("off".equals(sparksqlAutorestMode))
            throw new RESTSQLWorkflowException("SparkSQL AUTO REST Service is not available (off)!");

        String base = "/rest/view/";
        String viewRESTPath = request.getRequestURI()
                .substring(request.getRequestURI().indexOf(base) + base.length());
        logger.info("DEBUG: get_ViewDataSet: Querying View REST PATH: " + viewRESTPath);
        //
        String viewName = getViewName(viewRESTPath);
        if (viewName == null)
            throw new RESTSQLWorkflowException("REST Error: viewName for " + viewRESTPath + " is NULL!");
        //
        Dataset<Row> viewDataSet =  this.sparkSQLService.getSpark().sql("SELECT * FROM " + viewName);
        // DEBUG: View Data Set
        logger.info("DEBUG: get_ViewDataSet: View Schema: ");
        viewDataSet.printSchema();
        //viewDataSet.show();
        //
        String jsonList = viewDataSet.toJSON().collectAsList().toString();
        return jsonList;
    }

    //
    @RequestMapping(value = "/STRUCT/**", method = RequestMethod.GET,
            produces = {MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public String get_ViewDataSTRUCT(HttpServletRequest request) throws Exception {
        if ("off".equals(sparksqlAutorestMode))
            throw new RESTSQLWorkflowException("SparkSQL AUTO REST Service is not available!");
        //
        String base = "/rest/STRUCT/";
        String viewRESTPath = request.getRequestURI()
                .substring(request.getRequestURI().indexOf(base) + base.length());
        //
        String viewName = getViewName(viewRESTPath);
        logger.info("DEBUG: get_ViewDataSTRUCT: Querying View REST named: " + viewName);
        if (viewName == null)
            throw new RESTSQLWorkflowException("REST Error: viewName for " + viewRESTPath + " is NULL!");
        //
        Dataset<Row> viewDataSet =  this.sparkSQLService.getSpark().sql(
                "SELECT * FROM " + viewName + " WHERE 1=0");
        // DEBUG: View Data Set
        logger.info("DEBUG: get_ViewDataSTRUCT: View Schema: ");
        viewDataSet.printSchema();
        //
        String viewSchema = viewDataSet.schema().sql();
        return viewSchema;
    }
    // Resolve VIEW name from URL
    // ALTER VIEW <viewName> SET TBLPROPERTIES('AUTOREST' = "/module-path/<viewName>");
    // ALTER VIEW OLAP_DIM_CUSTS_CITIES_DEPTS SET TBLPROPERTIES('AUTOREST' = "olap/dim/custs_cities_depts");
    private String getViewName(String viewURL) {
        if ("off".equals(sparksqlAutorestMode))
            throw new RESTSQLWorkflowException("SparkSQL AUTO REST Service is not available!" +
                    " Check: sparksql.autorest.enabled property!");

        // Process VIEWs metadata from viewMap
        logger.info("DEBUG: views AUTORESTing searching: " + viewURL);
        for(String viewName: viewMap.keySet()) {
            //
            if (viewURL.toUpperCase().equals(viewName) ||
                    viewURL.toUpperCase().equals(viewMap.get(viewName).toUpperCase())) {
                //
                logger.info("DEBUG: views AUTORESTing: " + viewName + " -> " + viewMap.get(viewName));
                switch (sparksqlAutorestMode) {
                    case "on" -> {return viewName;}
                    case "restricted" -> {
                        if (viewMap.get(viewName).equals("NONE"))
                            throw new RESTSQLWorkflowException("REST Error: " + viewURL +
                                    " VIEW is not REST enabled!");
                        return viewName;
                    }
                    case "off" ->
                            throw new RESTSQLWorkflowException("REST Error: AutoREST disabled, " +
                                    "sparksql.autorest.mode is off!");
                    default -> throw new RESTSQLWorkflowException("REST Error: AutoREST disabled, " +
                            "sparksql.autorest.mode is invalid:" + sparksqlAutorestMode + "!");
                }
            }
        }
        // not registered view
        return null;
    }

    private Map<String, String> viewMap = new HashMap<>();
    private void loadViewDefs(){
        // Get VIEWs metadata into viewMap
        List<Row> viewDataSet =  this.sparkSQLService.getSpark().sql("SHOW VIEWS").collectAsList();
        viewMap = new HashMap<>();
        for (Row row: viewDataSet){
            String showViewCommand = String.format("SHOW TBLPROPERTIES %s('AUTOREST')", row.getString(1));
            String autorest = sparkSQLService.getSpark().sql(showViewCommand).first().getString(1);
            if (autorest == null || autorest.isBlank() || autorest.isEmpty()
                    || autorest.contains("does not have property: AUTOREST"))
                autorest = "NONE";
            viewMap.put(row.getString(1).toUpperCase(), autorest.toUpperCase());
        }
    }
}

/*
    REST Service URL
    http://localhost:9990/DSA-SparkSQL-Service/rest/ping
    http://localhost:9990/DSA-SparkSQL-Service/rest/view/{VIEW_NAME}
    http://localhost:9990/DSA-SparkSQL-Service/rest/STRUCT/{VIEW_NAME}

    --------------------------------------------------------------------------------
    SOURCE / REMOTE SPARKSQL VIEWS
    --------------------------------------------------------------------------------

    * PostgreSQL JDBC
    http://localhost:9990/DSA-SparkSQL-Service/rest/view/card_limits_view
    http://localhost:9990/DSA-SparkSQL-Service/rest/view/card_security_view
    http://localhost:9990/DSA-SparkSQL-Service/rest/view/transactions_view

    * PostgreSQL JPA
    http://localhost:9990/DSA-SparkSQL-Service/rest/view/cards_jpa_view
    http://localhost:9990/DSA-SparkSQL-Service/rest/view/merchants_jpa_view

    * XLSX
    http://localhost:9990/DSA-SparkSQL-Service/rest/view/CUSTOMER_VIEW
    http://localhost:9990/DSA-SparkSQL-Service/rest/view/CUSTOMER_FINANCE_VIEW

    * MongoDB
    http://localhost:9990/DSA-SparkSQL-Service/rest/view/transactions_mongo_view
    http://localhost:9990/DSA-SparkSQL-Service/rest/view/customer_risk_mongo_view
    http://localhost:9990/DSA-SparkSQL-Service/rest/view/customer_risk_cards_mongo_view
    http://localhost:9990/DSA-SparkSQL-Service/rest/view/transactions_customer_risk_mongo_view


    --------------------------------------------------------------------------------
    INTEGRATION VIEWS
    --------------------------------------------------------------------------------

    http://localhost:9990/DSA-SparkSQL-Service/rest/view/INT_CUSTOMER_PROFILE_V
    http://localhost:9990/DSA-SparkSQL-Service/rest/view/INT_CARD_PROFILE_V
    http://localhost:9990/DSA-SparkSQL-Service/rest/view/INT_TRANSACTIONS_BASE_V


    --------------------------------------------------------------------------------
    DIMENSIONAL VIEWS
    --------------------------------------------------------------------------------

    http://localhost:9990/DSA-SparkSQL-Service/rest/view/DIM_CLIENT_V
    http://localhost:9990/DSA-SparkSQL-Service/rest/view/DIM_CARD_V
    http://localhost:9990/DSA-SparkSQL-Service/rest/view/DIM_TIME_V
    http://localhost:9990/DSA-SparkSQL-Service/rest/view/DIM_MERCHANT_GEO_V


    --------------------------------------------------------------------------------
    FACT VIEWS
    --------------------------------------------------------------------------------

    http://localhost:9990/DSA-SparkSQL-Service/rest/view/FACT_TRANSACTIONS_V
    http://localhost:9990/DSA-SparkSQL-Service/rest/view/FACT_TRANSACTIONS_ENRICHED_V


    --------------------------------------------------------------------------------
    OLAP ANALYTICAL VIEWS
    --------------------------------------------------------------------------------

    http://localhost:9990/DSA-SparkSQL-Service/rest/view/OLAP_VIEW_TXN_CALENDAR_V
    http://localhost:9990/DSA-SparkSQL-Service/rest/view/OLAP_VIEW_TXN_MERCHANT_GEO_V
    http://localhost:9990/DSA-SparkSQL-Service/rest/view/OLAP_VIEW_TXN_CREDIT_AGE_V
    http://localhost:9990/DSA-SparkSQL-Service/rest/view/OLAP_VIEW_TXN_CARD_BRAND_TYPE_V
    http://localhost:9990/DSA-SparkSQL-Service/rest/view/OLAP_VIEW_TXN_SOURCE_CHANNEL_V
    http://localhost:9990/DSA-SparkSQL-Service/rest/view/OLAP_VIEW_TXN_STATE_BRAND_CUBE_V
    http://localhost:9990/DSA-SparkSQL-Service/rest/view/OLAP_VIEW_TXN_INCOME_DEBT_V
    http://localhost:9990/DSA-SparkSQL-Service/rest/view/OLAP_VIEW_TXN_CARD_SECURITY_CUBE_V
    http://localhost:9990/DSA-SparkSQL-Service/rest/view/OLAP_VIEW_TXN_YEAR_STATE_SOURCE_GSETS_V
    http://localhost:9990/DSA-SparkSQL-Service/rest/view/OLAP_VIEW_TXN_MONTH_INCOME_MCC_GSETS_V


    --------------------------------------------------------------------------------
    WINDOW ANALYTICAL VIEWS
    --------------------------------------------------------------------------------

    http://localhost:9990/DSA-SparkSQL-Service/rest/view/WV_TXN_RUNNING_TOTAL_CLIENT_V
    http://localhost:9990/DSA-SparkSQL-Service/rest/view/WV_TXN_CLIENT_AVG_DIFF_V
    http://localhost:9990/DSA-SparkSQL-Service/rest/view/WV_CARD_TOTAL_RANK_V
    http://localhost:9990/DSA-SparkSQL-Service/rest/view/WV_TXN_MONTH_SHARE_RUNNING_V
    http://localhost:9990/DSA-SparkSQL-Service/rest/view/WV_TXN_CLIENT_FIRST_LAST_TOP_V
    http://localhost:9990/DSA-SparkSQL-Service/rest/view/WV_MERCHANT_STATE_RANK_V
    http://localhost:9990/DSA-SparkSQL-Service/rest/view/WV_CREDIT_MONTH_PERFORMANCE_V


    --------------------------------------------------------------------------------
    STRUCTURE ENDPOINT EXAMPLES
    --------------------------------------------------------------------------------

    http://localhost:9990/DSA-SparkSQL-Service/rest/STRUCT/OLAP_VIEW_TXN_CALENDAR_V
    http://localhost:9990/DSA-SparkSQL-Service/rest/STRUCT/FACT_TRANSACTIONS_ENRICHED_V
    http://localhost:9990/DSA-SparkSQL-Service/rest/STRUCT/DIM_CLIENT_V
    http://localhost:9990/DSA-SparkSQL-Service/rest/STRUCT/WV_CREDIT_MONTH_PERFORMANCE_V
*/