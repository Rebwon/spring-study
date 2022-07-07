package com.rebwon.toby.springbook.web.validator;

import com.rebwon.toby.springbook.domain.Login;
import com.rebwon.toby.springbook.domain.User;
import com.rebwon.toby.springbook.service.UserService;
import com.rebwon.toby.springbook.web.security.LoginInfo;
import javax.inject.Inject;
import javax.inject.Provider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class LoginValidator implements Validator {

    private UserService userService;

    @Inject
    private Provider<LoginInfo> loginInfoProvider;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public boolean supports(Class<?> clazz) {
        return Login.class.isAssignableFrom(clazz);
    }

    public void validate(Object target, Errors errors) {
        Login login = (Login) target;
        User user = userService.findUser(login.getUsername());
        if (user == null || !user.getPassword().equals(login.getPassword())) {
            errors.reject("invalidLogin", "Invalid Login");
        } else {
            LoginInfo loginInfo = loginInfoProvider.get();
            loginInfo.save(user);
        }
    }
}
