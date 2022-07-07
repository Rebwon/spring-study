package com.rebwon.toby.springbook.dao;

import static org.assertj.core.api.Assertions.assertThat;

import com.rebwon.toby.springbook.domain.Group;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class GroupDaoTest {

    @PersistenceContext
    EntityManager em;
    @Autowired
    GroupDao groupDao;
    Group group1;
    Group group2;
    Group group3;

    @BeforeEach
    public void before() {
        group1 = new Group(0, "group1");
        group2 = new Group(0, "group2");
        group3 = new Group(0, "group2-1");
    }

    @Test
    public void crud() {
        groupDao.deleteAll();
        assertThat(groupDao.count()).isEqualTo(0L);

        // C
        groupDao.add(group1);
        assertThat(groupDao.count()).isEqualTo(1L);
        assertThat(group1.getId() > 0).isTrue();

        groupDao.add(group2);
        groupDao.add(group3);
        assertThat(groupDao.count()).isEqualTo(3L);

        // R
        assertThat(groupDao.get(group1.getId())).isEqualTo(group1);
        assertThat(groupDao.get(group2.getId())).isEqualTo(group2);
        assertThat(groupDao.get(group3.getId())).isEqualTo(group3);

        // U
        group1.setName("modified1");
        group1 = groupDao.update(group1);
        em.flush();
        Group updatedGroup1 = groupDao.get(group1.getId());
        assertThat(updatedGroup1.getName()).isEqualTo("modified1");

        // D
        groupDao.delete(group1.getId());
        assertThat(groupDao.count()).isEqualTo(2L);
        assertThat(groupDao.get(group1.getId())).isNull();
    }

    @Test
    public void search() {
        groupDao.deleteAll();
        groupDao.add(group1);
        groupDao.add(group2);
        groupDao.add(group3);

        assertThat(groupDao.search("abc").size()).isEqualTo(0);
        assertThat(groupDao.search("oup1").size()).isEqualTo(1);
        assertThat(groupDao.search("up2").size()).isEqualTo(2);
        assertThat(groupDao.search("group").size()).isEqualTo(3);
    }

}
