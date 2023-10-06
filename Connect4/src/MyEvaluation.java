import sac.graph.*;
import sac.StateFunction;
import sac.State;

//################ LOGIC BEHIND MY EVALUATION ###################
// these are the state scores for each condition.
// for sequences: 4=INFINITY, 3=100, 2=10, 1=1
// for first move at the middle: INFINITY
// for occupying corners: 100
// for playing middle: 0=0, 1=10, 2=20, 3=30, 2=20, ...
//##############################################################

public class MyEvaluation extends StateFunction {

    @Override
    public double calculate(State state) {
        Connect4 c4 = (Connect4) state;
        double stateScore = 0.0;

        stateScore += builtSequences(c4);
        stateScore += firstMoveStrategy(c4);
        stateScore += occupyCorners(c4);
        stateScore += playMiddles(c4);

        return stateScore;
    }

    private double builtSequences(Connect4 c4) {
        byte[][] board = c4.getBoard();
        int[] di = new int[] {0, -1, -1, -1};
        int[] dj = new int[] {1, 1, 0, -1};
        int m = Connect4.getM();
        int n = Connect4.getN();
        double totalScore = 0.0;

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                //if the current cell is not empty, we will count the sequence
                if (board[i][j] != Connect4.E) {
                    //for 4 different directions: right, up, and two diagonals
                    for (int k = 0; k < 4; k++) {
                        int x = j;
                        int y = i;
                        int seqScore = 0;

                        //we start from current cell (board[i][j]) and count for maximum of 4 cells:
                        for (int l = 0; l < 4; l++) {
                            //to check if the next cell is not out of bound:
                            if ( (y >= 0) && (y < m) && (x >= 0) && (x < n) ) {
                                //if next cell value is not the same as current cell value, the abs value of
                                //seqScore will be decreased (or if it is zero it will remain same)
                                // which means the next cell is not same as current cell so we break the loop
                                if (Math.abs(seqScore) >= Math.abs(seqScore + board[y][x])) {
                                    break;
                                }
                                seqScore += board[y][x];
                                y += di[k];
                                x += dj[k];
                            }
                        }
                        //if seqScore is abs(4), it means 100% Win. So no need to count anymore, and we return INFINITY.
                        //if not, add it to totalScore
                        if (seqScore == 4)
                            return Double.POSITIVE_INFINITY;
                        else if (seqScore == -4)
                            return Double.NEGATIVE_INFINITY;
                        else {
                            int score = Math.abs(seqScore);
                            switch (score) {
                                case 3 -> totalScore += 100*seqScore;
                                case 2 -> totalScore += 10*seqScore;
                                case 1 -> totalScore += seqScore;
                            }
                        }

                    }
                }
            }
        }
        return totalScore;
    }

    private double firstMoveStrategy (Connect4 c4) {
        byte[][] board = c4.getBoard();
        int m = Connect4.getM();
        int n = Connect4.getN();
        int numOfCells = m*n;
        int numOfEmpties = 0;

        //calculate the number of empty cells
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                if (board[i][j] == Connect4.E)
                    numOfEmpties++;

        // I want to force the AI to play middle column at first, so the returned evaluation score is Infinity.
        if (numOfEmpties == numOfCells-1 && board[m-1][n/2] == Connect4.X)
            return Double.POSITIVE_INFINITY;
        else if (numOfEmpties == numOfCells-1 && board[m-1][n/2] == Connect4.O) {
            return Double.NEGATIVE_INFINITY;
        }
        else if (numOfEmpties == numOfCells-2 && board[m-1][n/2] == Connect4.X && board[m-2][n/2] == Connect4.O) {
            return Double.NEGATIVE_INFINITY;
        }
        else if (numOfEmpties == numOfCells-2 && board[m-1][n/2] == Connect4.O && board[m-2][n/2] == Connect4.X) {
            return Double.POSITIVE_INFINITY;
        }
        else if (numOfEmpties == numOfCells-2 && board[m-1][n/2] == Connect4.X) {
            return Double.POSITIVE_INFINITY;
        }
        else if (numOfEmpties == numOfCells-2 && board[m-1][n/2] == Connect4.O) {
            return Double.NEGATIVE_INFINITY;
        }


       return 0.0;
    }

    private double occupyCorners(Connect4 c4) {
        byte[][] board = c4.getBoard();
        int m = Connect4.getM();
        int n = Connect4.getN();
        double totalScore = 0;

        if (board[m-1][0] == Connect4.X)
            totalScore += 100;
        if (board[m-1][n-1] == Connect4.X)
            totalScore += 100;
        if (board[m-1][0] == Connect4.O)
            totalScore += -100;
        if (board[m-1][n-1] == Connect4.O)
            totalScore += -100;

        return totalScore;
    }

    private double playMiddles(Connect4 c4) {
        byte[][] board = c4.getBoard();
        int n = Connect4.getN();
        int[] moveValues = new int[n];
        Double score;

        // initialize move values
        for (int i = 0; i <= n / 2; i++) {
            moveValues[i] = i;
            moveValues[n-1-i] = i;
        }

        // at the begining of the game, getMoveName returns a minus value (or null String)
        // because there is no move to evaluate yet (initial state)
        String column = c4.getMoveName();
        if (!column.isEmpty()) {
            Integer moveName = Integer.parseInt(column);
            if (moveName >= 0 && moveName < moveValues.length) {
                score = 10.0 * moveValues[moveName];
                return score;
            }
        }

        return 0.0;
    }


}


