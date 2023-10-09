package com.cherniva.springsecurityserver.utils;

import com.cherniva.springsecurityserver.model.Timestamp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class TimestampSort implements Comparator<Timestamp> {
    @Override
    public int compare(Timestamp a, Timestamp b) {
        SimpleDateFormat sdformat = new SimpleDateFormat("dd.MM.yy hh:mm:ss");
        Date dateA = null;
        Date dateB = null;
        String aString = a.getTimestamp();
        String bString = b.getTimestamp();
        try {
            dateA = sdformat.parse(aString.substring(0, aString.indexOf('!')));
            dateB = sdformat.parse(bString.substring(0, bString.indexOf('!')));
        }
        catch(ParseException e) {
            System.out.println(e.getMessage());
            System.out.println("Error: conversion to date format");
        }
        return dateA.compareTo(dateB);
    }
}
