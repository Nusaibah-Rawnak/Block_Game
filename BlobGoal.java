import java.awt.Color;

public class BlobGoal extends Goal {

	public BlobGoal(Color c) {
		super(c);
	}

	// method that calculates the blob goal score
	@Override
	public int score(Block board) {
		/*
		 * ADD YOUR CODE HERE
		 */

		// we flatten the board into a 2D structure
		Color[][] unitCells = board.flatten();
		// we initialize a 2D boolean array to keep track of the cells visited
		boolean[][] visited = new boolean[unitCells.length][unitCells[0].length];

		// we initialize a variable maxBlobSize that stores the maximum size of blob
		int maxBlobSize = 0;
		// we iterate through all the cells in the block
		for (int i = 0; i < unitCells.length; i++) {
			for (int j = 0; j < unitCells[i].length; j++) {
				// we check if the cell has not been visited and matches the target color
				if (!visited[i][j] && unitCells[i][j].equals(targetGoal)) {
					// we determine the size of the blob if the above conditions hold true for the cell
					int blobSize = undiscoveredBlobSize(i, j, unitCells, visited);
					// we update the maxBlobSize if a larger blob is found
					maxBlobSize = Math.max(blobSize, maxBlobSize);
				}
			}
		}
		// we return the maximum blob size
		return maxBlobSize;
	}

	@Override
	public String description() {
		return "Create the largest connected blob of " + GameColors.colorToString(targetGoal)
				+ " blocks, anywhere within the block";
	}

	// recursive helper method that helps calculate size of the target color blob
	public int undiscoveredBlobSize(int i, int j, Color[][] unitCells, boolean[][] visited) {
		/*
		 * ADD YOUR CODE HERE
		 */
		// we check if the cell is out of bound, if it has been visited already, and if it does not match the target color
		if (i < 0 || i >= unitCells.length || j < 0 || j >= unitCells[0].length || visited[i][j] || !unitCells[i][j].equals(targetGoal)) {
			// we return 0 if any of the above conditions hold true
			return 0;
		}

		// we make sure the same cell is not counted twice
		visited[i][j] = true;

		// we keep count of the blob size with this, starting with 1 for the current cell
		int blobSize = 1;

		// we use recursion to calculate the size of the blob
		// we take into account all 4 directions
		blobSize += undiscoveredBlobSize(i - 1, j, unitCells, visited);
		blobSize += undiscoveredBlobSize(i + 1, j, unitCells, visited);
		blobSize += undiscoveredBlobSize(i, j - 1, unitCells, visited);
		blobSize += undiscoveredBlobSize(i, j + 1, unitCells, visited);

		// we return the total blob size
		return blobSize;
	}

}



