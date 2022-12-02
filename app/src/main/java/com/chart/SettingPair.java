package com.chart;

class SettingPair<T> {

    private final String key;
    private final T value;

    SettingPair( String key, T value ) {
        this.key = key;
        this.value = value;
    }

    public String getKey( ) {
        return key;
    }

    public T getValue( ) {
        return value;
    }

}
