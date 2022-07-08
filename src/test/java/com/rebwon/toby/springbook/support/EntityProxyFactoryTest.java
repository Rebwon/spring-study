package com.rebwon.toby.springbook.support;

import static org.assertj.core.api.Assertions.assertThat;

import com.rebwon.toby.springbook.dao.GroupDao;
import com.rebwon.toby.springbook.domain.Group;
import java.util.List;
import org.junit.jupiter.api.Test;

public final class EntityProxyFactoryTest {

    @Test
    public void createCglibProxy() {
        EntityProxyFactory ep = new CglibEntityProxyFactory();
        GroupDaoImpl mockGroupDao = new GroupDaoImpl();
        Group group = ep.createProxy(Group.class, mockGroupDao, 1);
        assertThat(group.getId()).isEqualTo(1);
        assertThat(mockGroupDao.called).isFalse();
        assertThat(group.getName()).isEqualTo("name1");
        assertThat(mockGroupDao.called).isTrue();
    }

    static class GroupDaoImpl implements GroupDao {

        boolean called;

        public Group add(Group user) {
            throw new UnsupportedOperationException();
        }

        public long count() {
            throw new UnsupportedOperationException();
        }

        public void delete(int id) {
            throw new UnsupportedOperationException();
        }

        public int deleteAll() {
            throw new UnsupportedOperationException();
        }

        public Group get(int id) {
            called = true;
            return new Group(id, "name" + id);
        }

        public List<Group> getAll() {
            throw new UnsupportedOperationException();
        }

        public List<Group> search(String name) {
            throw new UnsupportedOperationException();
        }

        public Group update(Group user) {
            throw new UnsupportedOperationException();
        }
    }
}
