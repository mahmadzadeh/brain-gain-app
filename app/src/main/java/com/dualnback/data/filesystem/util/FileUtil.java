package com.dualnback.data.filesystem.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class FileUtil {

    public static final String FILE_NAME = "data.json";
    private static final String EMPTY_DATA = "{ \"data\" :[] }";

    public static File getDataFile( File filesDir, String fileName ) {
        File file = new File( filesDir, fileName );

        createFileIfNotExist( file );

        return file;

    }

    private static void createFileIfNotExist( File file ) {
        if ( !file.exists() ) {
            try {
                file.createNewFile();
                // Only write empty content when file is first created
                writeEmptyContent( file );
            } catch ( IOException e ) {
                throw new RuntimeException( "Unable create file " + file.getAbsolutePath() );
            }
        }
    }

    private static void writeEmptyContent( File file ) {
        try {
            FileWriter writer = new FileWriter( file );
            writer.write( EMPTY_DATA );
            writer.close();
        } catch ( IOException e ) {
            throw new RuntimeException( "Unable write file " + file.getAbsolutePath() );
        }
    }

    public static File getDataFile( File filesDir ) {
        return getDataFile( filesDir, FILE_NAME );

    }
}
