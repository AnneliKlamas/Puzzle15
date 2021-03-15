import java.io.*;
import java.util.*;

public class Puzzle15 {
    public static void main(String[] args) throws IOException {
        String dirPath = "Puzzles";
        String[] files = findFiles(dirPath);

        for (String file:files) {
            int[][] puzzle = readFile(file, dirPath);
            State state = new State(puzzle);
            if(puzzle!=null) {
                if (isSolvable(state)){
                    try {
                        System.out.println(file + " - " + solvePuzzle(state));
                    }
                    catch (Error | Exception e) {
                        errorMessages(3, file);
                    }
                }
                else {
                    errorMessages(1,file);
                }
            }
        }
    }

    /** Method that finds files with ".p15" extension from a directory
     * @param dirPath is a directory path as String
     * @return String array with file names
     */
    public static String[] findFiles(String dirPath) {
        File directory = new File(dirPath);
        FilenameFilter filter = (file, name) -> name.endsWith(".p15");
        return directory.list(filter);
    }

    /**Method that reads file in and returns 2D array of a puzzle
     * @param fileName name of the file as a String
     * @param dirPath directory path as a name
     * @return 2D array of a puzzle if file contains correct form of the puzzle
     * it doesn't it will return null and print error message
     */
    public static int[][] readFile(String fileName, String dirPath) throws IOException {
        int[][] puzzle = new int[4][4];
        int lineNr = 0;
        String line;
        int number;
        Set<Integer> expectedNumbers = new HashSet<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15));
        Set<Integer> realNumbers = new HashSet<>();
        File file;
        file = new File(dirPath +"\\"+fileName);
        BufferedReader br = new BufferedReader(new FileReader(file));

        while ((line = br.readLine()) != null) {
            lineNr++;
            line = line.trim();
            //If there are empty lines it will detect and ignore them
            if (line.length()==0) lineNr--;
            else {
                if (lineNr > puzzle.length) {
                    errorMessages(2, fileName);
                    return null;
                }
                String[] lineChars = line.split("\\s+");
                if (lineChars.length != 4) {
                    errorMessages(2, fileName);
                    return null;
                }
                for (int i = 0; i < lineChars.length; i++) {
                    try {
                        number = Integer.parseInt(lineChars[i]);
                    } catch (Exception e) {
                        br.close();
                        errorMessages(2, fileName);
                        return null;
                    }
                    if (!expectedNumbers.contains(number)) {
                        br.close();
                        errorMessages(2, fileName);
                        return null;
                    }
                    puzzle[lineNr - 1][i] = number;
                    realNumbers.add(Integer.parseInt(lineChars[i]));
                }
            }
        }
        if (expectedNumbers.size()!=realNumbers.size()) {
            br.close();
            errorMessages(2, fileName);
            return null;
        }
        br.close();
        return puzzle;
    }

    /**Function that creates error messages
     * @param errorNr error number from 1 to 2 where
     * -1 : The file contains a setup that cannot reach fig 1
     * -2 : The file contains an invalid input (for example letters or numbers over 15, duplicate numbers, etc.)
     * -3 : Unexpected error
     * @param fileName name of the file that has specified error
     */
    public static void errorMessages(int errorNr, String fileName){
        if (errorNr==1) System.err.println(fileName+" - -1");
        else if (errorNr==2) System.err.println(fileName+" - -2");
        else if (errorNr==3) System.err.println(fileName+" - -3");
    }

    /**Recursive function that solves puzzle
     * @return number of moves that were needed to complete the puzzle
     */
    public static int solvePuzzle(State state){

        //Different numbers need to be in different positions to solve
        Set<Integer> simpleNumbers = new HashSet<>(Arrays.asList(1, 2, 5, 6));
        Set<Integer> swapNumbers = new HashSet<>(Arrays.asList(3, 4, 7, 8));
        Set<Integer> lastSwapNumbers = new HashSet<>(Arrays.asList(9, 13, 10, 14));
        int i = 0;
        int number = 4 * (5 - state.getPuzzle().length) - 3;

        while (state.getPuzzle()[0][i] == number) {
            i++;
            number++;
            if (i > 3) break;
        }
        //if first row is correct then it doesn't need to be changed, so it can be deleted
        if (i == 4) {
            int[][] unsortedSpace = new int[state.getPuzzle().length - 1][4];
            for (int j = 1; j < state.getPuzzle().length; j++) {
                System.arraycopy(state.getPuzzle()[j], 0, unsortedSpace[j - 1], 0, state.getPuzzle()[j].length);
            }
            state.setPuzzle(unsortedSpace);
        }
        else {
            //checks if puzzle is solved
            if (state.getPuzzle().length == 1) {
                if (state.getPuzzle()[0][3] == 0) return state.getMovesNr();
            }

            state.findIndex(0);

            if (simpleNumbers.contains(number)) {
                state.findSimpleNumbersProperties(number);
            } else if (swapNumbers.contains(number)) {
                state.swapNumbersManipulations(number);
            } else if (lastSwapNumbers.contains(number)) {
                state.lastSwampNumbersManipulations(number);
            } else {
                state.finalNumbers(number);
            }

            //puts number in it's place
            while (state.getRowPlace() != state.getNumberRow() || state.getColumnPlace() != state.getNumberColumn()) {
                state.findLocation();
                state.organizeMovement();
            }

        }
        return solvePuzzle(state);
    }

    /**Function that checks if puzzle is solvable
     * @return boolean answer
     */
    public static boolean isSolvable(State state){
        state.findIndex(0);
        int number = state.getZeroRow();
        int[] array = new int[16];
        int position = 0;
        int inversions=0;

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (state.getPuzzle()[i][j]!=0){
                    array[position++] = state.getPuzzle()[i][j];
                }
            }
        }
        for (int i = 0; i < 14; i++) {
            for (int j = i+1; j < 15; j++) {
                if (array[i]>array[j]) inversions+=1;
            }
        }

        if (number%2==0) return inversions % 2 != 0;
        else return inversions % 2 == 0;
    }
}