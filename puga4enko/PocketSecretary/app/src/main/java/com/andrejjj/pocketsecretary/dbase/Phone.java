package com.andrejjj.pocketsecretary.dbase;

/**
 * Created by Andrejjj on 23.04.2016.
 */
/**
 * @author Andrey S. Pugachenko
 * @version 0.0.1
 * This is a POJO class for phones
 */
public class Phone {
    private int id;
    private String number;

    public Phone(){
        this(null);
    }

    public Phone(String number) {
        this.number = number;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
