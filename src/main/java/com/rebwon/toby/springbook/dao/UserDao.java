package com.rebwon.toby.springbook.dao;

import com.rebwon.toby.springbook.domain.User;

public interface UserDao extends GenericDao<User> {

    User findUser(String username);
}