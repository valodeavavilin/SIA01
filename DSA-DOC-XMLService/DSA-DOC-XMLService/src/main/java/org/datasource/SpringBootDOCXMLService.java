package org.datasource;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import java.util.logging.Logger;

/*
* java -jar DSA-DOC-XMLService-2025.1.jar --xml.data.source.file.path=/home/catalin/data/Locations.xml
 */
@SpringBootApplication (exclude={DataSourceAutoConfiguration.class} )
public class SpringBootDOCXMLService extends SpringBootServletInitializer {
	private static Logger logger = Logger.getLogger(SpringBootDOCXMLService.class.getName());
	
	public static void main(String[] args) {
		logger.info("Loading ... SpringBootDOCXMLService Default Settings ... XML");
		SpringApplication.run(SpringBootDOCXMLService.class, args);
	}

	/*
	* By default, Java Beans to XML Documents conversion is done by using Jackson Mapper.
	*
	* To take into consideration JAXB Annonations when generate XML Documents,
	* the JaxbAnnotationModule could be activated so that the following annotations are considered:
		* @XmlRootElement
		* @XmlElement
		* @XmlElementWrapper
	* There are some implications on JSON Documents generation, e.g. @XmlElement

	@Bean
	public Jackson2ObjectMapperBuilderCustomizer jacksonCustomizer() {
	    return (mapperBuilder) -> mapperBuilder.modulesToInstall(new JaxbAnnotationModule());
	}
	*/
}