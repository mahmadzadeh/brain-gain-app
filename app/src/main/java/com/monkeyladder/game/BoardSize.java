package com.monkeyladder.game;

enum BoardSize {
     OneByOne(1,1),
     TwoByTwo(2,2),
     FourByFive(4,5);

    private final int rowCount;

    private final int colCount;
    BoardSize( int rowCount, int colCount ) {

        this.rowCount = rowCount;
        this.colCount = colCount;
    }

    public int rowCount( ) {
        return rowCount;
    }

    public int colCount( ) {
        return colCount;
    }


}
