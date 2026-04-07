package org.datasource;

import org.datasource.xml.XMLResourceFileDataSourceConnector;
import org.datasource.xml.locations.CityView;
import org.datasource.xml.locations.DepartamentView;
import org.datasource.xml.locations.DepartamentsListView;
import org.datasource.xml.locations.DepartamentsViewBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.logging.Logger;

/*	REST Service URL
	http://localhost:8092/DSA-DOC-XMLService/rest/locations/DepartamentCityView
	http://localhost:8092/DSA-DOC-XMLService/rest/locations/DepartamentView
	http://localhost:8092/DSA-DOC-XMLService/rest/locations/CityView
*/
@RestController @RequestMapping("/locations")
public class RESTViewServiceXML{
	private static Logger logger = Logger.getLogger(RESTViewServiceXML.class.getName());
	
	@RequestMapping(value = "/ping", method = RequestMethod.GET,
		produces = {MediaType.TEXT_PLAIN_VALUE})
	@ResponseBody
	public String testDataSource() {
		logger.info(">>>> REST XML Data Source is Up!");
		return "PING response from JDBCDataSource!";
	}
	
	@RequestMapping(value = "/DepartamentCityView", method = RequestMethod.GET,
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public DepartamentsListView get_DepartamentCityView() throws Exception {
		return this.viewBuilder.build().getDepartamentsListView();
	}
	
	@RequestMapping(value = "/DepartamentView", method = RequestMethod.GET,
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<DepartamentView> get_DepartamentView() throws Exception {
		return this.viewBuilder.build().getDepartamentsViewList();
	}
	
	@RequestMapping(value = "/CityView", method = RequestMethod.GET,
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<CityView> get_CityView() throws Exception {
		return this.viewBuilder.build().getCitiesViewList();
	}
	
	@RequestMapping(value = "/DepartamentsListView", method = RequestMethod.GET,
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public DepartamentsListView get_DepartamentsListView() throws Exception {
		DepartamentsListView listView = this.viewBuilder.build().getDepartamentsListView();
		return listView;
	}
	// Set-up 
	@Autowired private XMLResourceFileDataSourceConnector xmlDataSourceConnector;
	@Autowired private DepartamentsViewBuilder viewBuilder;
	
}