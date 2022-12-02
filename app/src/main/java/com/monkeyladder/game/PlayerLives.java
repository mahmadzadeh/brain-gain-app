package com.monkeyladder.game;

public enum PlayerLives {
    Zero( 0 ) {
        @Override
        public PlayerLifeStatus getHealth( ) {
            return PlayerLifeStatus.Danger;
        }
    },
    One( 1 ) {
        @Override
        public PlayerLifeStatus getHealth( ) {
            return PlayerLifeStatus.Danger;
        }
    },
    Two( 2 ) {
        @Override
        public PlayerLifeStatus getHealth( ) {
            return PlayerLifeStatus.Warning;
        }
    },
    Three( 3 ) {
        @Override
        public PlayerLifeStatus getHealth( ) {
            return PlayerLifeStatus.Healthy;
        }
    };

    int lifeCount;

    PlayerLives( int i ) {
        lifeCount = i;
    }

    public static PlayerLives fromLifeCount( int count ) {
        for ( PlayerLives lives : PlayerLives.values() ) {
            if ( lives.lifeCount == count ) {
                return lives;
            }
        }

        throw new IllegalArgumentException( String.format( "invalid life count give %s", count ) );
    }

    public static PlayerLives getDefaultStartingValue( ) {
        return Three;
    }

    public int getLifeCount( ) {
        return lifeCount;
    }

    public abstract PlayerLifeStatus getHealth( );
}
