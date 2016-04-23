package com.andrejjj.pocketsecretary.dbase;

/**
 * Created by Andrejjj on 23.04.2016.
 */
/**
 * @author Andrey S. Pugachenko
 * @version 0.0.1
 * This is a POJO class for tasks
 * In this version there are 5 tasks:
 * <ol>
 *     <li>Meeting</li>
 *     <li>Call</li>
 *     <li>Work/Service</li>
 *     <li>House</li>
 *     <li>Other</li>
 * </ol>
 */
public class Task {
    private int id;
    private String Name;

    public Task() {
        this(null);
    }

    public Task(String Name) {
        this.Name = Name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }
}
