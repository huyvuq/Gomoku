package Model;

public interface Player {
	public Point movePoint(int player);

	int getPlayerFlag();

	void setPlayerFlag(int playerFlag);

	BoardState getBoardState();
}
