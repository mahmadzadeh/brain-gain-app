package com.chart.filesystem.io;

public interface IO {

    String read( ) throws FileIOException;

    void write( String data ) throws FileIOException;
}
