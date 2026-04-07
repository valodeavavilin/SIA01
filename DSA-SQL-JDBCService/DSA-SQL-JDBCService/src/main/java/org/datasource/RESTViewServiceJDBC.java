package org.datasource;

import org.datasource.jdbc.JDBCDataSourceConnector;
import org.datasource.jdbc.views.cardlimits.CardLimitsView;
import org.datasource.jdbc.views.cardlimits.CardLimitsViewBuilder;
import org.datasource.jdbc.views.cardsecurity.CardSecurityView;
import org.datasource.jdbc.views.cardsecurity.CardSecurityViewBuilder;
import org.datasource.jdbc.views.transactions.TransactionsView;
import org.datasource.jdbc.views.transactions.TransactionsViewBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.logging.Logger;

/*	REST Service URL
	http://localhost:8090/DSA-SQL-JDBCService/rest/payments/CardLimitsView
	http://localhost:8090/DSA-SQL-JDBCService/rest/payments/CardSecurityView
	http://localhost:8090/DSA-SQL-JDBCService/rest/payments/TransactionsView
*/
@RestController
@RequestMapping("/payments")
public class RESTViewServiceJDBC {
	private static Logger logger = Logger.getLogger(RESTViewServiceJDBC.class.getName());

	@RequestMapping(value = "/ping", method = RequestMethod.GET,
			produces = {MediaType.TEXT_PLAIN_VALUE})
	@ResponseBody
	public String ping() {
		logger.info(">>>> DSA-SQL-JDBCService:: RESTViewService is Up!");
		return "Ping response from DSA-SQL-JDBCService!";
	}

	@RequestMapping(value = "/CardLimitsView", method = RequestMethod.GET,
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<CardLimitsView> get_CardLimitsView() {
		return cardLimitsViewBuilder.build().getCardLimitsViewList();
	}

	@RequestMapping(value = "/CardSecurityView", method = RequestMethod.GET,
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<CardSecurityView> get_CardSecurityView() {
		return cardSecurityViewBuilder.build().getCardSecurityViewList();
	}

	@RequestMapping(value = "/TransactionsView", method = RequestMethod.GET,
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<TransactionsView> get_TransactionsView() {
		return transactionsViewBuilder.build().getTransactionsViewList();
	}

	@Autowired private JDBCDataSourceConnector jdbcConnector;
	@Autowired private CardLimitsViewBuilder cardLimitsViewBuilder;
	@Autowired private CardSecurityViewBuilder cardSecurityViewBuilder;
	@Autowired private TransactionsViewBuilder transactionsViewBuilder;
}