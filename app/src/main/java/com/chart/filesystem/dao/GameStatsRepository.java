package com.chart.filesystem.dao;

import com.chart.filesystem.io.FileIO;
import com.chart.filesystem.io.IO;
import com.chart.filesystem.util.FileUtil;
import com.util.DateUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Unified stats persistence. All game scores are stored in a single JSON file
 * keyed by game name.
 *
 * Format:
 * {
 *   "sudoku": { "data": [ {"datapoint": {"date":"...", "score":5}}, ... ] },
 *   "tokensearch": { "data": [ ... ] },
 *   ...
 * }
 */
public class GameStatsRepository {

    public static final String STATS_FILE = "stats.json";
    private static final String DATA_KEY = "data";
    private static final String DATAPOINT_KEY = "datapoint";
    private static final String DATE_KEY = "date";
    private static final String SCORE_KEY = "score";

    private final IO fileIO;

    public GameStatsRepository( IO fileIO ) {
        this.fileIO = fileIO;
    }

    public static GameStatsRepository create( File filesDir ) {
        return new GameStatsRepository( new FileIO( FileUtil.getDataFile( filesDir, STATS_FILE ) ) );
    }

    public DataPointCollection read( GameKey gameKey ) {
        List<DataPoint> dataPoints = new ArrayList<>();

        try {
            String content = fileIO.read();
            JSONObject root = new JSONObject( content );
            JSONObject gameObject = root.optJSONObject( gameKey.key() );

            if ( gameObject != null ) {
                JSONArray array = gameObject.optJSONArray( DATA_KEY );
                if ( array != null ) {
                    for ( int i = 0; i < array.length(); i++ ) {
                        JSONObject row = array.getJSONObject( i );
                        JSONObject dp = row.getJSONObject( DATAPOINT_KEY );
                        Date date = DateUtil.parse( dp.getString( DATE_KEY ) );
                        int score = dp.getInt( SCORE_KEY );
                        dataPoints.add( new DataPoint( date, score ) );
                    }
                }
            }
        } catch ( Exception ignored ) {
        }

        return new DataPointCollection( dataPoints );
    }

    public void write( GameKey gameKey, DataPointCollection data ) {
        try {
            JSONObject root = readRoot();
            JSONObject gameObject = toJSON( data );
            root.put( gameKey.key(), gameObject );
            fileIO.write( root.toString() );
        } catch ( Exception ignored ) {
        }
    }

    public void addScore( GameKey gameKey, Date date, int score ) {
        DataPointCollection existing = read( gameKey );
        existing.addDataPoint( new DataPoint( date, score ) );
        DataPointCollection shrunk = existing.shrinkDataSize();
        write( gameKey, shrunk );
    }

    private JSONObject readRoot() {
        try {
            String content = fileIO.read();
            return new JSONObject( content );
        } catch ( Exception e ) {
            return new JSONObject();
        }
    }

    private JSONObject toJSON( DataPointCollection data ) throws JSONException {
        JSONObject gameObject = new JSONObject();
        JSONArray array = new JSONArray();

        for ( DataPoint dp : data.userDataPoints() ) {
            JSONObject row = new JSONObject();
            JSONObject inner = new JSONObject();
            inner.put( DATE_KEY, DateUtil.format( dp.date() ) );
            inner.put( SCORE_KEY, dp.score() );
            row.put( DATAPOINT_KEY, inner );
            array.put( row );
        }

        gameObject.put( DATA_KEY, array );
        return gameObject;
    }
}
