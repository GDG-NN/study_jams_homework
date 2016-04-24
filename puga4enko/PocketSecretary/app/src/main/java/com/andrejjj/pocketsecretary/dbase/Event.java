package com.andrejjj.pocketsecretary.dbase;

/**
 * Created by Andrejjj on 23.04.2016.
 */

import java.util.Date;
import java.util.HashSet;

/**
 * @author Andrey S. Pugachenko
 * @version 0.0.1
 * This is a POJO class for events
 */
public class Event {
    private int id;
    private Date date;
    private Task task;
    private Client client;
    private String textDescription;
    private HashSet<AudioNote> audioDescription;
    /**
     * Event's status:
     * 0 - CURRENT
     * 1 - DONE
     * -1 - CANCELLED
     */
    private int status;

    public Event(Date date, Task task) {
        this(date, task, null, null, null, 0);
    }

    public Event(Date date, Task task, Client client, String textDescription, HashSet<AudioNote> audioDescription, int status) {
        this.date = date;
        this.task = task;
        this.client = client;
        this.textDescription = textDescription;
        this.audioDescription = audioDescription;
        this.status = status;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getTextDescription() {
        return textDescription;
    }

    public void setTextDescription(String textDescription) {
        this.textDescription = textDescription;
    }

    public HashSet<AudioNote> getAudioDescription() {
        return audioDescription;
    }

    public void setAudioDescription(HashSet<AudioNote> audioDescription) {
        this.audioDescription = audioDescription;
    }

    public void addAudioDescription(AudioNote audioDescription){
        if(audioDescription == null){
            this.audioDescription = new HashSet<>();
        }
        this.audioDescription.add(audioDescription);
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
