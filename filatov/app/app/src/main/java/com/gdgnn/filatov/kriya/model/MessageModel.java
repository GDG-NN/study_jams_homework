package com.gdgnn.filatov.kriya.model;

import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Locale;

public class MessageModel {

    @SerializedName("id")
    private int id = 0;

    @SerializedName("date")
    private int date = 0;

    @SerializedName("title")
    private String title = "";

    @SerializedName("place")
    private String place = "";

    @SerializedName("country")
    private String country = "";

    @SerializedName("message")
    private String text = "";

    public int getId() {
        return this.id;
    }

    public String getDate() {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("d MMMM, yyyy").withLocale(Locale.US);
        return formatter.print(new DateTime(this.date * 1000L));
    }

    public String getTitle() {
        return this.title;
    }

    public String getPlace() {
        return this.place;
    }

    public String getCountry() {
        return this.country;
    }

    public String getText() {
        return this.text;
    }
}
