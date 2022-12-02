package com.stroop.ui.element;

import com.util.RandomBoolean;

public class StatefulGameObject {

    private final ColourState colourState;
    private final TextState textState;

    public StatefulGameObject( TextState textState, ColourState colourState ) {
        this.textState = textState;
        this.colourState = colourState;
    }

    public ColourState getRandomColour( ) {
        return RandomBoolean.nextRandomTrue() ? ColourState.BlueColour : ColourState.RedColour;
    }

    public TextState getOppositeTextState( ) {
        return getTextState() == TextState.BlueText ? TextState.RedText : TextState.BlueText;
    }

    public TextState getTextState( ) {
        return textState;
    }

    public ColourState getColourState( ) {
        return colourState;
    }

    public boolean textAndColourMatch( ) {
        return ( colourState == ColourState.RedColour && textState == TextState.RedText ) ||
                ( colourState == ColourState.BlueColour && textState == TextState.BlueText );
    }

    public enum TextState {
        RedText( "RED" ), BlueText( "BLUE" );

        private String s;

        TextState( String s ) {

            this.s = s;
        }

        @Override
        public String toString( ) {
            return s;
        }
    }

    public enum ColourState {
        RedColour( "RED" ), BlueColour( "BLUE" );

        private String colour;

        ColourState( String colour ) {

            this.colour = colour;
        }

        @Override
        public String toString( ) {
            return colour;
        }
    }


}
