package com.chart.filesystem.json;

import com.chart.filesystem.dao.DataPoint;
import com.chart.filesystem.io.FileIOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.chart.TestFileUtil.readTestFile;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JSONTest {

    private SupportedGame game = SupportedGame.DualNBack;

    @Mock
    private JsonPolicy mockPolicy;

    private JSON sut;

    @Before
    public void setUp( ) throws Exception {
        sut = new JSON( mockPolicy, game );
    }

    @Test
    public void parse_whenNoDataExistForGivenGame_thenReturnEmptyList( ) throws JSONException, FileIOException {

        when( mockPolicy.isDataPointsForGame( any( SupportedGame.class ), any( JSONObject.class ) ) )
                .thenReturn( false );

        String testFileName = "sampleFileWithMultiGameOneDataEntry.json";

        String jsonString = readTestFile( testFileName );

        assertEquals( 0, sut.parse( jsonString ).size() );


    }

    @Test
    public void parse_whenDataExistForGivenGame_thenReturnList( ) throws JSONException, FileIOException {

        when( mockPolicy.isDataPointsForGame( any( SupportedGame.class ), any( JSONObject.class ) ) )
                .thenReturn( true );

        DataPoint expected = new DataPoint( new Date( System.currentTimeMillis() ),22 );

        when(mockPolicy.parseDataPoints( any(JSONObject.class) ))
                .thenReturn( Arrays.asList( expected ) );

        String testFileName = "sampleFileWithMultiGameOneDataEntry.json";

        String jsonString = readTestFile( testFileName );

        List<DataPoint> actual = sut.parse( jsonString );

        assertEquals( 1, actual.size() );
        assertEquals( expected.score(), actual.get(0).score() );
    }

    @Test(expected = IllegalArgumentException.class)
    public void parse_whenJSONIsEmptyString_throwRuntime( ) throws JSONException {
        sut.parse( "" );
    }

}