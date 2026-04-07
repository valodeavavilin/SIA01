package org.datasource.xml.locations;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement @XmlAccessorType(XmlAccessType.FIELD)
@Data @AllArgsConstructor @NoArgsConstructor(force = true)
public class DepartamentView implements Serializable{
	private Long idDepartament;
	private String departamentName;
	private String departamentCode;
	private String countryName;

	@XmlElementWrapper(name="cities") @XmlElement(name="city")
	private List<CityView> cities = new ArrayList<>();
}