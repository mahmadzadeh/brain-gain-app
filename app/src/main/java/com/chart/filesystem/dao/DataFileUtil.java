package com.chart.filesystem.dao;


import androidx.annotation.NonNull;

import com.chart.filesystem.io.FileIO;
import com.chart.filesystem.util.FileUtil;

import java.io.File;

public class DataFileUtil {

    public static DataPointCollection readAllData( File filesDir ) {
        Dao dao = getDao( filesDir );

        return dao.read();
    }

    public static DataPointCollection readAllDataSortedByDate( File filesDir ) {
        return readAllData( filesDir ).sortedDataPoints();
    }

    @NonNull
    private static Dao getDao( File filesDir ) {
        File file = FileUtil.getDataFile( filesDir );

        return new FileBasedDao( new FileIO( file ) );
    }

}
