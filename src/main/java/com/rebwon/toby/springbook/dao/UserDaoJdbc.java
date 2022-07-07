package com.rebwon.toby.springbook.dao;

import com.rebwon.toby.springbook.domain.Group;
import com.rebwon.toby.springbook.domain.Type;
import com.rebwon.toby.springbook.domain.User;
import com.rebwon.toby.springbook.support.EntityProxyFactory;
import com.rebwon.toby.springbook.support.MappedBeanPropertySqlParameterSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoJdbc implements UserDao {

    @Autowired
    private EntityProxyFactory entityProxyFactory;

    @Autowired
    private GroupDao groupDao;

    private JdbcTemplate jdbcTemplate;

    private SimpleJdbcInsert userInsert;

    private RowMapper<User> rowMapper = new RowMapper<>() {
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setName(rs.getString("name"));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            user.setType(Type.valueOf(rs.getInt("type")));
            user.setGroup(UserDaoJdbc.this.entityProxyFactory.createProxy(Group.class, groupDao,
                rs.getInt("groupid")));
            user.setCreated(rs.getDate("created"));
            user.setModified(rs.getDate("modified"));
            user.setLogins(rs.getInt("logins"));

            return user;
        }
    };

    @Autowired
    public void init(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.userInsert = new SimpleJdbcInsert(dataSource).withTableName("users")
            .usingGeneratedKeyColumns("id");
    }

    public User add(User user) {
        int generatedId = this.userInsert.executeAndReturnKey(
            new UserBeanPropertySqlParameterSource(user)).intValue();
        user.setId(generatedId);
        return user;
    }

    public User update(User user) {
        int affected = jdbcTemplate.update(
            "update users set " + "name = :name, " + "username = :username, "
                + "password = :password, " + "type = :type, " + "groupid = :groupid, "
                + "created = :created, " + "modified = :modified, " + "logins = :logins "
                + "where id = :id", new UserBeanPropertySqlParameterSource(user));
        return user;
    }

    public void delete(int id) {
        this.jdbcTemplate.update("delete from users where id = ?", id);
    }

    public int deleteAll() {
        return this.jdbcTemplate.update("delete from users");
    }

    public User get(int id) {
        try {
            return this.jdbcTemplate.queryForObject("select * from users where id = ?",
                this.rowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<User> search(String name) {
        return this.jdbcTemplate.query("select * from users where name like ?", this.rowMapper,
            "%" + name + "%");
    }

    public List<User> getAll() {
        return this.jdbcTemplate.query("select * from users order by id desc", this.rowMapper);
    }

    public long count() {
        return this.jdbcTemplate.queryForObject("select count(0) from users", Long.class);
    }

    private static class UserBeanPropertySqlParameterSource extends
        MappedBeanPropertySqlParameterSource {

        public UserBeanPropertySqlParameterSource(Object object) {
            super(object);
            map("type", "type.value");
            map("groupid", "group.id");
        }
    }

    public User findUser(String username) {
        try {
            return this.jdbcTemplate.queryForObject("select * from users where username = ?",
                this.rowMapper, username);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
