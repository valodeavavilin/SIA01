package org.datasource.jpa.views.cards;

import org.datasource.jpa.JPADataSourceConnector;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class CardJpaViewBuilder {
    private static Logger logger = Logger.getLogger(CardJpaViewBuilder.class.getName());

    protected String JPQL_CARDS_SELECT =
            "SELECT NEW org.datasource.jpa.views.cards.CardJpaView(" +
                    "c.cardId, c.clientId, c.cardBrand, c.cardType, c.expires, c.acctOpenDate, c.numCardsIssued) " +
                    "FROM CardJpaView c";

    protected List<CardJpaView> cardJpaViewList = new ArrayList<>();

    public List<CardJpaView> getCardJpaViewList() {
        return cardJpaViewList;
    }

    public CardJpaViewBuilder build() {
        return this.select();
    }

    protected CardJpaViewBuilder select() {
        EntityManager em = dataSourceConnector.getEntityManager();
        logger.info("Execute JPQL: " + JPQL_CARDS_SELECT);

        Query viewQuery = em.createQuery(JPQL_CARDS_SELECT);
        this.cardJpaViewList = viewQuery.getResultList();

        return this;
    }

    protected JPADataSourceConnector dataSourceConnector;

    public CardJpaViewBuilder(JPADataSourceConnector dataSourceConnector) {
        this.dataSourceConnector = dataSourceConnector;
    }
}