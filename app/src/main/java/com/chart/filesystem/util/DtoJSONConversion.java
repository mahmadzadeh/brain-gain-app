package com.chart.filesystem.util;


import static com.chart.filesystem.util.JSONUtil.DATAPOINT_ELEMENT;
import static com.chart.filesystem.util.JSONUtil.DATA_ELEMENT;
import static com.chart.filesystem.util.JSONUtil.DATE_ELEMENT;
import static com.chart.filesystem.util.JSONUtil.SCORE_ELEMENT;

import com.chart.filesystem.dao.DataPoint;
import com.chart.filesystem.dao.DataPointCollection;
import com.util.DateUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class DtoJSONConversion {

    public static JSONObject dataPointToJSON( DataPoint dataPoint ) throws JSONException {

        JSONObject jsonObject = new JSONObject();
        JSONObject innerObject = new JSONObject();

        innerObject.put( DATE_ELEMENT, DateUtil.format( dataPoint.date() ) );
        innerObject.put( SCORE_ELEMENT, dataPoint.score() );

        jsonObject.put( DATAPOINT_ELEMENT, innerObject );

        return jsonObject;
    }

    public static JSONObject dataDtoToJSON( DataPointCollection dto ) throws JSONException {

        JSONObject rootObject = new JSONObject();
        JSONArray array = new JSONArray();

        for ( DataPoint dataPoint : dto.userDataPoints() ) {
            array.put( dataPointToJSON( dataPoint ) );
        }

        rootObject.put( DATA_ELEMENT, array );

        return rootObject;
    }

}
