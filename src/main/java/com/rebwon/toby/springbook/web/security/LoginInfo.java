package com.rebwon.toby.springbook.web.security;

import com.rebwon.toby.springbook.domain.User;
import java.util.Date;

public interface LoginInfo {

    void save(User user);

    void remove();

    boolean isLoggedIn();

    User currentUser();

    Date getLoginTime();
}
