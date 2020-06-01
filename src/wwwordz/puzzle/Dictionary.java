package wwwordz.puzzle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.Normalizer;
import java.util.Locale;

import wwwordz.puzzle.Trie.Search;
import wwwordz.shared.WWWordzException;

public class Dictionary {

	private static final String FILEPATH = "pt-PT-AO.dic";

	/*
	 * Generated Singleton Pattern from Template
	 */
	private static Dictionary instance;
	Trie trie;

	private Dictionary() throws WWWordzException {
		trie = new Trie();
		// Scanner scanner;

		try {

			// scanner = new Scanner(file);
			InputStream istream;
			BufferedReader reader;
			try {
				istream = ClassLoader.getSystemResourceAsStream(FILEPATH);
				reader = new BufferedReader(new InputStreamReader(istream));
			} catch (Exception e) {
				throw new WWWordzException("Error reading dictionary file");
			}

			String line = reader.readLine();
			while ((line = reader.readLine()) != null) {

				line = line.split("\\s+")[0].split("/")[0];

				line = Normalizer.normalize(line.toUpperCase(Locale.ENGLISH), Normalizer.Form.NFD)
						.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "").replaceAll("[^A-Z]", "");

				if (line.length() >= 3)
					trie.put(line);
			}

			reader.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static Dictionary getInstance() {
		if (instance == null) {
			try {
				instance = new Dictionary();
			} catch (WWWordzException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return instance;
	}

	public String getRandomLargeWord() {
		return trie.getRandomLargeWord();
	}

	public Search startSearch() {
		return trie.startSearch();
	}

	public Search continueSearch(Search cont) {
		return trie.continueSearch(cont);
	}

}
