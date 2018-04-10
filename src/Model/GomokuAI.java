package Model;

import java.util.ArrayList;
public class GomokuAI implements Player {
	
	EvaluationBoard eBoard;
	BoardState boardState;
	int playerFlag = 2; //
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
	 */
	public void evalChessBoard(int player, EvaluationBoard eBoard) {
		eBoard.resetBoard();
		for (int i =0; i<4; i++)
			evalChessBoardWithDirection(player,eBoard,i);

	}
	
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
	public int getScore(int index, boolean attack) {
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
		if(player==2){
			maxVal(boardState, alpha, beta, maxDepth);
		}else{
			minVal(boardState, alpha, beta, maxDepth);
		}
	}

	private int maxVal(BoardState state, int alpha, int beta, int depth) {
		evalChessBoard(2, eBoard);
		eBoard.MaxPos();
		int value = eBoard.evaluationBoard;
		if (depth < 0) {
			return value;
		}
		evalChessBoard(2, eBoard);
		ArrayList<Point> list = new ArrayList<>();
		for (int i = 0; i < maxMove; i++) {
			Point node = eBoard.MaxPos();
			if(node == null)break;
			list.add(node);
			eBoard.EBoard[node.x][node.y] = 0;
		}
		int v = Integer.MIN_VALUE;
		for (int i = 0; i < list.size(); i++) {
			Point com = list.get(i);
			state.setPosition(com.x, com.y, 2);
			v = Math.max(v, minVal(state, alpha, beta, depth-1));
			state.setPosition(com.x, com.y, 0);
			if(v>= beta || state.checkEnd(com.x, com.y)==2){
				goPoint = com;
				return v;
				
			}
			alpha = Math.max(alpha, v);
		}

		return v;
	}

	private int minVal(BoardState state, int alpha, int beta, int depth) {
		evalChessBoard(1, eBoard);
		eBoard.MaxPos();
		int value = eBoard.evaluationBoard;
		if (depth < 0) {
			return value;
		}
		evalChessBoard(1, eBoard);
		ArrayList<Point> list = new ArrayList<>();
		for (int i = 0; i < maxMove; i++) {
			Point node = eBoard.MaxPos();
			if(node==null)break;
			list.add(node);
			eBoard.EBoard[node.x][node.y] = 0;
		}
		int v = Integer.MAX_VALUE;
		for (int i = 0; i < list.size(); i++) {
			Point com = list.get(i);
			state.setPosition(com.x, com.y, 1);
			v = Math.min(v, maxVal(state, alpha, beta, depth-1));
			state.setPosition(com.x, com.y, 0);
			if(v<= alpha || state.checkEnd(com.x, com.y)==1){
				return v;
			}
			beta = Math.min(beta, v);
		}
		return v;
	}


	// @Override

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
	public int getPlayerFlag() {
		return playerFlag;
	}

	@Override
	public void setPlayerFlag(int playerFlag) {
		this.playerFlag = playerFlag;
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
