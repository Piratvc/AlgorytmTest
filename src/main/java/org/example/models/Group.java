package org.example.models;

import java.util.List;
import java.util.Objects;

public class Group {

    private List<ListEntity> group;

    public Group(List<ListEntity> group) {
        this.group = group;
    }

    public int size() {
        return group.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group group1 = (Group) o;
        return group.equals(group1.group);
    }

    @Override
    public int hashCode() {
        return Objects.hash(group);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (ListEntity listEntity : group) {
            str.append(listEntity).append("\n");
        }
        return str.toString();
    }
}