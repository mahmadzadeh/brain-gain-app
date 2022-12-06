package com.chart.dao;

import com.dualnback.data.filesystem.dao.DataFileUtil;
import com.dualnback.data.filesystem.dao.DataPointCollection;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class DataFileUtilTest {

    private static final String TEST_RESOURCES_DIR = "src/test/resources";

    @Test
    public void givenDataFileDirThenReadAllDataReadsIt( ) {

        DataPointCollection dataPointCollection = DataFileUtil.readAllData( new File( TEST_RESOURCES_DIR ) );

        assertThat( dataPointCollection.size() ).isEqualTo( 0 );
    }

    @Ignore
    public void givenDataFileDirThenReadAllSortedDataReadsItInSortedOrder( ) {

        DataPointCollection dataPointCollection = DataFileUtil.readAllDataSortedByDate( new File( TEST_RESOURCES_DIR ) );

        assertThat( dataPointCollection.size() ).isEqualTo( 0 );
        assertThat( dataPointCollection.userDataPoints().get( 0 ).score() ).isEqualTo( 2 );
        assertThat( dataPointCollection.userDataPoints().get( 1 ).score() ).isEqualTo( 80 );
    }
}