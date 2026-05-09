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

import org.j4di.dimensional.views.DimClientView;
import org.j4di.dimensional.views.DimClientViewRepository;
import org.j4di.dimensional.views.DimCardView;
import org.j4di.dimensional.views.DimCardViewRepository;
import org.j4di.dimensional.views.DimTimeView;
import org.j4di.dimensional.views.DimTimeViewRepository;
import org.j4di.dimensional.views.DimMerchantGeoView;
import org.j4di.dimensional.views.DimMerchantGeoViewRepository;

import org.j4di.fact.views.FactTransactionsView;
import org.j4di.fact.views.FactTransactionsViewRepository;
import org.j4di.fact.views.FactTransactionsEnrichedView;
import org.j4di.fact.views.FactTransactionsEnrichedViewRepository;

import org.j4di.analytical.views.*;

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

	Dimension Views:
	http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/dimensional/client
    http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/dimensional/card
    http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/dimensional/time
    http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/dimensional/merchant-geo

    Fact Views:
    http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/fact/transactions
	http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/fact/transactions-enriched

	Analytycal:
	http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/analytical/txn-calendar
	http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/analytical/merchant-geo
	http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/analytical/credit-age
	http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/analytical/card-brand-type
	http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/analytical/source-channel
	http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/analytical/state-brand-cube
	http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/analytical/income-debt
	http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/analytical/card-security-cube
	http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/analytical/year-state-source-gsets
	http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/analytical/month-income-mcc-gsets

	http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/window/running-total-client
	http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/window/client-avg-diff
	http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/window/card-total-rank
	http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/window/month-share-running
	http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/window/client-first-last-top
	http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/window/merchant-state-rank
	http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/window/credit-month-performance

	http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/advanced/pivot-txn-source-month
	http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/advanced/txn-amount-distribution
	http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/advanced/txn-risk-statistics
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

	// -------------------------------------------------------------------------
	// Dimensional views
	// -------------------------------------------------------------------------

	@Autowired
	private DimClientViewRepository dimClientViewRepository;

	@Autowired
	private DimCardViewRepository dimCardViewRepository;

	@Autowired
	private DimTimeViewRepository dimTimeViewRepository;

	@Autowired
	private DimMerchantGeoViewRepository dimMerchantGeoViewRepository;

	@GetMapping(value = "/dimensional/client",
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<DimClientView> getDimClientView() {
		return dimClientViewRepository.getDimClientView();
	}

	@GetMapping(value = "/dimensional/card",
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<DimCardView> getDimCardView() {
		return dimCardViewRepository.getDimCardView();
	}

	@GetMapping(value = "/dimensional/time",
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<DimTimeView> getDimTimeView() {
		return dimTimeViewRepository.getDimTimeView();
	}

	@GetMapping(value = "/dimensional/merchant-geo",
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<DimMerchantGeoView> getDimMerchantGeoView() {
		return dimMerchantGeoViewRepository.getDimMerchantGeoView();
	}

	// -------------------------------------------------------------------------
	// Fact views
	// -------------------------------------------------------------------------

	@Autowired
	private FactTransactionsViewRepository factTransactionsViewRepository;

	@Autowired
	private FactTransactionsEnrichedViewRepository factTransactionsEnrichedViewRepository;

	@GetMapping(value = "/fact/transactions",
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<FactTransactionsView> getFactTransactionsView() {
		return factTransactionsViewRepository.getFactTransactionsView();
	}

	@GetMapping(value = "/fact/transactions-enriched",
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<FactTransactionsEnrichedView> getFactTransactionsEnrichedView() {
		return factTransactionsEnrichedViewRepository.getFactTransactionsEnrichedView();
	}

	// -------------------------------------------------------------------------
	// OLAP analytical views
	// -------------------------------------------------------------------------

	@Autowired
	private OlapTxnCalendarViewRepository olapTxnCalendarViewRepository;

	@Autowired
	private OlapTxnMerchantGeoViewRepository olapTxnMerchantGeoViewRepository;

	@Autowired
	private OlapTxnCreditAgeViewRepository olapTxnCreditAgeViewRepository;

	@Autowired
	private OlapTxnCardBrandTypeViewRepository olapTxnCardBrandTypeViewRepository;

	@Autowired
	private OlapTxnSourceChannelViewRepository olapTxnSourceChannelViewRepository;

	@Autowired
	private OlapTxnStateBrandCubeViewRepository olapTxnStateBrandCubeViewRepository;

	@Autowired
	private OlapTxnIncomeDebtViewRepository olapTxnIncomeDebtViewRepository;

	@Autowired
	private OlapTxnCardSecurityCubeViewRepository olapTxnCardSecurityCubeViewRepository;

	@Autowired
	private OlapTxnYearStateSourceGsetsViewRepository olapTxnYearStateSourceGsetsViewRepository;

	@Autowired
	private OlapTxnMonthIncomeMccGsetsViewRepository olapTxnMonthIncomeMccGsetsViewRepository;


	@GetMapping(value = "/analytical/txn-calendar",
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<OlapTxnCalendarView> getOlapTxnCalendarView() {
		return olapTxnCalendarViewRepository.getOlapTxnCalendarView();
	}

	@GetMapping(value = "/analytical/merchant-geo",
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<OlapTxnMerchantGeoView> getOlapTxnMerchantGeoView() {
		return olapTxnMerchantGeoViewRepository.getOlapTxnMerchantGeoView();
	}

	@GetMapping(value = "/analytical/credit-age",
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<OlapTxnCreditAgeView> getOlapTxnCreditAgeView() {
		return olapTxnCreditAgeViewRepository.getOlapTxnCreditAgeView();
	}

	@GetMapping(value = "/analytical/card-brand-type",
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<OlapTxnCardBrandTypeView> getOlapTxnCardBrandTypeView() {
		return olapTxnCardBrandTypeViewRepository.getOlapTxnCardBrandTypeView();
	}

	@GetMapping(value = "/analytical/source-channel",
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<OlapTxnSourceChannelView> getOlapTxnSourceChannelView() {
		return olapTxnSourceChannelViewRepository.getOlapTxnSourceChannelView();
	}

	@GetMapping(value = "/analytical/state-brand-cube",
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<OlapTxnStateBrandCubeView> getOlapTxnStateBrandCubeView() {
		return olapTxnStateBrandCubeViewRepository.getOlapTxnStateBrandCubeView();
	}

	@GetMapping(value = "/analytical/income-debt",
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<OlapTxnIncomeDebtView> getOlapTxnIncomeDebtView() {
		return olapTxnIncomeDebtViewRepository.getOlapTxnIncomeDebtView();
	}

	@GetMapping(value = "/analytical/card-security-cube",
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<OlapTxnCardSecurityCubeView> getOlapTxnCardSecurityCubeView() {
		return olapTxnCardSecurityCubeViewRepository.getOlapTxnCardSecurityCubeView();
	}

	@GetMapping(value = "/analytical/year-state-source-gsets",
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<OlapTxnYearStateSourceGsetsView> getOlapTxnYearStateSourceGsetsView() {
		return olapTxnYearStateSourceGsetsViewRepository.getOlapTxnYearStateSourceGsetsView();
	}

	@GetMapping(value = "/analytical/month-income-mcc-gsets",
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<OlapTxnMonthIncomeMccGsetsView> getOlapTxnMonthIncomeMccGsetsView() {
		return olapTxnMonthIncomeMccGsetsViewRepository.getOlapTxnMonthIncomeMccGsetsView();
	}


	// -------------------------------------------------------------------------
	// Window analytical views
	// -------------------------------------------------------------------------

	@Autowired
	private WvTxnRunningTotalClientViewRepository wvTxnRunningTotalClientViewRepository;

	@Autowired
	private WvTxnClientAvgDiffViewRepository wvTxnClientAvgDiffViewRepository;

	@Autowired
	private WvCardTotalRankViewRepository wvCardTotalRankViewRepository;

	@Autowired
	private WvTxnMonthShareRunningViewRepository wvTxnMonthShareRunningViewRepository;

	@Autowired
	private WvTxnClientFirstLastTopViewRepository wvTxnClientFirstLastTopViewRepository;

	@Autowired
	private WvMerchantStateRankViewRepository wvMerchantStateRankViewRepository;

	@Autowired
	private WvCreditMonthPerformanceViewRepository wvCreditMonthPerformanceViewRepository;


	@GetMapping(value = "/window/running-total-client",
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<WvTxnRunningTotalClientView> getWvTxnRunningTotalClientView() {
		return wvTxnRunningTotalClientViewRepository.getWvTxnRunningTotalClientView();
	}

	@GetMapping(value = "/window/client-avg-diff",
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<WvTxnClientAvgDiffView> getWvTxnClientAvgDiffView() {
		return wvTxnClientAvgDiffViewRepository.getWvTxnClientAvgDiffView();
	}

	@GetMapping(value = "/window/card-total-rank",
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<WvCardTotalRankView> getWvCardTotalRankView() {
		return wvCardTotalRankViewRepository.getWvCardTotalRankView();
	}

	@GetMapping(value = "/window/month-share-running",
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<WvTxnMonthShareRunningView> getWvTxnMonthShareRunningView() {
		return wvTxnMonthShareRunningViewRepository.getWvTxnMonthShareRunningView();
	}

	@GetMapping(value = "/window/client-first-last-top",
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<WvTxnClientFirstLastTopView> getWvTxnClientFirstLastTopView() {
		return wvTxnClientFirstLastTopViewRepository.getWvTxnClientFirstLastTopView();
	}

	@GetMapping(value = "/window/merchant-state-rank",
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<WvMerchantStateRankView> getWvMerchantStateRankView() {
		return wvMerchantStateRankViewRepository.getWvMerchantStateRankView();
	}

	@GetMapping(value = "/window/credit-month-performance",
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<WvCreditMonthPerformanceView> getWvCreditMonthPerformanceView() {
		return wvCreditMonthPerformanceViewRepository.getWvCreditMonthPerformanceView();
	}


	// -------------------------------------------------------------------------
	// Advanced analytical views
	// -------------------------------------------------------------------------

	@Autowired
	private AdvPivotTxnSourceMonthViewRepository advPivotTxnSourceMonthViewRepository;

	@Autowired
	private AdvTxnAmountDistributionViewRepository advTxnAmountDistributionViewRepository;

	@Autowired
	private AdvTxnRiskStatisticsViewRepository advTxnRiskStatisticsViewRepository;


	@GetMapping(value = "/advanced/pivot-txn-source-month",
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<AdvPivotTxnSourceMonthView> getAdvPivotTxnSourceMonthView() {
		return advPivotTxnSourceMonthViewRepository.getAdvPivotTxnSourceMonthView();
	}

	@GetMapping(value = "/advanced/txn-amount-distribution",
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<AdvTxnAmountDistributionView> getAdvTxnAmountDistributionView() {
		return advTxnAmountDistributionViewRepository.getAdvTxnAmountDistributionView();
	}

	@GetMapping(value = "/advanced/txn-risk-statistics",
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<AdvTxnRiskStatisticsView> getAdvTxnRiskStatisticsView() {
		return advTxnRiskStatisticsViewRepository.getAdvTxnRiskStatisticsView();
	}
}