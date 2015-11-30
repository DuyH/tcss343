/**
 * Created by duy on 11/26/15.
 */
public class challenge {

    public static void main(String[] args) {

        // Create boolean matrix of stones from command line arguments
        int fieldSize = Integer.parseInt(args[0]);
        boolean[][] field = new boolean[fieldSize][fieldSize];

        // Insert the stones
        int numStones = Integer.parseInt(args[1]);
        for (int i = 0; i < numStones * 2; i += 2) {
            field[Integer.parseInt(args[i + 2])][Integer.parseInt(args[i + 3])] = true;
        }
        // printMatrix(field);

        // Find the solution given the boolean matrix of stones
        findLargestSquare(field);

    }


    /**
     * Finds the largest valid square that contains no stones in a given field of nxn size.
     * @param field A boolean matrix where true means a stone.
     */
    public static void findLargestSquare(boolean[][] field) {
        int x = 0; // x of largest square
        int y = 0; // y of largest square
        int maxSize = 0; // size of largest square

        // Check each cell for stone, if not expand the square and continue checking for stones while keeping max size.
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field.length; j++) {
                int size = 0;
                while (validSolution(field.length, field, size, i, j)) {
                    // Update largest square and its coordinates
                    if (size > maxSize) {
                        maxSize = size;
                        x = i;
                        y = j;
                    }
                    size++;
                }
            }
        }


        System.out.println("Solution at (" + x + ", " + y + ") with size " + maxSize);
    }

    /**
     * Checks whether the proposed solution square is valid.
     * @param fieldSize Size nxn of field.
     * @param field Boolean matrix where stones are indicated as true.
     * @param targetSize Height and width of proposed solution square.
     * @param x Top left x of solution square.
     * @param y Top left y of solution square.
     * @return Whether the proposed solution square is valid (void of stones).
     */
    public static boolean validSolution(int fieldSize, boolean[][] field, int targetSize, int x, int y) {

        // Check if target size is impossible given the x,y (out of bounds)
        if (x + targetSize > fieldSize || y + targetSize > fieldSize) {
            return false;
        }

        // Starting with 1x1 square at the given (x,y), check for stones until targetSize reached.
        for (int i = x; i < targetSize; i++) {
            for (int j = y; j < targetSize; j++) {
                if (field[j][i]) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Print the matrix containing post values from a 2d array[][]
     * @param array The 2d array of post values.
     */
    public static void printMatrix(boolean[][] array) {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                System.out.print(array[i][j]);
                System.out.print("\t");
            }
            System.out.println();
        }

    }
}
