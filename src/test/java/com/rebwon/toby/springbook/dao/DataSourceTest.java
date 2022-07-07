package com.rebwon.toby.springbook.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@SpringBootTest
@Transactional
public class DataSourceTest {

    @Autowired
    DataSource dataSource;

    @Autowired
    PlatformTransactionManager transactionManager;

    @Test
    void connection() throws SQLException {
        try (Connection c = dataSource.getConnection()) {
            assertThat(c.isClosed()).isFalse();
        }
    }

    @Test
    void transaction() throws Exception {
        TransactionStatus tx = transactionManager.getTransaction(
            new DefaultTransactionDefinition());
        transactionManager.commit(tx);
        assertThat(tx.isCompleted()).isTrue();
    }
}
