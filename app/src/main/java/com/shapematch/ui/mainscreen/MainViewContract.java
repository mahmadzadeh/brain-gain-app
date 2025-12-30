package com.shapematch.ui.mainscreen;

public interface MainViewContract {

    interface Presenter {

        void handleMatchButtonClick();

        void handleMismatchButtonClick();

        void startTimer();

        void pauseTimer();

        void onFinish();
    }
}
