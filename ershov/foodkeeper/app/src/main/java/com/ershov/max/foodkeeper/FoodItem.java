package com.ershov.max.foodkeeper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Maxim Ershov on 24.04.2016.
 */
public class FoodItem {
    private Date buyDate;
    private Date expireDate;
    private String name;
	public static String DATE_FORMAT = "dd-MM-yyyy";
	
	public FoodItem(String expDate, String name) {
        this.buyDate = new Date();
		this.name = name;
		this.expireDate = stringToDate(expDate,DATE_FORMAT);   
    }

    public boolean isExpired() {
        Date currDate = new Date();
        return (currDate.after(this.expireDate));
    }

    public Date getBuyDate() {
        return buyDate;
    }

    public void setBuyDate(Date buyDate) {
        this.buyDate = buyDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }
	
	public static Date stringToDate(String dateString, String dateFormat) {
		SimpleDateFormat format = new SimpleDateFormat(dateFormat);
        Date date = null;
        try {
			date = format.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	public static String dateToString(Date date, String dateFormatString) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatString);
        return dateFormat.format(date);
	}

    public static String fromDateToDuration(String date) {
        Date currentDate = new Date();
        Date endDate = stringToDate(date,DATE_FORMAT);
        if (endDate != null) {
            long startTime = currentDate.getTime();
            long endTime = endDate.getTime();
            long diffTime = endTime - startTime;
            long diffDays = diffTime / (1000 * 60 * 60 * 24) + 1;
            if (diffDays > 0) {
                return String.valueOf(diffDays);
            } else return "0";
        } else return "0";
    }

    public static String fromDurationToDate(String duration) {
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        try {
            c.add(Calendar.DATE, Integer.parseInt(duration));
        } catch (NumberFormatException e) {
            return "";
        }
        date = c.getTime();
        return dateToString(date,DATE_FORMAT);
    }

    public static boolean isDateValid(String date)
    {
        try {
            SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);
            df.setLenient(false);
            df.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
}
