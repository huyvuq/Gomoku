package Model;

import java.util.ArrayList;

/**
 * Gomoku AI
 * @author Huy Vu
 *
 */
public class GomokuAI implements Player {
	
	EvaluationBoard eBoard;
	BoardState boardState;
	int playerFlag = 2; //TODO: change and program this so user can decide to go first or second
	int _x, _y; //

	//Change these two variables to adjust difficulty
	public static int maxDepth = 12;
	public static int maxMove = 3;

	public boolean cWin = false;
	Point goPoint;
	
	public GomokuAI(BoardState board) {
		this.boardState = board;
		this.eBoard = new EvaluationBoard(board.width, board.height);
	}

	
	/**
	 * Evaluation function
	 * calculate the effectiveness
	 * player 1 = Human, player 2 = PC
	 * score calculated than applied to eBoard for alpha-beta pruning
	 */
	public void evalChessBoard(int player, EvaluationBoard eBoard) {
		eBoard.resetBoard();
		for (int i =0; i<4; i++)
			evalChessBoardWithDirection(player,eBoard,i);

	}
	
	/**
	 * Support function for evalChessBoard function
	 * @param player
	 * @param eBoard
	 * @param direction : Direction we need to check
	 */
	public void evalChessBoardWithDirection(int player, EvaluationBoard eBoard, int direction) {
		
		int ePC, eHuman;

		int rwStart = 0; //0,0,0,4
		int rwEnd = 0; //0,0,-4,0
		int clStart = 0; //0,0,0,4
		int clEnd = -4;
		
		int outerLoop = eBoard.width; 
		int innerLoop = eBoard.height;
		
		int d1 = 0;
		int d2 = 0;
		
		boolean swap = false;
		
		//direction: 0 = vertical, 1 = horizontal, 2 = diagnal (uppper to bottom), 3 = diagnal (bottom to upper)
		switch (direction){
		case 0:
			d2 = 1;
			break;
		case 1:
			d1 = 1;
			outerLoop = eBoard.height;
			innerLoop = eBoard.width;
			swap = true;
			break;
		case 2:
			rwEnd = -4;
			d1 = 1;
			d2 = 1;
			swap = true;
			break;
		case 3: 
			rwStart = 4;
			d1 = -1;
			d2 = 1;
			outerLoop = eBoard.height;
			innerLoop = eBoard.width;
			break;
		default:
			break;
		}
		for (int rw = rwStart; rw < (outerLoop + rwEnd); rw++)
			for (int cl = clStart; cl < (innerLoop + clEnd); cl++) {
				ePC = 0;
				eHuman = 0;
				int new_rw = swap ? cl : rw;
				int new_cl = swap ? rw : cl;
				
				for (int i = 0; i < 5; i++) {
					if (boardState.getPosition(new_rw+i*d1, new_cl+i*d2) == 1) //rw+i*d1, cl+i*d2, d1: 0,1,1,-1 d2: 1,0,1,1
						eHuman++;
					if (boardState.getPosition(new_rw+i*d1, new_cl+i*d2) == 2) //rw+i*d1, cl+i*d2
						ePC++;
				}
				if (eHuman * ePC == 0 && eHuman != ePC)
					for (int i = 0; i < 5; i++) { 
						if (boardState.getPosition(new_rw+i*d1, new_cl+i*d2) == 0) {//Check if positon is not filled
							if (eHuman == 0) // ePC #0
								if (player == 1)
									eBoard.EBoard[new_rw+i*d1][new_cl+i*d2] += getScore(ePC,false); //Add PC defense score
								else
									eBoard.EBoard[new_rw+i*d1][new_cl+i*d2] += getScore(ePC,true);//Add PC attack score
							if (ePC == 0) // eHuman #0
								if (player == 2) {
									eBoard.EBoard[new_rw+i*d1][new_cl+i*d2] += getScore(eHuman,false);//Add human defense score
								} else {
									eBoard.EBoard[new_rw+i*d1][new_cl+i*d2] += getScore(eHuman,true);//Add human attack score
								}
							if (eHuman == 4 || ePC == 4)
								eBoard.EBoard[new_rw+i*d1][new_cl+i*d2] *= 2;
						}
					}
			}
	}
	
	/**
	 * Generate score depending on type of player (human or pc)
	 * and move type (attack or defense, attack's point is twice as defense's)
	 * @param index
	 * @param attack
	 * @return
	 */
	public int getScore(int index, boolean attack) {
		if (index > 4)
			System.out.println(index);
		int finalScore = 0;
		if (index == 0)
			return finalScore;
		else if (index == 1)
			finalScore = 1;
		else {
			finalScore = (int) Math.pow(9, index - 1);
		}
		
		if (attack)
			finalScore *=2;
		return finalScore;
	}
	

	/**
	 * Alpha beta pruning
	 * @param alpha
	 * @param beta
	 * @param depth
	 * @param player
	 */
	public void alphaBeta(int alpha, int beta, int depth, int player) {
		if(player==2){ //PC
			minMax(boardState, alpha, beta, depth, true);
		}else{ //Human
			minMax(boardState, alpha, beta, depth, false);
		}
	}

	private int minMax(BoardState state, int alpha, int beta, int depth, boolean maximizingLayer) {
		int evalationValue = maximizingLayer == true? 2 : 1;
		
		evalChessBoard(evalationValue, eBoard); //First, evaluates and applies score to evaluation board
		eBoard.MaxPos(); //Get max score
		if (depth < 0) { //If we reach the depth limit, quit evaluating
			return eBoard.score;
		}
		ArrayList<Point> list = new ArrayList<>(); //Store list of points that has maximum values on the board when we try a set number of time (set with maxMove)
		for (int i = 0; i < maxMove; i++) {
			Point node = eBoard.MaxPos();
			if(node == null)break; //If there is no point has maximum value, quit
			list.add(node); 
			eBoard.EBoard[node.x][node.y] = 0;
		}
		int v =  maximizingLayer == true ? Integer.MIN_VALUE : Integer.MAX_VALUE;
		for (int i = 0; i < list.size(); i++) { //Loop through the list of max score points
			Point com = list.get(i);
			state.setPosition(com.x, com.y, 2);
			v = maximizingLayer ? Math.max(v, minMax(state, alpha, beta, depth-1, !maximizingLayer)) : Math.min(v, minMax(state, alpha, beta, depth-1, !maximizingLayer));;
			state.setPosition(com.x, com.y, 0);

			if (maximizingLayer) {
				if(v>= beta || state.checkEnd(com.x, com.y)==evalationValue){
					goPoint = com;
					return v;
					
				}
				alpha = Math.max(alpha, v);
			}
			else {
				if(v<= alpha || state.checkEnd(com.x, com.y)==evalationValue){
					return v;
				}
				beta = Math.min(beta, v);
			}
		}

		return v;
	}

	// @Override
	/**
	 * Calculate best move for a player
	 * player 2 = PC's move
	 * @param player 
	 * @return the positon (point where next move should be)
	 */
	public Point AI(int player) {
		alphaBeta(0, 1, 2,player);
		Point temp = goPoint;
		if (temp != null) {
			_x = temp.x;
			_y = temp.y;
		}
		return new Point(_x, _y);
	}

	@Override
	public BoardState getBoardState() {
		return boardState;
	}

	@Override
	public Point movePoint(int player) {
		return AI(player);
	}

}
