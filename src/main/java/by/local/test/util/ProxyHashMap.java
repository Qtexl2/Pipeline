package by.local.test.util;

import by.local.test.model.TaskEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

public class ProxyHashMap extends HashMap<String, String> implements Map<String, String>{
    private String firstTask;
    private HashMap<String,Entity> map = new HashMap<>();
    @Override
    public String put(String key, String value) {
        if(firstTask == null){
            firstTask = key;
        }
        Entity entity = map.get(key);
        Set<TaskEntity> newValue;
        TaskEntity task = new TaskEntity();
        if(entity == null){
            newValue = new HashSet<>();
            task.setName(value);
            newValue.add(task);
            entity = new Entity(newValue,1);
            map.put(key,entity);
        } else {
            newValue = entity.getSet();
            if(newValue != null){
                task.setName(value);
                newValue.add(task);
                entity.setSet(newValue);
                map.put(key, entity);
            } else {
                newValue = new HashSet<>();
                task.setName(value);
                newValue.add(task);
                entity.setSet(newValue);
                map.put(key, entity);
            }
        }
        entity = map.get(value);
        if(entity == null){
            entity = new Entity();
            entity.setCount(1);
            map.put(value,entity);
        } else {
            entity.setCount(entity.getCount() + 1);
            map.put(value,entity);
        }
         return String.valueOf(newValue);
    }

    @Override
    public String get(Object key) {
        return String.valueOf(map.get(key));
    }

    public Entity getEntity(Object key) {
        return map.get(key);
    }

    public String getFirstTask(){
        return firstTask;
    }
    @Override
    public String toString() {
        return map.toString();
    }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class Entity{
        private Set<TaskEntity> set;
        private int count;
    }
}
