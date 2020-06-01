package wwwordz.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Table implements Serializable, Iterable<Table.Cell> {

	private static final long serialVersionUID = 1L;

	Table.Cell[][] table = new Table.Cell[6][6];
	int uniqueHash;

	public static class Cell implements Serializable {

		private static final long serialVersionUID = 1L;
		private int column;
		private int row;
		private char letter;

		Cell() {

		}

		public int getColumn() {
			return this.column;
		}

		public int getRow() {
			return this.row;
		}

		Cell(int row, int column) {
			this.row = row;
			this.column = column;
		}

		Cell(int row, int column, char letter) {
			this.row = row;
			this.column = column;
			this.letter = letter;
		}

		public char getLetter() {
			return letter;
		}

		public boolean isEmpty() {
			return letter == ' ';
		}

		public void setLetter(char letter) {
			this.letter = letter;
		}

		@Override
		public String toString() {
			return String.valueOf(this.letter);
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			// result = prime * result + getEnclosingInstance().hashCode();
			result = prime * result + column;
			result = prime * result + letter;
			result = prime * result + row;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Cell other = (Cell) obj;
			// if (!getEnclosingInstance().equals(other.getEnclosingInstance()))
			// return false;
			if (column != other.column)
				return false;
			if (letter != other.letter)
				return false;
			if (row != other.row)
				return false;
			return true;
		}

	}

	/*
	 * Iterator
	 */

	@Override
	public Iterator<Cell> iterator() {
		return new CellIterator();
	}

	private static class CellIterator extends Table implements Serializable, Iterator<Cell> {

		private static final long serialVersionUID = 1L;
		private int row;
		private int column;
		// Cell current;

		CellIterator() {
			this.row = 1;
			this.column = 1;
			// this.current = super.table[row][column];
		}

		@Override
		public boolean hasNext() {

			if (this.row <= 4 && this.column <= 4)
				return true;

			return false;
		}

		@Override
		public Cell next() {
			Cell cur = this.table[row][column];

			if (column < 4) {
				column++;
			} else {
				column = 1;
				row++;
			}

			return cur;
		}

		@Override
		public void remove() {
			return;
		}
	}

	private static String[] emptyTable = { "    ", "    ", "    ", "    " };

	public Table() {
		this(emptyTable);
	}

	public Table(String[] data) {

		for (int i = 1; i <= 4; i++)
			for (int j = 1; j <= 4; j++)
				table[i][j] = new Table.Cell(i, j, data[i - 1].charAt(j - 1));

		this.uniqueHash = this.hashCode();
	}

	public char getLetter(int row, int column) {
		return table[row][column].getLetter();

	}

	public void setLetter(int row, int column, char letter) {
		table[row][column].setLetter(letter);
	}

	public List<Table.Cell> getEmptyCells() {
		List<Table.Cell> list = new ArrayList<Table.Cell>();
		Iterator<Cell> it = new CellIterator();

		Cell next;
		while (it.hasNext()) {
			next = it.next();
			if (next.isEmpty())
				list.add(next);
		}

		return null;
	}

	public List<Table.Cell> getNeighbors(Table.Cell cell) {

		List<Table.Cell> list = new ArrayList<Table.Cell>();

		int row = cell.getRow();
		int column = cell.getColumn();

		int[] offset = { -1, 0, 1 };

		for (int i : offset) {
			for (int j : offset) {
				int x = Math.max(Math.min(row + i, 4), 1);
				int y = Math.max(Math.min(column + j, 4), 1);

				Cell neighbour = this.getCell(x, y);

				if (!cell.equals(neighbour) && !list.contains(neighbour)) {
					list.add(neighbour);
				}
			}
		}

		return list;
	}

	public Table.Cell getCell(int row, int column) {
		return this.table[row][column];
	}

	@Override
	public String toString() {
		String str = "";
		for (int i = 1; i <= 4; i++)
			for (int j = 1; j <= 4; j++)
				str += getCell(i, j).toString();

		return str;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(table);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Table other = (Table) obj;
		if (!Arrays.deepEquals(table, other.table))
			return false;
		return true;
	}

}