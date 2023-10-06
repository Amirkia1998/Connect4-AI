
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import sac.game.AlphaBetaPruning;
import sac.game.GameSearchAlgorithm;
import sac.game.GameSearchConfigurator;
import sac.game.GameState;
import sac.game.GameStateImpl;
import sac.game.MinMax;
import sac.game.Scout;

public class Connect4 extends GameStateImpl {
	
	public static final byte O = -1; // we assume it as MIN player
	public static final byte E = 0;
	public static final byte X = 1; // we assume it as MAX player	
	public static final String[] NAMES = {"O", ".", "X"};
	
	private static final int m = 6; // no. of rows
	private static final int n = 7; // no. of columns
	private byte[][] board = null;

	public static final boolean MIN_PLAYER_HUMAN = false;
	public static final boolean MAX_PLAYER_HUMAN = true;
	
	public static final int GAME_STATUS_WIN = 0;
	public static final int GAME_STATUS_ONGOING = 1;
	public static final int GAME_STATUS_TIE = 2;
	
	public Connect4() {
		board = new byte[m][n];

		for (int i = 0; i < m; i++)
			for (int j = 0; j < n; j++)
				board[i][j] = E;
	}
	
	public Connect4(Connect4 parent) {
		board = new byte[m][n];

		for (int i = 0; i < m; i++)
			for (int j = 0; j < n; j++)
				board[i][j] = parent.board[i][j];

		setMaximizingTurnNow(parent.isMaximizingTurnNow());
	}	
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("");
		for (int i = 0; i < m; i++) {
			sb.append("|");
			for (int j = 0; j < n; j++) {
				sb.append(NAMES[1 + board[i][j]]);
				sb.append("|");
			}
			sb.append("\n");
		}

		sb.append(" ");

		for (int j = 0; j < n; j++) {
			sb.append(Integer.toString(j));
			sb.append(" ");
		}

		return sb.toString();
	}
	
	public boolean move(int j) { // j - index of column where one wants to move
		for (int i = m - 1; i >= 0; i--)
			if (board[i][j] == E) {
				board[i][j] = (isMaximizingTurnNow()) ? X : O;
				setMaximizingTurnNow(!isMaximizingTurnNow());
				this.setMoveName(String.valueOf(j));
				return true;
			}
		return false;
	}
	
	@Override
	public List<GameState> generateChildren() {
		List<GameState> children = new ArrayList<GameState>();

		for (int j = 0; j < n; j++) {
			Connect4 child = new Connect4(this);
			boolean isMoveValid = child.move(j);
			if (isMoveValid) {
				children.add(child);
				child.setMoveName(Integer.toString(j));
			}
		}

		return children;
	}
	
	public int checkStatus() {
		int[] di = new int[] {0, -1, -1, -1};
		int[] dj = new int[] {1, 1, 0, -1};

		int empties = 0;
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				if (board[i][j] == E)
					empties++;		
				for (int k = 0; k < 4; k++) {
					int tempTotal = 0;
					int is = i;
					int js = j;
					for (int q = 0; q < 4; q++) {
						if ((is >= 0) && (is < m) && (js >= 0) && (js < n))
							tempTotal += board[is][js];
						is += di[k];
						js += dj[k];
					}
					if (Math.abs(tempTotal) == 4)
						return GAME_STATUS_WIN;					
				}
			}
		}

		return (empties > 0) ? GAME_STATUS_ONGOING : GAME_STATUS_TIE;
	}
	
	@Override
	public int hashCode() { // not to visit the same state twice (once its score is ready)
		byte[] flatBoard = new byte[m * n];
		int k = 0;
		for (int i = 0; i < m; i++)
			for (int j = 0; j < n; j++)
				flatBoard[k++] = board[i][j];

		return Arrays.hashCode(flatBoard);
	}	
	
	public static void aiExecution(Connect4 c4, GameSearchAlgorithm algo) {
		System.out.println("AI to move, thinking...");
		algo.setInitial(c4);
		algo.execute();
//		System.out.println("Time [ms]: " + algo.getDurationTime());
		System.out.println("States analyzed: " + algo.getClosedStatesCount());
		System.out.println("Depth reached: " + algo.getDepthReached());
		System.out.println("Moves scores: " + algo.getMovesScores());
		String bestMove = algo.getFirstBestMove();
		System.out.println("Best move: " + bestMove);

		//why best move is sometimes null ?
		if (bestMove == null)
			c4.move(n/2);
		else
			c4.move(Integer.valueOf(bestMove));
	}

	//==============================================================================================GETTERS SETTERS
	public byte[][] getBoard() {
		return board;
	}
	public static int getM() {
		return m;
	}
	public static int getN() {
		return n;
	}

	//==================================================================================================MAIN
	public static void main(String[] args) {
		Connect4 c4 = new Connect4();
		Scanner scanner = new Scanner(System.in);

		boolean isMoveValid = false;
		System.out.println(c4);

		Connect4.setHFunction(new MyEvaluation()); // we hook up heuristic to the Connect4 class

		GameSearchAlgorithm algo = new AlphaBetaPruning();
		GameSearchConfigurator conf = new GameSearchConfigurator();
		conf.setDepthLimit(4.5);
		algo.setConfigurator(conf);

		while (true) {						
			if (c4.isMaximizingTurnNow()) {
				if (MAX_PLAYER_HUMAN) {
					do {
						System.out.println("Human (X player) to move:");
						String columnString = scanner.nextLine();
						int column = Integer.valueOf(columnString);
						isMoveValid = c4.move(column);
						System.out.println("Last move: " + c4.getMoveName());
					} while (!isMoveValid);
				}
				else {
					// AI ...
					Connect4.aiExecution(c4, algo);
					System.out.println("Last move: " + c4.getMoveName());
				}
			}
			else {
				if (MIN_PLAYER_HUMAN) {
					do {
						System.out.println("Human (O player) to move:");
						String columnString = scanner.nextLine();
						int column = Integer.valueOf(columnString);
						isMoveValid = c4.move(column);
						System.out.println("Last move: " + c4.getMoveName());
					} while (!isMoveValid);					
				}
				else {
					// AI ...
					Connect4.aiExecution(c4, algo);
					System.out.println("Last move: " + c4.getMoveName());
				}				
			}
			System.out.println(c4);

			int status = c4.checkStatus();
			if (status == GAME_STATUS_WIN) {
				String winningSymbol = (c4.isMaximizingTurnNow()) ? "O" : "X";
				System.out.println(winningSymbol + " PLAYER WON!");
				break;
			}
			else if (status == GAME_STATUS_TIE) {
				System.out.println("GAME ENDED WITH A TIE.");
				break;
			}
		}



		scanner.close();
	}

}