package org.example.models;

import java.util.Objects;

public class Entity {
    private short columnOrder;
    private String string;
    private boolean isNotEmpty;

    public Entity(short columnOrder, String string) {
        this.columnOrder = columnOrder;
        this.string = string;
        isNotEmpty = !string.isEmpty();
    }

    public boolean isNotEmpty() {
        return isNotEmpty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entity entity = (Entity) o;
        return columnOrder == entity.columnOrder &&
                string.equals(entity.string);
    }

    @Override
    public int hashCode() {
        return Objects.hash(columnOrder, string);
    }

    @Override
    public String toString() {
        return string;
    }
}
