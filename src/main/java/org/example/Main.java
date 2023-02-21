package org.example;

import org.example.models.Entity;
import org.example.models.Group;
import org.example.models.ListEntity;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
//    private static String FILE_INPUT = System.getProperty("user.dir") + "/src/main/resources/lng.txt";
//    private static String FILE_OUTPUT = "/scr/main/resources/lng_new.txt";
    private static Set<ListEntity> uniqueValues = new HashSet<>();
    private static Map<Entity, ArrayList<ListEntity>> containers = new HashMap<>();
    private static Map<Integer, ArrayList<Group>> finalGroup = new TreeMap<>(Collections.reverseOrder());

    private static int countGroups = 0;

    public static void main(String[] args) {

        long start = System.currentTimeMillis();

        Pattern pattern = Pattern.compile("^(\"([^\"]*)?\";)*\"([^\"]*)?\"$");

//        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_INPUT))) {
        try (BufferedReader reader = new BufferedReader(new FileReader(args[0]))) {
            String line = reader.readLine();
            while (line != null) {
                Matcher matcher = pattern.matcher(line);
                if (matcher.matches()) {
                    workerData(line
                            .replace("\"", "")
                            .split(";"));
                }
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        deleteGroupOneValue();
        finalMerge();
        printAndWrite();

        System.out.println(((System.currentTimeMillis() - start) / 1000f) + " seconds");
        System.out.println((Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory()) / 1000000 + " MB памяти использовано");
    }

    private static void workerData(String[] values) {
        ListEntity listEntity = new ListEntity(values);
        if (!uniqueValues.contains(listEntity)
                && listEntity.isNotEmpty()
        ) {
            uniqueValues.add(listEntity);

            for (Entity entity : listEntity.getNotEmptyEntities()) {
                containers.computeIfAbsent(entity, k -> new ArrayList<ListEntity>());
                containers.get(entity).add(listEntity);
            }

        }
    }
    private static void deleteGroupOneValue() {
        containers
                .values()
                .removeIf(arrayList -> arrayList.size() < 2);
    }


    private static void finalMerge() {
        Map<ListEntity, Integer> multiEntityCount = new HashMap<>();

        for (Map.Entry<Entity, ArrayList<ListEntity>> entry : containers.entrySet()) {
            ArrayList<ListEntity> entities = entry.getValue();
            for (ListEntity listEntity : entities) {
                int value = multiEntityCount.getOrDefault(listEntity, 0);
                multiEntityCount.put(listEntity, value + 1);
            }
        }

        List<Set<ListEntity>> multiEntitySubGroup = new ArrayList<>();
        for (Map.Entry<ListEntity, Integer> entry : multiEntityCount.entrySet()) {
            if (entry.getValue() > 1) {
                Set<ListEntity> set = new HashSet<>();
                for (Entity entity : entry.getKey().getNotEmptyEntities()) {
                    if (containers.containsKey(entity)) {
                        set.addAll(containers.remove(entity));
                    }
                }
                if (set.size() > 1)
                    multiEntitySubGroup.add(set);
            }
        }

        for (Set<ListEntity> subGroup : multiEntitySubGroup) {
            ArrayList<Group> v = finalGroup.getOrDefault(subGroup.size(), new ArrayList<>());
            Group group = new Group(new ArrayList<>(subGroup));
            v.add(group);
            finalGroup.put(group.size(), v);
            countGroups++;
        }

        for (Map.Entry<Entity, ArrayList<ListEntity>> entry : containers.entrySet()) {
            ArrayList<Group> v = finalGroup.getOrDefault(entry.getValue().size(), new ArrayList<>());
            Group group = new Group(entry.getValue());
            v.add(group);
            finalGroup.put(group.size(), v);
            countGroups++;
        }
    }

    public static void printAndWrite() {
        System.out.println("Кол-во групп " + countGroups);
        int count = 1;

        try (FileWriter writer = new FileWriter("lng_new.txt", false)) {
            for (Map.Entry<Integer, ArrayList<Group>> entry : finalGroup.entrySet()) {
                for (Group group : entry.getValue()) {
                    writer.write("Группа " + count + "\n");
//                    System.out.println(str);
                    writer.write(String.valueOf(group));
//                    System.out.println(group);


                    count++;
                    writer.flush();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}