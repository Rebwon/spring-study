package com.rebwon.toby.springbook.service;

import com.rebwon.toby.springbook.domain.Group;
import java.util.List;

public interface GroupService extends GenericService<Group> {

    List<Group> getAll();
}
