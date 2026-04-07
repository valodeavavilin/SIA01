package org.datasource;

import org.datasource.jpa.JPADataSourceConnector;
import org.datasource.jpa.views.cards.CardJpaView;
import org.datasource.jpa.views.cards.CardJpaViewBuilder;
import org.datasource.jpa.views.merchants.MerchantJpaView;
import org.datasource.jpa.views.merchants.MerchantJpaViewBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.logging.Logger;

/*	REST Service URL
	http://localhost:8091/DSA_SQL_JPAService/rest/cards/CardJpaView
	http://localhost:8091/DSA_SQL_JPAService/rest/cards/MerchantJpaView
*/
@RestController
@RequestMapping("/cards")
public class RESTViewServiceJPA {
	private static Logger logger = Logger.getLogger(RESTViewServiceJPA.class.getName());

	@RequestMapping(value = "/ping", method = RequestMethod.GET,
			produces = {MediaType.TEXT_PLAIN_VALUE})
	@ResponseBody
	public String pingDataSource() {
		logger.info(">>>> DSA-SQL-JPAService:: RESTViewService is Up!");
		return "Ping response from DSA-SQL-JPAService!";
	}

	@RequestMapping(value = "/CardJpaView", method = RequestMethod.GET,
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<CardJpaView> get_CardJpaView() {
		List<CardJpaView> viewList = this.cardJpaViewBuilder.build().getCardJpaViewList();
		return viewList;
	}

	@RequestMapping(value = "/MerchantJpaView", method = RequestMethod.GET,
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<MerchantJpaView> get_MerchantJpaView() {
		List<MerchantJpaView> viewList = this.merchantJpaViewBuilder.build().getMerchantJpaViewList();
		return viewList;
	}

	@Autowired private JPADataSourceConnector dataSourceConnector;
	@Autowired private CardJpaViewBuilder cardJpaViewBuilder;
	@Autowired private MerchantJpaViewBuilder merchantJpaViewBuilder;
}