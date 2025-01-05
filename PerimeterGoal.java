import java.awt.Color;

public class PerimeterGoal extends Goal{

	public PerimeterGoal(Color c) {
		super(c);
	}

	// method that calculates the perimeter goal score
	@Override
	public int score(Block board) {
		/*
		 * ADD YOUR CODE HERE
		 */
		// we flatten the board into a 2D structure
		Color[][] flattenedBoard = board.flatten();

		// we initialize the score variable and set it to 0
		int score = 0;
		// we initialize the index for the last row
		int lastRow = flattenedBoard.length - 1;
		// we initialize the index for the last column
		int lastColumn = flattenedBoard[0].length - 1;

		// we iterate through the rows to calculate the top and bottom row perimeter cells
		for (int column = 0; column <= lastColumn; column++) {
			// we increment score for top row perimeter cells that match the target color
			if (flattenedBoard[0][column].equals(targetGoal)) {
				// we count the corner cells twice
				if (column == 0 || column == lastColumn) {
					score += 2;
				}
				// we count the non-corner cells only once
				else {
					score += 1;
				}
			}
			// we increment score for bottom row perimeter cells that match the target color
			if (lastRow > 0 && flattenedBoard[lastRow][column].equals(targetGoal)) {
				// we count the corner cells twice
				if (column == 0 || column == lastColumn) {
					score += 2;
				}
				// we count the non-corner cells only once
				else {
					score += 1;
				}
			}

		}
		// we iterate through the columns to calculate the left and right column perimeter cells
		for (int row = 1; row < lastRow; row++) {
			// we increment score for left column perimeter cells that match the target color
			if (flattenedBoard[row][0].equals(targetGoal)) {
				score += 1;
			}
			// we increment score for right column perimeter cells that match the target color
			if (lastColumn > 0 && flattenedBoard[row][lastColumn].equals(targetGoal)) {
				score += 1;
			}
		}
		// we return the final perimeter goal score
		return score;
	}


	@Override
	public String description() {
		return "Place the highest number of " + GameColors.colorToString(targetGoal) 
		+ " unit cells along the outer perimeter of the board. Corner cell count twice toward the final score!";
	}

}
