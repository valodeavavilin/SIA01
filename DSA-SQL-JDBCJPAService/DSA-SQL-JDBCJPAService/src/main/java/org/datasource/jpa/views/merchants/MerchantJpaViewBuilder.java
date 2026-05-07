package org.datasource.jpa.views.merchants;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.datasource.jpa.JPADataSourceConnector;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class MerchantJpaViewBuilder {
    private static Logger logger = Logger.getLogger(MerchantJpaViewBuilder.class.getName());

    protected String JPQL_MERCHANTS_SELECT =
            "SELECT NEW org.datasource.jpa.views.merchants.MerchantJpaView(" +
                    "m.merchantId, m.merchantCity, m.merchantState, m.zip) " +
                    "FROM MerchantJpaView m";

    protected List<MerchantJpaView> merchantJpaViewList = new ArrayList<>();

    public List<MerchantJpaView> getMerchantJpaViewList() {
        return merchantJpaViewList;
    }

    public MerchantJpaViewBuilder build() {
        return this.select();
    }

    protected MerchantJpaViewBuilder select() {
        EntityManager em = dataSourceConnector.getEntityManager();
        logger.info("Execute JPQL: " + JPQL_MERCHANTS_SELECT);

        Query viewQuery = em.createQuery(JPQL_MERCHANTS_SELECT);
        this.merchantJpaViewList = viewQuery.getResultList();

        return this;
    }

    protected JPADataSourceConnector dataSourceConnector;

    public MerchantJpaViewBuilder(JPADataSourceConnector dataSourceConnector) {
        this.dataSourceConnector = dataSourceConnector;
    }
}