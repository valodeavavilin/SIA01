package org.datasource;

import org.datasource.poi.customers.CustomerView;
import org.datasource.poi.customers.CustomerViewBuilder;
import org.datasource.poi.customerfinance.CustomerFinanceView;
import org.datasource.poi.customerfinance.CustomerFinanceViewBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.logging.Logger;

/*  REST Service URL
    http://localhost:8094/DSA-DOC-XLSService/rest/customers/CustomerView
    http://localhost:8094/DSA-DOC-XLSService/rest/customers/CustomerFinanceView
*/
@RestController
@RequestMapping("/customers")
public class RESTViewServiceXLS {
	private static final Logger logger = Logger.getLogger(RESTViewServiceXLS.class.getName());

	@RequestMapping(
			value = "/ping",
			method = RequestMethod.GET,
			produces = {MediaType.TEXT_PLAIN_VALUE}
	)
	@ResponseBody
	public String pingDataSource() {
		logger.info(">>>> REST XLS Data Source is Up!");
		return "PING response from DSA-DOC-XLSService!";
	}

	@RequestMapping(
			value = "/CustomerView",
			method = RequestMethod.GET,
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
	)
	@ResponseBody
	public List<CustomerView> getCustomerView() throws Exception {
		return this.customerViewBuilder.build().getViewList();
	}

	@RequestMapping(
			value = "/CustomerFinanceView",
			method = RequestMethod.GET,
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
	)
	@ResponseBody
	public List<CustomerFinanceView> getCustomerFinanceView() throws Exception {
		return this.customerFinanceViewBuilder.build().getViewList();
	}

	@Autowired
	private CustomerViewBuilder customerViewBuilder;

	@Autowired
	private CustomerFinanceViewBuilder customerFinanceViewBuilder;
}