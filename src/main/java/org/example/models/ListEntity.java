package org.example.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class ListEntity {

    private List<Entity> entities = new ArrayList<>();

    public ListEntity(String... strings) {
        for (short columnOrder = 0; columnOrder < strings.length; columnOrder++) {
            entities.add(new Entity(columnOrder, strings[columnOrder]));
        }
    }

    public List<Entity> getNotEmptyEntities() {
        return entities.stream()
                .filter(Entity::isNotEmpty)
                .collect(Collectors.toList());
    }

    public boolean isNotEmpty() {
        for (Entity entity : entities) {
            if(entity.isNotEmpty()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ListEntity that = (ListEntity) o;
        return entities.equals(that.entities);
    }

    @Override
    public int hashCode() {
        return Objects.hash(entities);
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(" ; ");
        for (Entity entity : entities)
            joiner.add(entity.toString());
        return joiner.toString();
    }
}
