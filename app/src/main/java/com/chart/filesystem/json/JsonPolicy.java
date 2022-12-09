package com.chart.filesystem.json;

import com.chart.filesystem.dao.DataPoint;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public interface JsonPolicy {

    <T extends DataPoint> List<T> parseDataPoints( JSONObject oneRow ) throws JSONException;

    boolean isDataPointsForGame( SupportedGame game, JSONObject oneRow ) throws JSONException;
}
