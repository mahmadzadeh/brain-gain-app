package com.tokensearch.ui.mainscreen;

import android.os.Handler;
import android.os.Looper;

import com.tokensearch.game.BoxLayout;
import com.tokensearch.game.TokenSearchGame;

import java.util.Random;

/**
 * Simple dependency injector for Token Search.
 */
public class TokenSearchInjector {

    public static TokenSearchPresenter inject( TokenSearchViewContract view ) {
        Random random = new Random();
        BoxLayout layout = new BoxLayout( random );
        TokenSearchGame game = new TokenSearchGame( layout, random );

        return new TokenSearchPresenter(
            view,
            game,
            new Handler( Looper.getMainLooper() )
        );
    }
}
