package com.rebwon.toby.springbook.web.validator;

import com.rebwon.toby.springbook.domain.User;
import com.rebwon.toby.springbook.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UsernameValidator implements Validator {

    @Autowired
    private UserService userService;

    public boolean supports(Class<?> clazz) {
        return User.class.isAssignableFrom(clazz);
    }

    public void validate(Object target, Errors errors) {
        User formUser = (User) target;
        User user = this.userService.findUser(formUser.getUsername());
        if (user != null && user.getId() != formUser.getId()) {
            errors.rejectValue("username", "duplicateUsername");
        }
    }

}
