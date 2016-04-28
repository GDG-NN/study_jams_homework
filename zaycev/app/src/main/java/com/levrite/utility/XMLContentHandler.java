package com.levrite.utility;

import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

public class XMLContentHandler extends DefaultHandler {


    private boolean dayType = false;

    private StringBuilder mStringBuilder = new StringBuilder();

    private WeatherData mWeatherData = new WeatherData();

    private List<WeatherData> mWeatherDataList = new ArrayList<WeatherData>();

    public List<WeatherData> getParsedData() {
        return this.mWeatherDataList;
    }


    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

        if (qName.equals("day_part") && attributes.getValue("type").equals("day")) {
            this.mWeatherData = new WeatherData();
            this.dayType = true;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {

        if (localName.equals("weather_type") && dayType) {
            this.mWeatherData = new WeatherData();
            this.mWeatherDataList.add(mWeatherData);
            mWeatherData.setWeatherType(mStringBuilder.toString().trim());
            this.dayType = false;

        } else {

        }

        mStringBuilder.setLength(0);

    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        mStringBuilder.append(ch, start, length);
    }
}
