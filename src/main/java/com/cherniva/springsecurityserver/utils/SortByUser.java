package com.cherniva.springsecurityserver.utils;

import com.cherniva.springsecurityserver.model.Pin;

import java.util.Comparator;

public class SortByUser implements Comparator<Pin> {
    @Override
    public int compare(Pin a, Pin b) {
        return a.getValue().compareTo(b.getValue());
    }
}
