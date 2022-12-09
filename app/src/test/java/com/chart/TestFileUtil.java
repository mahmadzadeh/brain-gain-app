package com.chart;

import com.chart.filesystem.io.FileIO;
import com.chart.filesystem.io.FileIOException;

import java.io.File;

public class TestFileUtil {

    private static final String TEST_RESOURCES_DIR = "src/test/resources/";

    public static String readTestFile( String testFileName ) throws FileIOException {
        return new FileIO( new File( TEST_RESOURCES_DIR + testFileName ) ).read();
    }
}
