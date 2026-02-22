package com.monkeyladder.game;


import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.stream.IntStream.range;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public enum GameLevel implements Comparable<GameLevel>{

    Null( 0 ) {
        @Override
        public Optional<GameLevel> nextLevelUp( ) {
            return empty();
        }

        @Override
        public Optional<GameLevel> previousLevelDown( ) {
            return empty();
        }
    },
    LevelOne( 1 ) {
        @Override
        public Optional<GameLevel> nextLevelUp( ) {
            return of( LevelTwo );
        }

        @Override
        public Optional<GameLevel> previousLevelDown( ) {
            return empty();
        }
    },
    LevelTwo( 2 ) {
        @Override
        public Optional<GameLevel> nextLevelUp( ) {
            return of( LevelThree );
        }

        @Override
        public Optional<GameLevel> previousLevelDown( ) {
            return of( LevelOne );
        }
    },
    LevelThree( 3 ) {
        @Override
        public Optional<GameLevel> nextLevelUp( ) {
            return of( LevelFour );
        }

        @Override
        public Optional<GameLevel> previousLevelDown( ) {
            return of( LevelTwo );
        }
    },
    LevelFour( 4 ) {
        @Override
        public Optional<GameLevel> nextLevelUp( ) {
            return of( LevelFive );
        }

        @Override
        public Optional<GameLevel> previousLevelDown( ) {
            return of( LevelThree );
        }
    },
    LevelFive( 5 ) {
        @Override
        public Optional<GameLevel> nextLevelUp( ) {
            return of( LevelSix );
        }

        @Override
        public Optional<GameLevel> previousLevelDown( ) {
            return of( LevelFour );
        }
    },
    LevelSix( 6 ) {
        @Override
        public Optional<GameLevel> nextLevelUp( ) {
            return of( LevelSeven );
        }

        @Override
        public Optional<GameLevel> previousLevelDown( ) {
            return of( LevelFive );
        }
    },
    LevelSeven( 7 ) {
        @Override
        public Optional<GameLevel> nextLevelUp( ) {
            return of( LevelEight );
        }

        @Override
        public Optional<GameLevel> previousLevelDown( ) {
            return of( LevelSix );
        }
    },
    LevelEight( 8 ) {
        @Override
        public Optional<GameLevel> nextLevelUp( ) {
            return of( LevelNine );
        }

        @Override
        public Optional<GameLevel> previousLevelDown( ) {
            return of( LevelSeven );
        }
    },
    LevelNine( 9 ) {
        @Override
        public Optional<GameLevel> nextLevelUp( ) {
            return Optional.of( LevelTen );
        }

        @Override
        public Optional<GameLevel> previousLevelDown( ) {
            return of( LevelEight );
        }
    },
    LevelTen( 10 ) {
        @Override
        public Optional<GameLevel> nextLevelUp( ) {
            return of( LevelEleven );
        }

        @Override
        public Optional<GameLevel> previousLevelDown( ) {
            return of( LevelNine );
        }
    },
    LevelEleven( 11 ) {
        @Override
        public Optional<GameLevel> nextLevelUp( ) {
            return Optional.of( LevelTwelve );
        }

        @Override
        public Optional<GameLevel> previousLevelDown( ) {
            return of( LevelTen );
        }
    },
    LevelTwelve( 12 ) {
        @Override
        public Optional<GameLevel> nextLevelUp( ) {
            return of( LevelThirteen );
        }

        @Override
        public Optional<GameLevel> previousLevelDown( ) {
            return of( LevelEleven );
        }
    },
    LevelThirteen( 13 ) {
        @Override
        public Optional<GameLevel> nextLevelUp( ) {
            return of( LevelFourteen );
        }

        @Override
        public Optional<GameLevel> previousLevelDown( ) {
            return of( LevelTwelve );
        }
    },
    LevelFourteen( 14 ) {
        @Override
        public Optional<GameLevel> nextLevelUp( ) {
            return of( LevelFifteen );
        }

        @Override
        public Optional<GameLevel> previousLevelDown( ) {
            return of( LevelThirteen );
        }
    },
    LevelFifteen( 15 ) {
        @Override
        public Optional<GameLevel> nextLevelUp( ) {
            return of( LevelSixteen );
        }

        @Override
        public Optional<GameLevel> previousLevelDown( ) {
            return of( LevelFourteen );
        }
    },
    LevelSixteen( 16 ) {
        @Override
        public Optional<GameLevel> nextLevelUp( ) {
            return empty();
        }

        @Override
        public Optional<GameLevel> previousLevelDown( ) {
            return of( LevelFifteen );
        }
    };

    private final int cellCount;

    GameLevel( int i ) {
        cellCount = i;
    }

    public static Optional<GameLevel> fromTextValue( String textValue ) {

        return Optional.empty();
    }

    public int map( List<CharSequence> adapter ) {
        return range( 0, adapter.size() )
                .filter( index -> adapter.get( index ).toString().equals( getTextRepresentation() ) )
                .findFirst()
                .orElse( 1 );
    }

    public static GameLevel lastLevel() {
        List<GameLevel> levels = Arrays.stream( GameLevel.values() ).collect( Collectors.toList() );
        Collections.sort( levels, (o1,o2)-> o1.compareTo( o2 ) );

        return levels.get(levels.size()-1);
    }

    public String getTextRepresentation( ) {
        return this.toString();
    }

    public int cellCount( ) {
        return cellCount;
    }

    public abstract Optional<GameLevel> nextLevelUp( );

    public abstract Optional<GameLevel> previousLevelDown( );
}