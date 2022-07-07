package com.rebwon.toby.springbook.dao;

import static org.assertj.core.api.Assertions.assertThat;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@SpringBootTest
@Transactional
public class EntityManagerTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    PlatformTransactionManager transactionManager;

    @Test
    void name() {
        TransactionStatus status = transactionManager.getTransaction(
            new DefaultTransactionDefinition());
        assertThat(em.isOpen()).isTrue();
        transactionManager.commit(status);
    }
}
