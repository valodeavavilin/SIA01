package org.datasource;

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

/*
    REST Service URLs:

    http://localhost:8090/DSA-SQL-PostgreSQLService/rest/cards/CardJpaView
    http://localhost:8090/DSA-SQL-PostgreSQLService/rest/cards/MerchantJpaView
*/
@RestController
@RequestMapping("/cards")
public class RESTViewServiceJPA {

	private static final Logger logger = Logger.getLogger(RESTViewServiceJPA.class.getName());

	@RequestMapping(value = "/ping", method = RequestMethod.GET,
			produces = {MediaType.TEXT_PLAIN_VALUE})
	@ResponseBody
	public String pingDataSource() {
		logger.info(">>>> DSA-SQL-PostgreSQLService:: RESTViewServiceJPA is Up!");
		return "Ping response from DSA-SQL-PostgreSQLService JPA!";
	}

	@RequestMapping(value = "/CardJpaView", method = RequestMethod.GET,
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<CardJpaView> get_CardJpaView() {
		return cardJpaViewBuilder
				.build()
				.getCardJpaViewList();
	}

	@RequestMapping(value = "/MerchantJpaView", method = RequestMethod.GET,
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<MerchantJpaView> get_MerchantJpaView() {
		return merchantJpaViewBuilder
				.build()
				.getMerchantJpaViewList();
	}

	@Autowired
	private CardJpaViewBuilder cardJpaViewBuilder;

	@Autowired
	private MerchantJpaViewBuilder merchantJpaViewBuilder;
}