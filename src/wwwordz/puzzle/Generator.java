package wwwordz.puzzle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import wwwordz.shared.Puzzle;
import wwwordz.shared.Puzzle.Solution;
import wwwordz.shared.Table;
import wwwordz.shared.Table.Cell;
import wwwordz.puzzle.Trie.Search;

public class Generator {

	Dictionary dic;

	public Generator() {
		dic = Dictionary.getInstance();
	}

	public Puzzle generate() {
		Puzzle puzzle = new Puzzle();
		String largeWord = dic.getRandomLargeWord();

		Random rand = new Random();

		// Random between 1 and 4 (table limits)
		int randRow = rand.nextInt(4) + 1;
		int randCol = rand.nextInt(4) + 1;

		Table table = puzzle.getTable();
		Cell randCell = table.getCell(randRow, randCol);
		char firstLetter = largeWord.charAt(0);

		randCell.setLetter(firstLetter);

		for (char letter : largeWord.substring(1).toCharArray()) {

			List<Cell> neighbours = new ArrayList<Table.Cell>(table.getNeighbors(randCell));

			Collections.shuffle(neighbours);

			for (Cell c : neighbours) {
				if (!c.isEmpty())
					continue;

				c.setLetter(letter);
				randCell = c;
				break;
			}
		}

		for (int row = 1; row <= 4; row++) {
			for (int column = 1; column <= 4; column++) {

				Cell cell = table.getCell(row, column);
				if (!cell.isEmpty())
					continue;

				char randChar = (char) (rand.nextInt(26) + 'A');
				cell.setLetter(randChar);

			}
		}

		puzzle.setSolutions(getSolutions(table));
		return puzzle;
	}

	public Puzzle random() {

		String abc = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		Random random = new Random();
		Table table;
		String[] data = new String[4];
		char[] buffer = new char[4];

		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < buffer.length; j++) {
				char randLetter = abc.charAt(random.nextInt(abc.length()));
				buffer[j] = randLetter;
			}
			data[i] = new String(buffer);
		}
		table = new Table(data);
		Puzzle puzzle = new Puzzle();
		puzzle.setTable(table);
		puzzle.setSolutions(getSolutions(table));
		return puzzle;
	}

	Set<Map<String, List<Cell>>> solutions = new HashSet<>();

	public List<Puzzle.Solution> getSolutions(Table table) {

		List<Solution> solutionList = new LinkedList<Solution>();

		for (int row = 1; row <= 4; row++) {
			for (int column = 1; column <= 4; column++) {

				Search search = dic.startSearch();
				Cell cell = table.getCell(row, column);
				String str = "";
				Set<Cell> visited = new HashSet<Cell>();

				wordsList(table, cell, str, search, visited);

			}
		}

		for (Map<String, List<Cell>> map : solutions) {
			for (String word : map.keySet()) {
				solutionList.add(new Solution(word, map.get(word)));
			}
		}

		return solutionList;
	}

	public void wordsList(Table table, Cell cell, String path, Search search, Set<Cell> visited) {

		if (visited.contains(cell))
			return;

		visited.add(cell);

		if (!search.continueWith(cell.getLetter()))
			return;

		String word = new String(path + cell.getLetter());

		if (word.length() >= 3 && search.isWord()) {
			Map<String, List<Cell>> map = new HashMap<String, List<Cell>>();
			map.put(word, new LinkedList<>(visited));

			boolean flag = false;
			for (Map<String, List<Cell>> m : solutions) {
				if (m.containsKey(word)) {
					flag = true;
				}
			}

			if (!flag) {
				solutions.add(map);
			}

		}

		for (Cell n : table.getNeighbors(cell)) {
			Set<Cell> newVisited = new HashSet<>(visited);
			Search newSearch = dic.continueSearch(search);
			wordsList(table, n, word, newSearch, newVisited);
		}

		return;
	}

}
