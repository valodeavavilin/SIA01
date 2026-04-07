package org.datasource;

import org.datasource.poi.custcategories.CustomerCategoryView;
import org.datasource.poi.custcategories.CustomerEmpCategoryViewBuilder;
import org.datasource.poi.custcategories.CustomerTurnoverCategoryViewBuilder;
import org.datasource.poi.periods.TimePeriodView;
import org.datasource.poi.periods.TimePeriodViewBuilder;
import org.datasource.poi.prodcategories.ProductCategoryHierarchyView;
import org.datasource.poi.prodcategories.ProductCategoryHierarchyViewBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.logging.Logger;


/*	REST Service URL
	http://localhost:8094/DSA-DOC-XLSService/rest/customers/CustomerTurnoverCategoryView
	http://localhost:8094/DSA-DOC-XLSService/rest/customers/CustomerEmployeesCategoryView

	http://localhost:8094/DSA-DOC-XLSService/rest/customers/TimePeriodView
	http://localhost:8094/DSA-DOC-XLSService/rest/customers/ProductCategoryHierarchyView
*/
@RestController @RequestMapping("/customers")
public class RESTViewServiceXLS {
	private static Logger logger = Logger.getLogger(RESTViewServiceXLS.class.getName());
	
	@RequestMapping(value = "/ping", method = RequestMethod.GET,
			produces = {MediaType.TEXT_PLAIN_VALUE})
	@ResponseBody
	public String pingDataSource() {
		logger.info(">>>> REST XML Data Source is Up!");
		return "PING response from JDBCDataSource!";
	}
	
	@RequestMapping(value = "/CustomerTurnoverCategoryView", method = RequestMethod.GET,
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<CustomerCategoryView> get_CustomerTurnoverCategoryView() throws Exception {
		List<CustomerCategoryView> viewList = this.turnoverCategoriesViewBuilder.build().getViewList();
		return viewList;
	}

	@RequestMapping(value = "/CustomerEmployeesCategoryView", method = RequestMethod.GET,
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<CustomerCategoryView> get_CustomerEmployeesCategoryView() throws Exception {
		List<CustomerCategoryView> viewList = this.employeesCategoriesViewBuilder.build().getViewList();
		return viewList;
	}

	@RequestMapping(value = "/TimePeriodView", method = RequestMethod.GET,
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<TimePeriodView> get_TimePeriodView() throws Exception {
		List<TimePeriodView> viewList = this.timePeriodViewBuilder.build().getViewList();
		return viewList;
	}

	@RequestMapping(value = "/ProductCategoryHierarchyView", method = RequestMethod.GET,
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<ProductCategoryHierarchyView> get_ProductCategoryHierarchyView() throws Exception {
		System.out.println( "RESTViewServiceXLS::get_CustomerCategoryHierarchyView()");
		List<ProductCategoryHierarchyView> viewList = this.productCategoryHierarchyViewBuilder.build().getViewList();
		return viewList;
	}

	// Set-up
	@Autowired private CustomerTurnoverCategoryViewBuilder turnoverCategoriesViewBuilder;
	@Autowired private CustomerEmpCategoryViewBuilder employeesCategoriesViewBuilder;
	@Autowired private TimePeriodViewBuilder timePeriodViewBuilder;
	@Autowired private ProductCategoryHierarchyViewBuilder productCategoryHierarchyViewBuilder;
}