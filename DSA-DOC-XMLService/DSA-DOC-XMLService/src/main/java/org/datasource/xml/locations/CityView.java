package org.datasource.xml.locations;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.xml.bind.annotation.*;
import java.io.Serializable;

@XmlRootElement @XmlAccessorType(XmlAccessType.FIELD)
@Data @AllArgsConstructor @NoArgsConstructor(force = true)
public class CityView implements Serializable{
	private Long idCity;
	private String cityName;
	private String postalCode;
}