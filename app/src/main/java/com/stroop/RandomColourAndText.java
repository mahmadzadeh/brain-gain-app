package com.stroop;


import static com.stroop.ui.element.StatefulGameObject.TextState.BlueText;
import static com.stroop.ui.element.StatefulGameObject.TextState.RedText;

import com.stroop.ui.element.MainText;
import com.stroop.ui.element.StatefulGameObject.ColourState;
import com.stroop.ui.element.StatefulGameObject.TextState;
import com.util.IntegerRange;
import com.util.RandomNumberGenerator;

public class RandomColourAndText {

    public static MainText nextRandomMainText( ) {

        TextState textState = RandomNumberGenerator.next( new IntegerRange( 1, 100 ) ) >= 50 ? BlueText : RedText;

        ColourState colour = RandomNumberGenerator.next( new IntegerRange( 1, 100 ) ) >= 50 ? ColourState.BlueColour : ColourState.RedColour;

        return new MainText( textState, colour );
    }

}
