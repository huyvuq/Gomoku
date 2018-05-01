package Model;

/**
 * Class: - Evaluation Gomoku Board
 * used to store defense/attack score of each move
 * on the board
 * @author Huy Vu
 *
 */
public class EvaluationBoard {
	public int height, width;
	public int[][] EBoard;

	/**
	 * Constructor
	 * @param height
	 * @param width
	 */
	public EvaluationBoard(int height, int width) {
		this.height = height;
		this.width = width;
		EBoard = new int[height][width];
	}

	/**
	 * Clear evaluation board
	 */
	public void resetBoard() {
		for (int r = 0; r < height; r++)
			for (int c = 0; c < width; c++)
				EBoard[r][c] = 0;
	}

	/*
	 * 
	 */
	public void setPosition(int x, int y, int flag) {
		EBoard[x][y] = flag;
	}

	/**
	 * Find and return the positon where score is maximum
	 * Also modify the evaluationBoard value (which stores the score)
	 * @return
	 */
	public Point MaxPos() {
		int Max = 0;
		Point p = new Point();
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				if (EBoard[i][j] > Max) {
					p.x = i;
					p.y = j;
					Max = EBoard[i][j];
				}
			}
		}
		if (Max == 0) {
			return null;
		}
		score = Max;
		return p;
	}
	public int score = 0;
	
}
