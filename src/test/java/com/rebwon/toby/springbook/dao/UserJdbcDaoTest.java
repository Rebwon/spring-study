package com.rebwon.toby.springbook.dao;

import static org.assertj.core.api.Assertions.assertThat;

import com.rebwon.toby.springbook.domain.Group;
import com.rebwon.toby.springbook.domain.Type;
import com.rebwon.toby.springbook.domain.User;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class UserJdbcDaoTest {

    @PersistenceContext
    EntityManager em;
    @Autowired
    UserDao userDao;
    @Autowired
    GroupDao groupDao;
    Group group1;
    Group group2;
    Date day1;
    Date day2;
    Date day3;
    User user1;
    User user2;
    User user3;

    @BeforeEach
    public void before() {
        group1 = new Group(0, "group1");
        group2 = new Group(0, "group2");
        day1 = new GregorianCalendar(2010, 1 - 1, 1).getTime();
        day2 = new GregorianCalendar(2010, 2 - 1, 1).getTime();
        day3 = new GregorianCalendar(2010, 3 - 1, 1).getTime();
        user1 = new User(0, "name1", "username1", "password1", Type.ADMIN, group1, day1, day2, 1);
        user2 = new User(0, "name2", "username2", "password2", Type.GUEST, group2, day2, day3, 10);
        user3 = new User(0, "name3", "username3", "password3", Type.MEMBER, group2, day1, day3,
            100);
    }

    private void init() {
        userDao.deleteAll();
        groupDao.deleteAll();
        groupDao.add(group1);
        groupDao.add(group2);
        em.flush();
        em.clear();
    }

    @Test
    public void crud() {
        init();

        // C
        assertThat(userDao.count()).isEqualTo(0L);
        userDao.add(user1);
        assertThat(user1.getId() > 0).isTrue();
        assertThat(userDao.count()).isEqualTo(1L);
        userDao.add(user2);
        assertThat(userDao.count()).isEqualTo(2L);
        userDao.add(user3);
        assertThat(userDao.count()).isEqualTo(3L);

        // R
        User u1 = userDao.get(user1.getId());
        compareUserProperties(user1, u1);
        User u2 = userDao.get(user2.getId());
        compareUserProperties(user2, u2);

        // U
        user1.setName("mName");
        user1.setUsername("mPassword");
        user1.setPassword("mPassword");
        user1.setCreated(new GregorianCalendar(1999, 9 - 1, 9).getTime());
        user1.setModified(new GregorianCalendar(1999, 9 - 1, 9).getTime());
        user1.setLogins(999);
        user1.setType(Type.GUEST);
        user1.setGroup(group2);
//        userDao.update(user1);
//        User u3 = userDao.get(user1.getId());
//        compareUserProperties(user1, u3);
//        User u4 = userDao.get(user2.getId());
//        compareUserProperties(user2, u4);

        // D
        userDao.delete(user1.getId());
        assertThat(userDao.count()).isEqualTo(2L);
        assertThat(userDao.get(user1.getId())).isNull();
    }

    @Test
    public void findUser() {
        init();
        userDao.add(user1);
        userDao.add(user2);
        assertThat(userDao.findUser(user1.getUsername())).isEqualTo(user1);
        assertThat(userDao.findUser(user2.getUsername())).isEqualTo(user2);
        assertThat(userDao.findUser("asdf1234")).isNull();
    }

    private void compareUserProperties(User u1, User u2) {
        assertThat(u1.getId()).isEqualTo(u2.getId());
        assertThat(u1.getName()).isEqualTo(u2.getName());
        assertThat(u1.getUsername()).isEqualTo(u2.getUsername());
        assertThat(u1.getPassword()).isEqualTo(u2.getPassword());
        assertThat(u1.getType()).isEqualTo(u2.getType());
        assertThat(u1.getGroup().getId()).isEqualTo(u2.getGroup().getId());
        assertThat(u1.getCreated()).isEqualTo(u2.getCreated());
        assertThat(u1.getModified()).isEqualTo(u2.getModified());
    }
}
