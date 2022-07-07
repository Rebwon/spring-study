package com.rebwon.toby.springbook.service;

import com.rebwon.toby.springbook.domain.User;
import java.util.List;

public interface UserService extends GenericService<User> {

    User findUser(String username);

    List<User> getAll();

    void login(User user);
}
