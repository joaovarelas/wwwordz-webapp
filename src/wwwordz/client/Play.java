package wwwordz.client;

import java.util.LinkedList;
import java.util.List;

import com.google.gwt.user.client.ui.ToggleButton;

import wwwordz.shared.Puzzle;
import wwwordz.shared.Puzzle.Solution;
import wwwordz.shared.Table;

/**
 * Perform actions given player's input on new instance per puzzle.
 */

public class Play {

	private Puzzle puzzle;
	private String word = "";
	private int accPoints = 0;

	private ToggleButton lastClickedBtn;

	LinkedList<Solution> playerSolutions = new LinkedList<>();

	private UI gameUI = UI.getInstance();

	public Play(Puzzle puzzle) {
		this.puzzle = puzzle;
	}

	/**
	 * Select respective cell when user presses letter button.
	 */
	public void selectCell(ToggleButton btn) {
		gameUI.playLabel.setText("");
		String letter = btn.getText();

		if (btn.isDown()) {

			if (word.isEmpty()) {
				word += letter;
				lastClickedBtn = btn;

			} else {
				/* Check if is a valid adjacent / neighbour */
				boolean validAdjacent = checkAdjacent(gameUI.getButtonPos(btn));

				if (validAdjacent) {
					word += letter;
					lastClickedBtn = btn;
				} else {
					gameUI.resetButton(btn);
					gameUI.getPlayLabel().setText("Only adjacent letters may be selected.");
				}

			}

		} else {
			/* Word is complete. Process word */
			if (word.length() < 3) {
				/* Not a word */
				word = "";
				gameUI.resetPuzzleGrid();
				gameUI.getPlayLabel().setText("Words must have at least 3 characters in length");
				return;
			}

			Solution sol = checkSolution();

			/* Calculate and set respective points */
			if (sol != null) {
				/* Check repeated solution */
				if (playerSolutions.contains(sol)) {
					gameUI.getPlayLabel().setText("Already submitted solution '" + sol.getWord() + "'. "
							+ new String(Character.toChars(0x2753)));

				} else {
					accPoints += sol.getPoints();
					playerSolutions.add(sol);
					gameUI.getPlayLabel().setText("Valid solution! " + new String(Character.toChars(0x2705)) + " ("
							+ sol.getPoints() + " points)");
				}
			} else {
				gameUI.getPlayLabel()
						.setText("Invalid solution. " + new String(Character.toChars(0x274C)) + " (0 points)");
			}

			word = "";
			gameUI.resetPuzzleGrid();
		}

		ClientManager.consoleLog("Current word: " + word);

	}

	public Puzzle getPuzzle() {
		return puzzle;
	}

	public void setPuzzle(Puzzle puzzle) {
		this.puzzle = puzzle;
	}

	/**
	 * Check if selected button has any adjacent already clicked.
	 */
	private boolean checkAdjacent(Pos clickedBtnPos) {
		Table table = puzzle.getTable();
		Pos lastBtnPos = gameUI.getButtonPos(lastClickedBtn);

		Table.Cell lastCell = table.getCell(lastBtnPos.x, lastBtnPos.y);
		for (Table.Cell adjCell : table.getNeighbors(lastCell)) {

			Pos adjPos = new Pos(adjCell.getRow(), adjCell.getColumn());
			if (adjPos.equals(clickedBtnPos))
				return true;

		}
		return false;
	}

	/**
	 * Triggered when user double-click and word >= 3.
	 */
	private Solution checkSolution() {
		for (Solution sol : puzzle.getSolutions()) {
			if (sol.equals(word)) {
				return sol;
			}
		}
		return null;
	}

	/**
	 * Get accumulated points of valid solutions
	 * 
	 * @return acc points
	 */
	public int getPoints() {
		return accPoints;
	}

	public void setPoints(int points) {
		this.accPoints = points;
	}

	/**
	 * Get list of player solutions submitted during Play stage
	 * 
	 * @return list of solutions
	 */
	public List<Solution> getPlayerSolutions() {
		return playerSolutions;
	}

}
