package com.monkeyladder.game;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.monkeyladder.game.GameLevel.LevelEight;
import static com.monkeyladder.game.GameLevel.LevelEleven;
import static com.monkeyladder.game.GameLevel.LevelFifteen;
import static com.monkeyladder.game.GameLevel.LevelFive;
import static com.monkeyladder.game.GameLevel.LevelFour;
import static com.monkeyladder.game.GameLevel.LevelFourteen;
import static com.monkeyladder.game.GameLevel.LevelNine;
import static com.monkeyladder.game.GameLevel.LevelOne;
import static com.monkeyladder.game.GameLevel.LevelSeven;
import static com.monkeyladder.game.GameLevel.LevelSix;
import static com.monkeyladder.game.GameLevel.LevelSixteen;
import static com.monkeyladder.game.GameLevel.LevelTen;
import static com.monkeyladder.game.GameLevel.LevelThirteen;
import static com.monkeyladder.game.GameLevel.LevelThree;
import static com.monkeyladder.game.GameLevel.LevelTwelve;
import static com.monkeyladder.game.GameLevel.LevelTwo;
import static com.monkeyladder.game.GameLevel.Null;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@RunWith(MockitoJUnitRunner.class)
public class GameLevelTest {

    @Test
    public void givenInvalidUiRepresentationOfNBackVersionThenFromUiReturnsOptionalEmpty( ) throws Exception {
        assertEquals( Optional.empty(), GameLevel.fromTextValue( "bugus_version" ) );
    }

    @Ignore
    public void givenValidUiRepresentationOfNBackVersionThenFromUiReturnsIt( ) throws Exception {

        asList( "LevelEleven" )
                .stream()
                .forEach( ui -> {
                    assertTrue( GameLevel.fromTextValue( ui ).get() instanceof GameLevel );
                } );
    }

    @Test
    public void givenCurrentVersionThenNextVersionUpReturnsCorrectValue( ) {

        Map<GameLevel, Optional<GameLevel>> expected = new HashMap<>();
        expected.put( Null, Optional.empty() );
        expected.put( LevelOne, Optional.of( LevelTwo ) );
        expected.put( LevelTwo, Optional.of( LevelThree ) );
        expected.put( LevelThree, Optional.of( LevelFour ) );
        expected.put( LevelFour, Optional.of( LevelFive ) );
        expected.put( LevelFive, Optional.of( LevelSix ) );
        expected.put( LevelSix, Optional.of( LevelSeven ) );
        expected.put( LevelSeven, Optional.of( LevelEight ) );
        expected.put( LevelEight, Optional.of( LevelNine ) );
        expected.put( LevelNine, Optional.of( LevelTen ) );
        expected.put( LevelTen, Optional.of( LevelEleven ) );
        expected.put( LevelEleven, Optional.of( LevelTwelve ) );
        expected.put( LevelTwelve, Optional.of( LevelThirteen ) );
        expected.put( LevelThirteen, Optional.of( LevelFourteen ) );
        expected.put( LevelFourteen, Optional.of( LevelFifteen ) );
        expected.put( LevelFifteen, Optional.of( LevelSixteen ) );
        expected.put( LevelSixteen, Optional.empty() );

        for ( GameLevel version : GameLevel.values() ) {
            assertThat( version.nextLevelUp() ).isEqualTo( expected.get( version ) );
        }
    }

    @Test
    public void givenCurrentVersionThenPreviousVersionDownReturnsCorrectValue( ) {

        Map<GameLevel, Optional<GameLevel>> expected = new HashMap<>();
        expected.put( Null, Optional.empty() );
        expected.put( LevelOne, Optional.empty() );
        expected.put( LevelTwo, Optional.of( LevelOne ) );
        expected.put( LevelThree, Optional.of( LevelTwo ) );
        expected.put( LevelFour, Optional.of( LevelThree ) );
        expected.put( LevelFive, Optional.of( LevelFour ) );
        expected.put( LevelSix, Optional.of( LevelFive ) );
        expected.put( LevelSeven, Optional.of( LevelSix ) );
        expected.put( LevelEight, Optional.of( LevelSeven ) );
        expected.put( LevelNine, Optional.of( LevelEight ) );
        expected.put( LevelTen, Optional.of( LevelNine ) );
        expected.put( LevelEleven, Optional.of( LevelTen ) );
        expected.put( LevelTwelve, Optional.of( LevelEleven ) );
        expected.put( LevelThirteen, Optional.of( LevelTwelve ) );
        expected.put( LevelFourteen, Optional.of( LevelThirteen ) );
        expected.put( LevelFifteen, Optional.of( LevelFourteen ) );
        expected.put( LevelSixteen, Optional.of( LevelFifteen ) );

        for ( GameLevel version : GameLevel.values() ) {
            assertThat( version.previousLevelDown() ).isEqualTo( expected.get( version ) );
        }
    }

    @Test
    public void givenCurrentVersionThenMapMethodWillSelectIndexOfArrayAdapter( ) {

        List<CharSequence> arrayAdapter = new ArrayList<>();

        stream( GameLevel.values() )
                .forEach( ver -> arrayAdapter.add( ver.getTextRepresentation() ) );

        assertThat( LevelOne.map( arrayAdapter ) ).isEqualTo( 1 );
        assertThat( LevelTwo.map( arrayAdapter ) ).isEqualTo( 2 );
    }
}