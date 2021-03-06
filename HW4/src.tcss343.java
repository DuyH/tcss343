import java.io.*;
import java.util.*;

/**
 * Created by duy on 11/26/15.
 */
public class tcss343 {
    private static int MAX_VALUE = 100; // Maximum value for random generator

    public static void main(String[] args) throws FileNotFoundException {

        // Grab file name from argument
        String fileName = args[0];

        // Obtain posts matrix from input text file
        int[][] matrixFromFile = getMatrixFromFile(fileName);

        // Generate & output to file random matrices of size 100, 200, 400, 600, 800
        // writeRandomMatrix(100, "input100.txt");
        // writeRandomMatrix(200, "input200.txt");
        // writeRandomMatrix(400, "input400.txt");
        // writeRandomMatrix(600, "input600.txt");
        // writeRandomMatrix(800, "input800.txt");

        // 3.1 - BRUTE FORCE
        long startTime = System.currentTimeMillis();
        printBruteSolution(matrixFromFile);
        long endTime = System.currentTimeMillis();
        System.out.println("Brute Force Runtime: " + (endTime - startTime) + "ms");

        // 3.2 - DIVIDE & CONQUER
        startTime = System.currentTimeMillis();
        // [YOUR CODE HERE]
        endTime = System.currentTimeMillis();
        System.out.println("Divide and Conquer Runtime: " + (endTime - startTime) + "ms");

        // 3.3 - DYNAMIC PROGRAMMING
        startTime = System.currentTimeMillis();
        // [YOUR CODE HERE]
        endTime = System.currentTimeMillis();
        System.out.println("Dynamic Programming Runtime: " + (endTime - startTime) + "ms");

    }

    /**
     * Prints out to console the best path to take with cost.
     * @param matrix Input matrix of post values
     */
    public static void printBruteSolution(int[][] matrix) {

        /* Cost Analysis:
                Each possible solution row has at most O(n) elements in a path
                Each element has a constant lookup time of accessing the post cost matrix.
                There are #possiblePaths * n elements:         ((n-2)(n-2)/2+1) * n elements
                Each element is asked a conditional so:        2((n-2)(n-1)/2+1) * n) cost
                Each lookup involves an addition operation so: 3((n-2)(n-1)/2+1) * n) cost
                Adding to treemap is log(n) insertion cost so: log((n-2)(n-1)/2+1)

                Total cost: 3n((n-2)(n-1)/2+1) + log((n-2)(n-1)/2+1)

           Runtime Tests (ms) on my MacBook Pro, 2.7GHz i5, 64bit, 8GB RAM:
                n        Trial 1 | Trial 2 | Trial 3
                100      97        105       81
                200      201       199       209
                400      3626      3898      3066
                600      10579     11753     11954
                800      OutOfMem  OutOfMem  OutOfMem
        */

        // Number of posts 'n'
        int numPosts = matrix.length;

        // A map containing paths as key, costs as value.
        // TreeMap uses a Red-Black tree, log(n) insertion
        TreeMap<Integer, ArrayList<Integer>> pathCosts = new TreeMap<>();

        // Retrieve all possible paths. O(n^2) runtime
        Set<ArrayList<Integer>> possibleSolutions = getPossiblePaths(numPosts);
        // System.out.println(possibleSolutions);

        // Fill map with all possible solutions and their cost.
        for (ArrayList<Integer> row : possibleSolutions) {
            int cost = 0;
            Integer current = null;
            // Look at previous post and current post to know where to go in cost matrix
            for (Integer next : row) {
                if (current != null) {
                    cost += matrix[current-1][next-1];
                }
                current = next;
            }
            pathCosts.put(cost, row);
        }
        // System.out.println(pathCosts);
        System.out.println("Best path via Brute Force: " + pathCosts.firstEntry().getValue() + " with cost of " + pathCosts.firstKey());
    }


    /**
     * Create a matrix from input text file.
     * @param fileName Name of text file.
     * @return int[][] array of post values.
     * @throws FileNotFoundException
     */
    public static int[][] getMatrixFromFile(String fileName) throws FileNotFoundException {
        Scanner input = new Scanner(new FileReader(fileName)).useDelimiter("\t");
        String[] line;
        int numPosts;
        int[][] matrix = null;

        if (input.hasNextLine()) {
            // First obtain the number of posts:
            line = input.nextLine().split("\t");
            numPosts = line.length;
            matrix = new int[numPosts][numPosts];
            input.close();
            input = new Scanner(new FileReader(fileName)).useDelimiter("\t");

            // Now loop through the rows and build up the matrix
            if (input.hasNextLine()) {
                for(int i = 0; i<numPosts;i++) {
                    line = input.nextLine().split("\t");
                    for (int j = 0; j < numPosts; j++) {
                        if (line[j].equals("NA")) {
                            matrix[i][j] = 0;
                        } else {
                            matrix[i][j] = Integer.parseInt(line[j]);
                        }
                    }
                }
                input.close();
                }
        }
        return matrix;
    }

    /* // Getting a matrix from file BUT returning a LIST instead of a int[][]
    public static List getMatrixFromFile(String fileName) throws FileNotFoundException {
        Scanner input = new Scanner(new FileReader(fileName)).useDelimiter("\t");

        List<ArrayList<Integer>> matrix = new ArrayList<>();

        while (input.hasNextLine()) {
            ArrayList<Integer> path = new ArrayList<>();
            String[] line = input.nextLine().split("\t");
            for (String postValue : line) {
                if (postValue.equals("NA")) {
                    path.add(0);
                } else {
                    path.add(Integer.parseInt(postValue));
                }
            }

            matrix.add(path);
        }
        // System.out.print(matrix);
        return matrix;
    }
    */

    /**
     * Fill a nxn matrix of post values where only upper-right above diagonal is filled
     * @param n Number of posts.
     * @return a 2d int[][] array
     */
    public static int[][] getRandomMatrix(int n) {
        Random random = new Random();

        // DUY'S NOTE: the following lines that are commented out are for implementing a Collection instead of int[][]

        // List<ArrayList<Integer>> matrix = new ArrayList<>();
        int[][] matrix = new int[n][n];
        for (int i = 1; i <= n; i++) {
            // ArrayList<Integer> row = new ArrayList<>();
            for(int j=1;j<=n;j++) {
                if (j <= i) {
                    matrix[i-1][j-1] = 0;
                } else {
                    matrix[i-1][j-1] = random.nextInt(MAX_VALUE) + 1; // +1 to avoid 0 cost
                }
            }
            // matrix.add(row);
        }
        return matrix;
    }


    // 3.5 in the assignment says we have to create text files for the random matrices
    public static void writeRandomMatrix(int n, String fileName) {

        int[][] matrix = getRandomMatrix(n);

        try {

            File file = new File("src/" + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            for(int i = 0;i<matrix.length;i++) {
                for(int j = 0; j<matrix.length;j++) {
                    bw.write(Integer.toString(matrix[i][j]));
                    if (j != matrix.length - 1) {
                        bw.write("\t");
                    }
                }
                bw.newLine();
            }
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Return a collection of paths. Each path consists of posts to be visited.
     * Closed formula for total number of paths: (n-2)(n-1)/2 + 1 => O(n^2)
     *
     * @param numPosts Input the number of posts from problem
     * @return A set of path arrays
     */
    public static Set<ArrayList<Integer>> getPossiblePaths(int numPosts) {
        Set<ArrayList<Integer>> pathList = new HashSet<>();

        for (int p = numPosts; p > 2; p--) {
            for (int i = 2; i < numPosts; i++) {
                ArrayList<Integer> arr = new ArrayList<>();
                arr.add(1); // Add first post
                for (int j = i; j < p; j++) {
                    arr.add(j); // Add posts in between
                }
                arr.add(numPosts); // Add last post
                pathList.add(arr);
            }
        }

        return pathList;
    }

    /**
     * Print the matrix containing post values from a 2d array[][]
     * @param array The 2d array of post values.
     */
    public static void printMatrix(int[][] array) {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                System.out.print(array[i][j]);
                System.out.print("\t");
            }
            System.out.println();
        }

    }

    /**
     * Print the matrix containing post values from a List
     * @param matrix The array List of post values.
     */
    public static void printMatrix(List<ArrayList<Integer>> matrix) {
        for (ArrayList<Integer> m : matrix) {
            for (Integer i : m) {
                System.out.print(i + "\t");
            }
            System.out.println();
        }
    }

}
