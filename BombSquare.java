import java.util.*;

public class BombSquare extends GameSquare
{
	private boolean thisSquareHasBomb = false;
	private boolean clicked = false;
	private boolean lost = false;
	public static final int MINE_PROBABILITY = 10;

	public BombSquare(int x, int y, GameBoard board) {
		super(x, y, "images/blank.png", board);

		Random r = new Random();
		thisSquareHasBomb = (r.nextInt(MINE_PROBABILITY) == 0);
	}

	public void clicked()
	{
		if (lost) return; // If the user clicked on a bomb then he can't click on any button
		if (clicked) return; // If a button has been already clicked it can't be clicked again, or that might result in an infinite recursion
		clicked = true;

		if (thisSquareHasBomb) {setImage("images/bomb.png"); gameFinished(); return;} // If a user clicked a bomb we set the image and the game finishes

		int neighbour_bombs = countNeighbourBombs();
		setImage("images/" + neighbour_bombs + ".png");
		
		if (neighbour_bombs != 0) return; // If the current square neighbours contains bombs then we just display the square and return

		// We run the clicked method on each neighbour of the zero square to display it
		ArrayList<BombSquare> neighbours = getNeighbours();
		for (int i = 0; i < neighbours.size(); i++)
			neighbours.get(i).clicked();
	}

	private Boolean containsBomb() {
		return thisSquareHasBomb;
	}

	// Returns an integer represeting the number of bombs in the neighbours of a square
	private int countNeighbourBombs() {
		ArrayList<BombSquare> neighbours = getNeighbours();

		int count_bombs = 0;
		for (int i = 0; i < neighbours.size(); i++) // That is O(1)
			if (neighbours.get(i).containsBomb()) count_bombs++;

		return count_bombs;
	}

	// Returns an ArrayLists containg all neighbours of the current Square
	private ArrayList<BombSquare> getNeighbours() {
		// Direction Array
		int di[] = {-1, -1, 0, 1, 1, 1, 0, -1};
		int dj[] = {0, 1, 1, 1, 0, -1, -1, -1};

		ArrayList<BombSquare> neighbours = new ArrayList<BombSquare>();
		for (int d = 0; d < 8; d++) {
			int x_neighbour = xLocation + di[d];
			int y_neighbour = yLocation + dj[d];

			BombSquare neighbour = (BombSquare) board.getSquareAt(x_neighbour, y_neighbour);
			if (neighbour == null) continue;
			neighbours.add(neighbour);
		}

		return neighbours;
	}

	// setLost is to indicate that the game have finished
	public void setLost(boolean lost) {
		this.lost = lost;
	}

	// Goes and each Square in on the Board and sets it to lost, to indicate that the game have finished
	private void gameFinished() {
		// Counting the Number of Rows in the Game Board
		int i = 0;
		while (true) {
			BombSquare current_square = (BombSquare) board.getSquareAt(i++, 0);
			if (current_square == null) break;
		}

		// Counting the Number of Columns in the Game Board
		int j = 0;
		while (true) {
			BombSquare current_square = (BombSquare) board.getSquareAt(0, j++);
			if (current_square == null) break;
		}


		int rows = i - 1;
		int cols = j - 1;

		// Setting every BombSquare as lost
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				BombSquare current_square = (BombSquare) board.getSquareAt(row, col);
				current_square.setLost(true);
			}
		}

		System.out.println("---- You Lost -----");
	}
}