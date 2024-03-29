package com.rebwon.toby.springbook.web;

import com.rebwon.toby.springbook.domain.Group;
import com.rebwon.toby.springbook.domain.Type;
import com.rebwon.toby.springbook.domain.User;
import com.rebwon.toby.springbook.service.GroupService;
import com.rebwon.toby.springbook.service.UserService;
import com.rebwon.toby.springbook.web.validator.UsernameValidator;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

@Controller
@RequestMapping("/register")
@SessionAttributes("user")
public class RegisterController {

    private GroupService groupService;
    private UserService userService;
    private UsernameValidator usernameValidator;

    @Autowired
    public void init(GroupService groupService, UserService userService,
        UsernameValidator usernameValidator) {
        this.groupService = groupService;
        this.userService = userService;
        this.usernameValidator = usernameValidator;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setDisallowedFields("id", "logins");
    }

    @ModelAttribute
    public List<Group> groups() {
        return this.groupService.getAll();
    }

    @RequestMapping(method = RequestMethod.GET)
    public String showform(ModelMap model) {
        User user = new User();
        user.setType(Type.MEMBER); // default type
        model.addAttribute(user);
        return "register";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String register(@ModelAttribute @Valid User user, BindingResult result,
        SessionStatus status) {
        this.usernameValidator.validate(user, result);
        if (result.hasErrors()) {
            return "register";
        } else {
            this.userService.add(user);
            status.setComplete();
            return "redirect:welcome";
        }
    }
}
