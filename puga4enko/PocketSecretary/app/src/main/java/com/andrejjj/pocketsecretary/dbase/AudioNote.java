package com.andrejjj.pocketsecretary.dbase;

/**
 * Created by Andrejjj on 23.04.2016.
 */

/**
 * @author Andrey S. Pugachenko
 * @version 0.0.1
 * This is a POJO class for working with audio
 */

public class AudioNote {
    private int id;
    private String path;

    public AudioNote(String path) {
        this.path = path;
    }

    public AudioNote() {
        this(null);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
