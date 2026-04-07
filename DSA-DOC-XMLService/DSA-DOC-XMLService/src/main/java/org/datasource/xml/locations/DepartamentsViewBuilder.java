package org.datasource.xml.locations;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import org.datasource.xml.XMLResourceFileDataSourceConnector;
import org.springframework.stereotype.Service;

import jakarta.xml.bind.annotation.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class DepartamentsViewBuilder {
	private static Logger logger = Logger.getLogger(DepartamentsViewBuilder.class.getName());
	// Data cache
	private DepartamentsListView departamentsListView;
	private List<DepartamentView> departamentsViewList;
	private List<CityView> citiesViewList;
	
	public List<DepartamentView> getDepartamentsViewList() {
		return departamentsViewList;
	}
	
	public List<CityView> getCitiesViewList() {
		return citiesViewList;
	}

	public DepartamentsListView getDepartamentsListView() {
		return departamentsListView;
	}
	
	// Support Infrastructure
	private XMLResourceFileDataSourceConnector dataSourceConnector;
	private File xmlFile;
	
	public DepartamentsViewBuilder(XMLResourceFileDataSourceConnector dataSourceConnector) throws Exception {
		this.dataSourceConnector = dataSourceConnector;
		xmlFile = dataSourceConnector.getXMLFile();
	}
	
	// Builder Workflow
	public DepartamentsViewBuilder build() throws Exception{
		return this.select().map();
	}


	private DepartamentsViewBuilder map() {
		this.departamentsViewList = this.departamentsListView.getDepartaments();
		this.citiesViewList = new ArrayList<>();

		for(DepartamentView departamentView: departamentsViewList){
			this.citiesViewList.addAll(departamentView.getCities());
		}

		return this;
	}


	public DepartamentsViewBuilder select() throws Exception {
		JAXBContext jaxbContext = JAXBContext.newInstance(DepartamentsListView.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		this.departamentsListView = (DepartamentsListView) jaxbUnmarshaller.unmarshal(xmlFile);
		//this.departamentsListView = jaxbUnmarshaller.unmarshal(xmlFile, DepartamentsListView.class);

		logger.info("DepartamentsViewBuilder: departamentsListView="+departamentsListView);
		return this;
	}
}