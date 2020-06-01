package wwwordz.shared;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class Puzzle implements Serializable {

	private static final long serialVersionUID = 1L;

	Table table = new Table();
	List<Puzzle.Solution> solutions = new LinkedList<Puzzle.Solution>();

	public Puzzle() {

	}

	public Table getTable() {
		return table;
	}

	public void setTable(Table table) {
		this.table = table;
	}

	public List<Puzzle.Solution> getSolutions() {
		return solutions;
	}

	public void setSolutions(List<Puzzle.Solution> solutions) {
		this.solutions = solutions;
	}

	public static class Solution implements Serializable {

		private static final long serialVersionUID = 1L;
		private List<Table.Cell> cells;
		private String word;

		public Solution() {

		}

		public Solution(String word, List<Table.Cell> cells) {
			this.word = word;
			this.cells = cells;
		}

		public String getWord() {
			return word;
		}

		public List<Table.Cell> getCells() {
			return cells;
		}

		public int getPoints() {
			return getPoints(word.length());
		}

		public int getPoints(int length) {
			if (length == 3)
				return 1;
			else
				return 2 * (getPoints(length - 1)) + 1;
		}

		public boolean equals(String word) {
			return this.word.equals(word);
		}

	}

	@Override
	public String toString() {
		return this.getTable().toString();
	}

}
