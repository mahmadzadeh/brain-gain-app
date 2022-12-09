package com.dualnback.util;

import com.chart.filesystem.dao.DataPoint;
import com.chart.filesystem.io.FileIO;
import com.chart.filesystem.io.FileIOException;
import com.chart.filesystem.util.JSONUtil;

import org.json.JSONException;
import org.junit.Test;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;

import static com.chart.TestFileUtil.readTestFile;
import static junit.framework.Assert.assertTrue;
import static org.assertj.core.api.Assertions.assertThat;


public class JSONUtilTest {

    @Test
    public void givenJSONStringNoDataThenParsReturnsEmptyListOfDataPoints( ) throws FileIOException, JSONException {

        String JSON = readTestFile( "emptySampleFile.json" );

        List<DataPoint> dataPointList = JSONUtil.parse( JSON );

        assertTrue( dataPointList.isEmpty() );
    }

    @Test
    public void givenJSONStringWithOneDataPointThenParsReturnsListOfOneDataPoints( ) throws FileIOException, JSONException {

        String JSON = readTestFile( "oneDataPointSampleFile.json" );

        List<DataPoint> dataPointList = JSONUtil.parse( JSON );

        DataPoint dataPoint = dataPointList.get( 0 );
        String expectedDateFormatted = new SimpleDateFormat( DateUtil.FORMAT_PATTERN ).format( dataPoint.date() );

        assertThat( expectedDateFormatted ).isEqualTo( "2012-04-23T18:25:43" );
        assertThat( dataPoint.score() ).isEqualTo( 2 );
    }

    @Test
    public void givenJSONStringWithMultipleDataPointThenParsReturnsListOfOneDataPoints( ) throws FileIOException, JSONException {

        String JSON = readTestFile( "sampleFile.json" );

        List<DataPoint> dataPointList = JSONUtil.parse( JSON );

        assertThat( dataPointList.size() ).isEqualTo( 4 );
    }

}