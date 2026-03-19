package com.spatialplanning.ui.mainscreen;

import com.spatialplanning.game.SlotId;

public interface MainViewContract {

    interface Presenter {

        void onSlotTapped(SlotId slotId);

        void startTimer();

        void pauseTimer();

        void onFinish();
    }
}
