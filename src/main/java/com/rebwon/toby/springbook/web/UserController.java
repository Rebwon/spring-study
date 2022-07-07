package com.rebwon.toby.springbook.web;

import com.rebwon.toby.springbook.domain.User;
import com.rebwon.toby.springbook.service.UserService;
import com.rebwon.toby.springbook.web.security.LoginInfo;
import javax.inject.Inject;
import javax.inject.Provider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Inject
    private Provider<LoginInfo> loginInfoProvider;

    @ModelAttribute("currentUser")
    public User currentUser() {
        return loginInfoProvider.get().currentUser();
    }

    @RequestMapping("/list")
    public String list(ModelMap model) {
        model.addAttribute(this.userService.getAll());
        return "user/list";
    }

    @RequestMapping("/view/{id}")
    public String list(@PathVariable int id, ModelMap model) {
        model.addAttribute(this.userService.get(id));
        model.addAttribute("id", id);
        return "user/view";
    }

    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable int id) {
        this.userService.delete(id);
        return "user/deleted";
    }
}
