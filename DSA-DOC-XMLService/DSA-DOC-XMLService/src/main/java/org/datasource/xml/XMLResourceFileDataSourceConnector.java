package org.datasource.xml;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.StandardCopyOption;
import java.util.logging.Logger;

@Component
public class XMLResourceFileDataSourceConnector {
	private static Logger logger = Logger.getLogger(XMLResourceFileDataSourceConnector.class.getName());

	@Value("${xml.data.source.file.path}")
	protected String XMLFilePath;
	//
	protected File XMLFile;

	public File getXMLFile() throws Exception {
		logger.info("Filepath accessed: " + this.XMLFilePath);
		if (this.XMLFile == null) {
			this.XMLFile = new File(this.XMLFilePath);
			if(!this.XMLFile.exists()){
				this.XMLFile = new File("temp.xml");
				java.nio.file.Files.copy(
						new ClassPathResource(this.XMLFilePath).getInputStream(),
						this.XMLFile.toPath(),
						StandardCopyOption.REPLACE_EXISTING);
				logger.info("... loaded from ClassPathResource!");
			}else
				logger.info("... loaded from local FileSystem!");
		}
		return XMLFile;
	}
}
