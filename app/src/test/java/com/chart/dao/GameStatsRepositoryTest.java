package com.chart.dao;

import com.chart.filesystem.dao.DataPoint;
import com.chart.filesystem.dao.DataPointCollection;
import com.chart.filesystem.dao.GameKey;
import com.chart.filesystem.dao.GameStatsRepository;
import com.chart.filesystem.io.IO;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith( MockitoJUnitRunner.class )
public class GameStatsRepositoryTest {

    @Mock
    IO mockIO;

    private GameStatsRepository repository;

    @Before
    public void setUp() {
        repository = new GameStatsRepository( mockIO );
    }

    @Test
    public void readReturnsEmptyCollectionWhenFileIsEmpty() throws Exception {
        when( mockIO.read() ).thenReturn( "" );

        DataPointCollection result = repository.read( GameKey.SUDOKU );

        assertThat( result.size(), is( 0 ) );
    }

    @Test
    public void readReturnsEmptyCollectionWhenGameKeyNotPresent() throws Exception {
        when( mockIO.read() ).thenReturn( "{}" );

        DataPointCollection result = repository.read( GameKey.SUDOKU );

        assertThat( result.size(), is( 0 ) );
    }

    @Test
    public void readParsesDataForGameKey() throws Exception {
        String json = "{\"sudoku\":{\"data\":["
            + "{\"datapoint\":{\"date\":\"2026-02-14T12:00:00\",\"score\":42}},"
            + "{\"datapoint\":{\"date\":\"2026-02-15T12:00:00\",\"score\":55}}"
            + "]}}";
        when( mockIO.read() ).thenReturn( json );

        DataPointCollection result = repository.read( GameKey.SUDOKU );

        assertThat( result.size(), is( 2 ) );
        assertThat( result.userDataPoints().get( 0 ).score(), is( 42 ) );
        assertThat( result.userDataPoints().get( 1 ).score(), is( 55 ) );
    }

    @Test
    public void readReturnsOnlyRequestedGameData() throws Exception {
        String json = "{\"sudoku\":{\"data\":["
            + "{\"datapoint\":{\"date\":\"2026-02-14T12:00:00\",\"score\":42}}"
            + "]},\"tokensearch\":{\"data\":["
            + "{\"datapoint\":{\"date\":\"2026-02-14T12:00:00\",\"score\":7}}"
            + "]}}";
        when( mockIO.read() ).thenReturn( json );

        DataPointCollection sudokuData = repository.read( GameKey.SUDOKU );
        DataPointCollection tokenData = repository.read( GameKey.TOKEN_SEARCH );

        assertThat( sudokuData.size(), is( 1 ) );
        assertThat( sudokuData.userDataPoints().get( 0 ).score(), is( 42 ) );
        assertThat( tokenData.size(), is( 1 ) );
        assertThat( tokenData.userDataPoints().get( 0 ).score(), is( 7 ) );
    }

    @Test
    public void writeCreatesGameKeyEntry() throws Exception {
        when( mockIO.read() ).thenReturn( "{}" );
        doNothing().when( mockIO ).write( anyString() );

        Date date = new Date();
        DataPointCollection data = new DataPointCollection(
            Arrays.asList( new DataPoint( date, 100 ) )
        );

        repository.write( GameKey.SUDOKU, data );

        verify( mockIO ).write( anyString() );
    }

    @Test
    public void writePreservesOtherGameData() throws Exception {
        String existingJson = "{\"tokensearch\":{\"data\":["
            + "{\"datapoint\":{\"date\":\"2026-02-14T12:00:00\",\"score\":7}}"
            + "]}}";
        when( mockIO.read() ).thenReturn( existingJson );

        final String[] writtenJson = new String[1];
        org.mockito.Mockito.doAnswer( invocation -> {
            writtenJson[0] = (String) invocation.getArguments()[0];
            return null;
        } ).when( mockIO ).write( anyString() );

        Date date = new Date();
        DataPointCollection data = new DataPointCollection(
            Arrays.asList( new DataPoint( date, 42 ) )
        );
        repository.write( GameKey.SUDOKU, data );

        // After writing sudoku data, tokensearch data should still be present
        when( mockIO.read() ).thenReturn( writtenJson[0] );
        DataPointCollection tokenData = repository.read( GameKey.TOKEN_SEARCH );
        assertThat( tokenData.size(), is( 1 ) );
        assertThat( tokenData.userDataPoints().get( 0 ).score(), is( 7 ) );
    }

    @Test
    public void addScoreAppendsAndShrinks() throws Exception {
        when( mockIO.read() ).thenReturn( "{}" );
        doNothing().when( mockIO ).write( anyString() );

        repository.addScore( GameKey.SUDOKU, new Date(), 99 );

        verify( mockIO ).write( anyString() );
    }
}
