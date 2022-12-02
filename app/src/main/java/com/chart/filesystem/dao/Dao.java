package com.chart.filesystem.dao;


public interface Dao {

    DataPointCollection read( );

    void write( DataPointCollection data );

}
