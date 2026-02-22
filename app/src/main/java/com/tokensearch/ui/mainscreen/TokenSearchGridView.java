package com.tokensearch.ui.mainscreen;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.monkeyladder.R;
import com.tokensearch.game.Box;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom view that handles the grid layout for Token Search boxes.
 */
public class TokenSearchGridView extends FrameLayout {

    public interface BoxClickListener {
        void onBoxTapped( int boxIndex );
    }

    private final List<View> boxViews = new ArrayList<>();
    private BoxClickListener listener;

    public TokenSearchGridView( Context context ) {
        super( context );
    }

    public TokenSearchGridView( Context context, AttributeSet attrs ) {
        super( context, attrs );
    }

    public void setBoxClickListener( BoxClickListener listener ) {
        this.listener = listener;
    }

    public void showBoxes( List<Box> boxes, int gridCols, int gridRows ) {
        removeAllViews();
        boxViews.clear();

        if ( getWidth() == 0 || getHeight() == 0 ) {
            // View not laid out yet, retry once it is
            post( () -> showBoxes( boxes, gridCols, gridRows ) );
            return;
        }

        int cellWidth = getWidth() / gridCols;
        int cellHeight = getHeight() / gridRows;
        int boxSize = Math.min( cellWidth, cellHeight ) - dpToPx( 8 );
        int padX = ( cellWidth - boxSize ) / 2;
        int padY = ( cellHeight - boxSize ) / 2;

        for ( int i = 0; i < boxes.size(); i++ ) {
            Box box = boxes.get( i );
            View boxView = createBoxView( i, boxSize );

            LayoutParams params = new LayoutParams( boxSize, boxSize );
            params.leftMargin = box.col() * cellWidth + padX;
            params.topMargin = box.row() * cellHeight + padY;

            boxView.setLayoutParams( params );
            addView( boxView );
            boxViews.add( boxView );
        }
    }

    public void revealToken( int boxIndex ) {
        if ( boxIndex >= 0 && boxIndex < boxViews.size() ) {
            boxViews.get( boxIndex ).setBackgroundResource( R.drawable.tokensearch_box_token );
        }
    }

    public void showError( int boxIndex ) {
        if ( boxIndex >= 0 && boxIndex < boxViews.size() ) {
            boxViews.get( boxIndex ).setBackgroundResource( R.drawable.tokensearch_box_error );
        }
    }

    public void hideToken( int boxIndex ) {
        if ( boxIndex >= 0 && boxIndex < boxViews.size() ) {
            boxViews.get( boxIndex ).setBackgroundResource( R.drawable.tokensearch_box_closed );
        }
    }

    private View createBoxView( final int index, int size ) {
        View view = new View( getContext() );
        view.setBackgroundResource( R.drawable.tokensearch_box_closed );
        view.setElevation( dpToPx( 2 ) );
        view.setOnClickListener( v -> {
            if ( listener != null ) {
                listener.onBoxTapped( index );
            }
        } );
        return view;
    }

    private int dpToPx( int dp ) {
        return (int) ( dp * getResources().getDisplayMetrics().density );
    }
}
