package com.willy.will.common.controller;

import com.willy.will.common.model.Group;

import java.util.Comparator;

public class AscendingGroupByName implements Comparator<Group> {
    @Override
    public int compare(Group o1, Group o2) {
        return (o1.getGroupName()).compareTo(o2.getGroupName());
    }
}
