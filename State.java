public class State {
    private int[][] puzzle;
    private int rowPlace;
    private int columnPlace;
    private int zeroRow;
    private int zeroColumn;
    private int numberRow;
    private int numberColumn;
    private String location;
    private int movesNr;

    public State(int[][] puzzle) {
        this.puzzle = puzzle;
    }

    public int[][] getPuzzle() {
        return puzzle;
    }

    public void setPuzzle(int[][] puzzle) {
        this.puzzle = puzzle;
    }

    public int getRowPlace() {
        return rowPlace;
    }

    public int getColumnPlace() {
        return columnPlace;
    }

    public int getZeroRow() {
        return zeroRow;
    }

    public int getNumberRow() {
        return numberRow;
    }

    public int getNumberColumn() {
        return numberColumn;
    }

    public int getMovesNr() {
        return movesNr;
    }

    /**Function that swaps zero with the number on its right side
     */
    public void zeroToRight(){
        int changeNumber=puzzle[zeroRow][zeroColumn+1];
        puzzle[zeroRow][zeroColumn]=changeNumber;
        puzzle[zeroRow][zeroColumn+1]=0;
        zeroColumn+=1;
        movesNr+=1;
    }

    /**Function that swaps zero with the number which is right below it
     */
    public void zeroToBottom(){
        int changeNumber=puzzle[zeroRow+1][zeroColumn];
        puzzle[zeroRow][zeroColumn]=changeNumber;
        puzzle[zeroRow+1][zeroColumn]=0;
        zeroRow+=1;
        movesNr+=1;
    }

    /**Function that swaps zero with the number which is right on top of it
     */
    public void zeroToTop(){
        int changeNumber=puzzle[zeroRow-1][zeroColumn];
        puzzle[zeroRow][zeroColumn]=changeNumber;
        puzzle[zeroRow-1][zeroColumn]=0;
        zeroRow-=1;
        movesNr+=1;
    }

    /**Function that swaps zero with the number which is on the left from it
     */
    public void zeroToLeft(){
        int changeNumber=puzzle[zeroRow][zeroColumn-1];
        puzzle[zeroRow][zeroColumn]=changeNumber;
        puzzle[zeroRow][zeroColumn-1]=0;
        zeroColumn-=1;
        movesNr+=1;
    }

    /**Function that moves zero right and up
     */
    public void rightUp(){
        zeroToRight();
        zeroToTop();
    }

    /**Function that moves zero left and up
     */
    public void leftUp(){
        zeroToLeft();
        zeroToTop();
    }

    /**Function that moves zero right and down
     */
    public void rightDown(){
        zeroToRight();
        zeroToBottom();
    }

    /**Function that moves zero left and down
     */
    public void leftDown(){
        zeroToLeft();
        zeroToBottom();
    }

    /**Function that moves zero down and right
     */
    public void downRight(){
        zeroToBottom();
        zeroToRight();
    }

    /**Function that moves zero down and left
     */
    public void downLeft(){
        zeroToBottom();
        zeroToLeft();
    }

    /**Function that moves zero up and right
     */
    public void upRight(){
        zeroToTop();
        zeroToRight();
    }

    /**Function that moves zero up and left
     */
    public void upLeft(){
        zeroToTop();
        zeroToLeft();
    }

    /**Function that give simple numbers (1,2,3 and 4) their indexes
     * @param number that is needed to be put on its place
     */
    public void findSimpleNumbersProperties(int number){
        rowPlace = 0;
        findIndex(number);
        if(number==1||number==5) columnPlace = 0;
        else columnPlace = 1;
    }

    /**Function that decides which of swap numbers (3,4,7,8) need to be placed in place and places them
     * if they are in the right position (3 below 4 when 4 is on the actual 3 place and same goes with 7 and 8)
     * @param number number which turn it is
     */
    public void swapNumbersManipulations(int number){
        //Check if it is 4 or 8
        if(puzzle[0][2]==4*(5-puzzle.length)){

            if(puzzle[1][2]==4*(5-puzzle.length)-1){
                if(puzzle[1][1]==0){
                    downRight();
                }
                if(puzzle[2][2]==0){
                    rightUp();
                }
                if(puzzle[1][3]==0){
                    upLeft();
                    zeroToBottom();
                }
                else {
                    findIndex(number);
                    zeroFindNumber();
                }

                findIndex(number);
                rowPlace = numberRow;
            }

            //Situation when moving 3 or 7 below 4 or 8 will move 4 and 8 from their place (prevents infinite loop)
            else if (puzzle[0][3]==0 & puzzle[1][3]==7||puzzle[1][3]==3){
                rowPlace=2;
            }

            else{
                rowPlace=1;
            }
            columnPlace=2;
        }

        else{
            rowPlace = 0;
            columnPlace = 2;
            number+=1;
        }

        findIndex(number);
    }

    /**Function that decides which of last swap numbers (9,10,13 and 14) need to be placed in place and places them
     * if they are in the right position (13 next to 9 from the left side and 13 is on the 9's place)
     * @param number number which turn it is
     */
    public void lastSwampNumbersManipulations(int number){
        int n;

        findIndex(number);

        if(number==9||number==13){
            n=0;
        }
        else{n=1;}

        if(puzzle[0][n]==13+n){
            if(puzzle[0][1+n]==9+n){
                if (zeroRow == 0 & zeroColumn == 2 + n) {
                    zeroToBottom();
                }
                if(zeroRow == 1 & zeroColumn == 2 + n){
                    zeroToLeft();
                }
                if(zeroRow==1&zeroColumn==1+n){
                    zeroToLeft();
                }
                if(zeroRow==1&zeroColumn==n){
                    upRight();
                }
                else {
                    zeroFindNumber();
                }
                findIndex(number);
                rowPlace=numberRow;
                columnPlace=numberColumn;
            }
            else if(puzzle[1][n]==0&puzzle[1][1+n]==9+n){
                columnPlace=3;
                rowPlace=0;
            }
            else{
                columnPlace=1+n;
                rowPlace=0;
            }
        }

        else{
            number+=4;
            rowPlace=0;
            columnPlace=n;
            findIndex(number);
        }
    }

    /**Function that give indexes for the last 4 numbers (11,12,15 and 0)
     * @param number number which turn it is
     */
    public void finalNumbers(int number){
        findIndex(number);
        if(number==11||number==12) rowPlace=0;
        else rowPlace=puzzle.length-1;
        if(number==11||number==15) columnPlace=2;
        else columnPlace=3;
    }

    /**Function that moves zero towards the number that needs to be moved
     */
    public void zeroFindNumber(){
        findLocation();
        if(numberColumn>zeroColumn){
            numberColumn-=1;
            organizeMovement();
        }
        else if (numberColumn<zeroColumn){
            numberColumn+=1;
            organizeMovement();
        }
        else {
            organizeMovement();
        }
    }

    /**Function that finds where number is placed from it's real place
     * If number is somewhere below from it's expected place (where it should be), then it is needed to be
     * moved to the expected column and only then moved up
     * If number is somewhere above from it's expected place, then it is needed to be
     * moved down to the expected row and only then moved to the right column
     */
    public void findLocation(){
        if (rowPlace < numberRow) {
            location = "down";
        } else if (rowPlace> numberRow) {
            location = "up";
        } else {
            location = "right";
        }
    }

    /**
     * Function that decides where zero should move, in simple situations moves zero too
     */
    public void organizeMovement(){

        //one column
        if (zeroColumn == numberColumn){
            if (zeroRow == numberRow + 1){
                zeroBelowNumber();
            }
            else if (zeroRow == numberRow - 1) {
                zeroAboveNumber();
            }
            else if (zeroRow > numberRow) {
                zeroToTop();
            }
            else if (zeroRow < numberRow) {
                zeroToBottom();
            }

        }
        else if (zeroRow == numberRow){
            if (zeroColumn == numberColumn - 1){
                zeroOnLeft();
            }
            else if (zeroColumn == numberColumn + 1){
                zeroOnRight();
            }
            else if (zeroColumn < numberColumn) {
                zeroToRight();
            }
            else {
                zeroToLeft();
            }
        }
        else if (zeroRow < numberRow) {
            zeroToBottom();
        }

        else {
            if (numberColumn > zeroColumn) {
                zeroToRight();
            }
            if (numberColumn < zeroColumn) {
                zeroToLeft();
            }
        }
    }

    /**
     * If zero is below number then moves zero to the right place so number could move where needed
     */
    public void zeroBelowNumber(){

        if (location.equals("right") || location.equals("down")) {
            if (columnPlace > numberColumn) {
                rightUp();
            } else if (columnPlace < numberColumn) {
                leftUp();
            }
            else {
                if (numberColumn < 3) {
                    rightUp();
                } else {
                    leftUp();
                }
            }
        }

        else if (location.equals("up")) {
            zeroToTop();
            numberRow+=1;
        }
    }

    /**
     * If zero is right above number then moves zero to the right place so number could move where needed
     */
    public void zeroAboveNumber(){
        if (location.equals("right") || location.equals("down")) {
            if (columnPlace > numberColumn) {
                rightDown();
            } else if (columnPlace < numberColumn) {
                leftDown();
            }
            else {
                zeroToBottom();
                numberRow-=1;
            }
        } else if (location.equals("up")) {
            if (numberColumn < 3) {
                rightDown();
            } else {
                leftDown();
            }
        }
    }

    /**
     * If zero is right on the left side from number then moves zero to the right place so number could move where needed
     */
    public void zeroOnLeft(){
        if(location.equals("right")||location.equals("down")) {
            if (columnPlace > numberColumn) {
                if (numberRow < puzzle.length - 1) {
                    downRight();
                }
                else {
                    upRight();
                }
            } else if (columnPlace < numberColumn) {
                zeroToRight();
                numberColumn-=1;

            } else {
                if (numberRow < puzzle.length - 1) {
                    downRight();
                } else {
                    upRight();
                }
            }
        }
        else if(location.equals("up")){
            downRight();
        }
    }

    /**
     * If zero is right on the right side from number then moves zero to the right place so number could move where needed
     */
    public void zeroOnRight(){

        if(location.equals("right")||location.equals("down")){
            if (columnPlace > numberColumn) {
                zeroToLeft();
                numberColumn+=1;
            }
            else if (columnPlace < numberColumn) {
                if(numberRow<puzzle.length-1){
                    downLeft();
                }
                else {
                    upLeft();
                }
            }
            else {
                upLeft();
            }
        }
        else if(location.equals("up")){
            downLeft();
        }
    }

    /**Function that finds exact position of a specified number
     * @param number which position is needed
     */
    public void findIndex(int number){
        for (int i = 0; i < puzzle.length; i++) {
            for (int j = 0; j < puzzle[i].length; j++) {
                if (puzzle[i][j]==number) {
                    if (number==0){
                        zeroRow=i;
                        zeroColumn=j;
                    }
                    else{
                        numberRow=i;
                        numberColumn=j;
                    }
                }
            }
        }
    }
}