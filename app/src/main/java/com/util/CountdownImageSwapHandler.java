package com.util;

import android.os.Handler;
import android.os.Message;

import com.monkeyladder.R;
import com.stroop.ui.countdown.CountDownActivity;

import java.util.Arrays;
import java.util.List;


public class CountdownImageSwapHandler extends Handler {
    private int currentImageIndex;
    private CountDownActivity activity;

    private List<Integer> imageResourceIds =
            Arrays.asList(
                    R.drawable.android3,
                    R.drawable.android2,
                    R.drawable.android1,
                    R.drawable.android_go );

    private int countDownImageCount = imageResourceIds.size();

    public CountdownImageSwapHandler( CountDownActivity activity ) {
        super();
        this.activity = activity;
        this.currentImageIndex = 0;
    }

    public void handleMessage( Message m ) {

        if ( hasMoreImagesToSwap() ) {
            activity.swapImage( imageResourceIds.get( currentImageIndex ) );
        }
        currentImageIndex += 1;
    }

    public boolean hasMoreImagesToSwap( ) {
        return currentImageIndex < countDownImageCount;
    }

    public int getCountOfImagesToBeSwapped( ) {
        return countDownImageCount;
    }
}
