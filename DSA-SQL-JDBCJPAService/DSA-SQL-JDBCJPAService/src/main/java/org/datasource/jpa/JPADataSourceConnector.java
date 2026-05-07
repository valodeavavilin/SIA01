package org.datasource.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;

@Service @ApplicationScope
public class JPADataSourceConnector {
	@PersistenceContext
	private EntityManager em;

	public EntityManager getEntityManager(){
		return em;
	}
	
}