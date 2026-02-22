package com.tokensearch.game;

import com.tokensearch.ui.mainscreen.TokenSearchPresenter;

public class AlreadyFound implements SearchResult {
    @Override
    public void handle( TokenSearchPresenter presenter, int boxIndex ) {
        presenter.onErrorTapped( boxIndex );
    }
}
