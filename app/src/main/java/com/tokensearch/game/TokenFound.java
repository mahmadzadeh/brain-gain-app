package com.tokensearch.game;

import com.tokensearch.ui.mainscreen.TokenSearchPresenter;

public class TokenFound implements SearchResult {
    @Override
    public void handle( TokenSearchPresenter presenter, int boxIndex ) {
        presenter.onTokenFound( boxIndex );
    }
}
