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

    private static final long ENTRANCE_ANIM_DURATION_MS = 250;
    private static final long ENTRANCE_STAGGER_DELAY_MS = 40;

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
            boxView.setScaleX( 0f );
            boxView.setScaleY( 0f );

            LayoutParams params = new LayoutParams( boxSize, boxSize );
            params.leftMargin = box.col() * cellWidth + padX;
            params.topMargin = box.row() * cellHeight + padY;

            boxView.setLayoutParams( params );
            addView( boxView );
            boxViews.add( boxView );

            boxView.animate()
                .scaleX( 1f )
                .scaleY( 1f )
                .setDuration( ENTRANCE_ANIM_DURATION_MS )
                .setStartDelay( i * ENTRANCE_STAGGER_DELAY_MS )
                .start();
        }
    }

    public void revealToken( int boxIndex ) {
        if ( boxIndex >= 0 && boxIndex < boxViews.size() ) {
            View view = boxViews.get( boxIndex );
            view.setBackgroundResource( R.drawable.tokensearch_box_token );
            animatePop( view );
        }
    }

    public void showError( int boxIndex ) {
        if ( boxIndex >= 0 && boxIndex < boxViews.size() ) {
            View view = boxViews.get( boxIndex );
            view.setBackgroundResource( R.drawable.tokensearch_box_error );
            animateShake( view );
        }
    }

    private void animatePop( View view ) {
        view.animate()
            .scaleX( 1.35f )
            .scaleY( 1.35f )
            .setDuration( 100 )
            .withEndAction( () -> view.animate().scaleX( 1f ).scaleY( 1f ).setDuration( 100 ) );
    }

    private void animateShake( View view ) {
        view.animate()
            .translationX( dpToPx( 8 ) )
            .setDuration( 50 )
            .withEndAction( () -> view.animate()
                .translationX( dpToPx( -8 ) )
                .setDuration( 50 )
                .withEndAction( () -> view.animate()
                    .translationX( 0 )
                    .setDuration( 50 ) ) );
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
