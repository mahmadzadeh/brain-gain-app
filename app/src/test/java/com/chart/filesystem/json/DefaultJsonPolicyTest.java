package com.chart.filesystem.json;

import com.chart.filesystem.dao.DataPoint;
import com.chart.filesystem.io.FileIOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static com.chart.TestFileUtil.readTestFile;
import static com.chart.filesystem.json.DefaultJsonPolicy.DATA_ELEMENT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class DefaultJsonPolicyTest {

    DefaultJsonPolicy sut;

    @Before
    public void setUp( ) throws Exception {
        sut = new DefaultJsonPolicy();
    }

    @Test
    public void parseDataPoints_whenNoData_thenReturnEmptyListOfDataPoints( ) throws JSONException {

        String testFileName = "sampleFileWithMultiGameNoDataEntry.json";

        JSONObject oneRow = readJsonFileAndReturnFirstEntry( testFileName );

        List<DataPoint> actual = sut.parseDataPoints( oneRow );

        assertEquals( Collections.EMPTY_LIST, actual );

    }

    @Test
    public void parseDataPoints_whenSingleData_thenReturnListOfOneDataPoints( ) throws JSONException {

        String testFileName = "sampleFileWithMultiGameOneDataEntry.json";

        JSONObject oneRow = readJsonFileAndReturnFirstEntry( testFileName );

        List<DataPoint> actual = sut.parseDataPoints( oneRow );

        assertEquals( 1, actual.size() );

        assertEquals( 22, actual.get( 0 ).score() );
    }

    @Test
    public void isDataPointsForGame_whenGameHasEntry_thenReturnTrue( ) throws JSONException {

        String testFileName = "sampleFileWithMultiGameOneDataEntry.json";

        JSONObject oneRow = readJsonFileAndReturnFirstEntry( testFileName );

        assertTrue(
                sut.isDataPointsForGame( SupportedGame.MonkeyLadder, oneRow ) );

        assertFalse(
                sut.isDataPointsForGame( SupportedGame.DualNBack, oneRow ) );

    }

    private JSONObject readJsonFileAndReturnFirstEntry( String testFileName ) {
        String jsonString = null;

        try {
            jsonString = readTestFile( testFileName );
        } catch ( FileIOException e ) {
            fail( "Unable to read input file " + testFileName );
        }

        JSONObject jsonObject = null;

        try {

            jsonObject = new JSONObject( jsonString );
            JSONArray array = jsonObject.optJSONArray( DATA_ELEMENT );

            jsonObject = ( JSONObject ) array.get( 0 );

        } catch ( JSONException e ) {
            fail( "Unable to process JSON string" );
        }

        return jsonObject;
    }
}