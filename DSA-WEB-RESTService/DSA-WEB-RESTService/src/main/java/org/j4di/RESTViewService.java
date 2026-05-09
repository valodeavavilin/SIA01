package org.j4di;

import org.j4di.access.views.postgresql.CardLimitsView;
import org.j4di.access.views.postgresql.CardLimitsViewRepository;
import org.j4di.access.views.postgresql.CardSecurityView;
import org.j4di.access.views.postgresql.CardSecurityViewRepository;
import org.j4di.access.views.postgresql.TransactionsView;
import org.j4di.access.views.postgresql.TransactionsViewRepository;
import org.j4di.access.views.postgresql.CardsJpaView;
import org.j4di.access.views.postgresql.CardsJpaViewRepository;
import org.j4di.access.views.postgresql.MerchantsJpaView;
import org.j4di.access.views.postgresql.MerchantsJpaViewRepository;

import org.j4di.access.views.xlsx.CustomerView;
import org.j4di.access.views.xlsx.CustomerViewRepository;
import org.j4di.access.views.xlsx.CustomerFinanceView;
import org.j4di.access.views.xlsx.CustomerFinanceViewRepository;

import org.j4di.access.views.mongodb.TransactionsMongoView;
import org.j4di.access.views.mongodb.TransactionsMongoViewRepository;
import org.j4di.access.views.mongodb.CustomerRiskMongoView;
import org.j4di.access.views.mongodb.CustomerRiskMongoViewRepository;

import org.j4di.integration.views.IntCustomerProfileView;
import org.j4di.integration.views.IntCustomerProfileViewRepository;
import org.j4di.integration.views.IntCardProfileView;
import org.j4di.integration.views.IntCardProfileViewRepository;
import org.j4di.integration.views.IntTransactionsBaseView;
import org.j4di.integration.views.IntTransactionsBaseViewRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

/*
    REST Service URL

    Base:
    http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/ping

    Access / Source Views:

    PostgreSQL JDBC:
    http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/access/postgresql/card-limits
    http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/access/postgresql/card-security
    http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/access/postgresql/transactions

    PostgreSQL JPA:
    http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/access/postgresql/cards
    http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/access/postgresql/merchants

    XLSX:
    http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/access/xlsx/customers
    http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/access/xlsx/customer-finance

    MongoDB:
    http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/access/mongodb/transactions
    http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/access/mongodb/customer-risk

    Integration Views:
    http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/integration/customer-profile
	http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/integration/card-profile
	http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/integration/transactions-base
*/
@RestController
@RequestMapping("/OLAP")
public class RESTViewService {

	private static final Logger logger = Logger.getLogger(RESTViewService.class.getName());

	@RequestMapping(value = "/ping", method = RequestMethod.GET,
			produces = {MediaType.TEXT_PLAIN_VALUE})
	@ResponseBody
	public String pingDataSource() {
		logger.info(">>>> DSA-WEB-RESTService:: RESTViewService is Up!");
		return "Ping response from DSA-WEB-RESTService!";
	}

	// -------------------------------------------------------------------------
	// PostgreSQL JDBC access views
	// -------------------------------------------------------------------------

	@Autowired
	private CardLimitsViewRepository cardLimitsViewRepository;

	@Autowired
	private CardSecurityViewRepository cardSecurityViewRepository;

	@Autowired
	private TransactionsViewRepository transactionsViewRepository;

	@GetMapping(value = "/access/postgresql/card-limits",
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<CardLimitsView> getCardLimitsView() {
		return cardLimitsViewRepository.getCardLimitsView();
	}

	@GetMapping(value = "/access/postgresql/card-security",
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<CardSecurityView> getCardSecurityView() {
		return cardSecurityViewRepository.getCardSecurityView();
	}

	@GetMapping(value = "/access/postgresql/transactions",
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<TransactionsView> getTransactionsView() {
		return transactionsViewRepository.getTransactionsView();
	}

	// -------------------------------------------------------------------------
	// PostgreSQL JPA access views
	// -------------------------------------------------------------------------

	@Autowired
	private CardsJpaViewRepository cardsJpaViewRepository;

	@Autowired
	private MerchantsJpaViewRepository merchantsJpaViewRepository;

	@GetMapping(value = "/access/postgresql/cards",
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<CardsJpaView> getCardsJpaView() {
		return cardsJpaViewRepository.getCardsJpaView();
	}

	@GetMapping(value = "/access/postgresql/merchants",
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<MerchantsJpaView> getMerchantsJpaView() {
		return merchantsJpaViewRepository.getMerchantsJpaView();
	}

	// -------------------------------------------------------------------------
	// XLSX access views
	// -------------------------------------------------------------------------

	@Autowired
	private CustomerViewRepository customerViewRepository;

	@Autowired
	private CustomerFinanceViewRepository customerFinanceViewRepository;

	@GetMapping(value = "/access/xlsx/customers",
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<CustomerView> getCustomerView() {
		return customerViewRepository.getCustomerView();
	}

	@GetMapping(value = "/access/xlsx/customer-finance",
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<CustomerFinanceView> getCustomerFinanceView() {
		return customerFinanceViewRepository.getCustomerFinanceView();
	}

	// -------------------------------------------------------------------------
	// MongoDB access views
	// -------------------------------------------------------------------------

	@Autowired
	private TransactionsMongoViewRepository transactionsMongoViewRepository;

	@Autowired
	private CustomerRiskMongoViewRepository customerRiskMongoViewRepository;

	@GetMapping(value = "/access/mongodb/transactions",
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<TransactionsMongoView> getTransactionsMongoView() {
		return transactionsMongoViewRepository.getTransactionsMongoView();
	}

	@GetMapping(value = "/access/mongodb/customer-risk",
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<CustomerRiskMongoView> getCustomerRiskMongoView() {
		return customerRiskMongoViewRepository.getCustomerRiskMongoView();
	}

	// -------------------------------------------------------------------------
	// Integration views
	// -------------------------------------------------------------------------

	@Autowired
	private IntCustomerProfileViewRepository intCustomerProfileViewRepository;

	@Autowired
	private IntCardProfileViewRepository intCardProfileViewRepository;

	@Autowired
	private IntTransactionsBaseViewRepository intTransactionsBaseViewRepository;

	@GetMapping(value = "/integration/customer-profile",
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<IntCustomerProfileView> getIntCustomerProfileView() {
		return intCustomerProfileViewRepository.getIntCustomerProfileView();
	}

	@GetMapping(value = "/integration/card-profile",
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<IntCardProfileView> getIntCardProfileView() {
		return intCardProfileViewRepository.getIntCardProfileView();
	}

	@GetMapping(value = "/integration/transactions-base",
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<IntTransactionsBaseView> getIntTransactionsBaseView() {
		return intTransactionsBaseViewRepository.getIntTransactionsBaseView();
	}
}