package com.cherniva.springsecurityserver.utils;

import com.cherniva.springsecurityserver.model.Pin;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class SortByDate implements Comparator<Pin> {
    @Override
    public int compare(Pin a, Pin b) {
        if(a.getTimestamps().isEmpty()) {
            if(b.getTimestamps().isEmpty())
                return 0;
            else
                return 1;
        }
        else if(b.getTimestamps().isEmpty()) {
            return -1;
        }
        SimpleDateFormat sdformat = new SimpleDateFormat("dd.MM.yy hh:mm:ss");
        Date dateA = null;
        Date dateB = null;
        String aString = a.getTimestamps().get(a.getTimestamps().size()-1).getTimestamp();
        String bString = b.getTimestamps().get(b.getTimestamps().size()-1).getTimestamp();
        try {
            dateA = sdformat.parse(aString.substring(0, aString.indexOf('!')));
            dateB = sdformat.parse(bString.substring(0, bString.indexOf('!')));
        }
        catch(ParseException e) {
            System.out.println(e.getMessage());
            System.out.println("Error: conversion to date format");
        }
        return dateB.compareTo(dateA);
    }
}
