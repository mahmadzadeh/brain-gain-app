package com.dualnback.ui.mainscreen;

import com.dualnback.game.NBackVersion;

public interface MainViewContract {

    interface Presenter {

        void handleLocationButtonClick( );

        void handleSoundButtonClick( );

        void setCountDownText( String text );

        void endTrial( );

        void startTrial( );

        void onFinish( );

        void startTimer( );

        void pauseTimer( );

        NBackVersion getCurrentVersion( int minRequiredSc0reToGoToNextLvl, int minRequiredSc0reToMaintainCurrentLvl );
    }
}
