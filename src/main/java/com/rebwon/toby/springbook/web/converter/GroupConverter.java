package com.rebwon.toby.springbook.web.converter;

import com.rebwon.toby.springbook.domain.Group;
import com.rebwon.toby.springbook.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

public class GroupConverter {

    private GroupConverter() {
    }

    @Component
    public static class GroupToString implements Converter<Group, String> {

        public String convert(Group group) {
            return (group == null) ? "" : String.valueOf(group.getId());
        }
    }

    @Component
    public static class StringToGroup implements Converter<String, Group> {

        private GroupService groupService;

        @Autowired
        public void setGroupService(GroupService groupService) {
            this.groupService = groupService;
        }

        public Group convert(String text) {
            return groupService.get(Integer.valueOf(text));
        }
    }
}
