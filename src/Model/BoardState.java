package Model;

public class BoardState {
	public static int[][] boardArr;
	public int width;
	public int height;

	public BoardState(int width, int height) {
		boardArr = new int[width][height];
		this.height = height;
		this.width = width;
	}
	public void resetBoard(){
		boardArr = new int[width][height];
	}
	public int checkEnd(int rw, int cl) {
		int r = 0, c = 0;
		
		// Check horizontally
		while (c < width - 4) {
			if (getCheckResult(rw,c,0,1) != 0)
				 return getCheckResult(rw,c,0,1);
			c++;
		}

		// Check vertically
		while (r < height - 4) {
			if (getCheckResult(r,cl,1,0) != 0)
				 return getCheckResult(r,cl,1,0);
			r++;
		}

		// Check diagnally (top to bottom)
		r = rw;
		c = cl;
		while (r > 0 && c > 0) {
			r--;
			c--;
		}
		while (r < height - 4 && c < width - 4) {
			if (getCheckResult(r,c,1,1) != 0)
				 return getCheckResult(r,c,1,1);
			r++;
			c++;
		}

		// Check diagnally (bottom to top)
		r = rw;
		c = cl;
		while (r < height - 1 && c > 0) {
			r++;
			c--;
		}
		while (r >= 4 && c < height - 4) {
			if (getCheckResult(r,c,-1,1) != 0)
				 return getCheckResult(r,c,-1,1);
			
			r--;
			c++;
		}
		return 0;
	}

	public int getCheckResult(int r, int c, int a, int b) {
		boolean human = true;
		boolean pc = true;
		for (int i = 0; i < 5; i++) {
			if (boardArr[r+a*i][c+b*i] != 1)
				human = false;
			if (boardArr[r+a*i][c+b*i] != 2)
				pc = false;
		}
		if (human)
			return 1;
		if (pc)
			return 2;
		
		return 0;
	}
	public int getPosition(int x, int y) {
		return boardArr[x][y];
	}

	public void setPosition(int x, int y, int player) {
		boardArr[x][y] = player;
	}
}
