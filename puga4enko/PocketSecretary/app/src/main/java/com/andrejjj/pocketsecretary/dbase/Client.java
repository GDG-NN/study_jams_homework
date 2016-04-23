package com.andrejjj.pocketsecretary.dbase;

/**
 * Created by Andrejjj on 23.04.2016.
 */

import java.util.HashSet;

/**
 * @author Andrey S. Pugachenko
 * @version 0.0.1
 * This is a POJO class for clients
 */
public class Client {
    private int id;
    private String firstName;
    private String secondName;
    private String patronic;
    private HashSet<Phone> phones;
    private String email;
    private String skype;
    private String additionalTextNote;
    private HashSet<AudioNote> additionalAudioNote;

    public Client(String firstName){
        this(firstName, null, null, new HashSet<Phone>(), null, null, null, new HashSet<AudioNote>());
    }

    public Client(String firstName, String secondName, String patronic, HashSet<Phone> phones, String email, String skype, String additionalTextNote, HashSet<AudioNote> additionalAudioNote) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.patronic = patronic;
        this.phones = phones;
        this.email = email;
        this.skype = skype;
        this.additionalTextNote = additionalTextNote;
        this.additionalAudioNote = additionalAudioNote;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getPatronic() {
        return patronic;
    }

    public void setPatronic(String patronic) {
        this.patronic = patronic;
    }

    public HashSet<Phone> getPhones() {
        return phones;
    }

    public void setPhones(HashSet<Phone> phones) {
        this.phones = phones;
    }

    public void addPhone(Phone phone){
        if(phones == null){
            phones = new HashSet<>();
        }
        phones.add(phone);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSkype() {
        return skype;
    }

    public void setSkype(String skype) {
        this.skype = skype;
    }

    public String getAdditionalTextNote() {
        return additionalTextNote;
    }

    public void setAdditionalTextNote(String additionalTextNote) {
        this.additionalTextNote = additionalTextNote;
    }

    public HashSet<AudioNote> getAdditionalAudioNote() {
        return additionalAudioNote;
    }

    public void setAdditionalAudioNote(HashSet<AudioNote> additionalAudioNote) {
        this.additionalAudioNote = additionalAudioNote;
    }

    public void addAdditionalAudioNote(AudioNote audioNote){
        if(additionalAudioNote == null){
            additionalAudioNote = new HashSet<>();
        }
        additionalAudioNote.add(audioNote);
    }
}
