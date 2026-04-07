package org.datasource.xml.locations;

import jakarta.xml.bind.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;


@XmlRootElement(name="departaments") @XmlAccessorType(XmlAccessType.FIELD)
@Data @AllArgsConstructor @NoArgsConstructor(force = true)
public class DepartamentsListView {
	@XmlElement(name="departament")
	private List<DepartamentView> departaments = new ArrayList<>();
}