package wwwordz.puzzle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Stack;

public class Trie implements Iterable<String> {

	public static Node root;
	public int numNodes;

	boolean validNode(Node node) {
		return node != null;
	}

	/*
	 * Composite
	 */
	class Node {

		Node parent;
		Map<Character, Node> children;
		boolean isWord;
		String word;

		Node(Node parent) {
			this.parent = parent;
			this.children = new HashMap<>();
			this.isWord = false;
			this.word = "";
		}

		void insert(char ch, Node newNode) {
			if (!children.containsKey(ch))
				children.put(ch, newNode);

			return;
		}

		Node get(char ch) {
			return children.get(ch);
		}

		List<Character> getNodeLetters() {
			List<Character> charList = new ArrayList<>();

			for (char c : this.children.keySet())
				charList.add(c);

			return charList;
		}

	}

	public Search startSearch() {
		return new Search(root);
	}

	public Search continueSearch(Search cont) {
		return new Search(cont);
	}

	public class Search {

		private Node node;

		Search(Node node) {
			this.node = node;
		}

		Search(Search search) {
			this.node = search.node;
		}

		boolean continueWith(char letter) {
			if (!validNode(node))
				return false;
			if (node.get(letter) == null)
				return false;

			node = node.get(letter);
			return true;
		}

		boolean isWord() {
			if (validNode(node))
				return node.isWord;
			else
				return false;
		}

	}

	/*
	 * Iterator
	 */

	@Override
	public Iterator<String> iterator() {
		return new NodeIterator();
	}

	class NodeIterator implements Iterator<String>, Runnable {

		Thread thread;
		boolean terminated;
		String nextWord;
		Stack<Node> stack = new Stack<>();
		Node node = root;

		public NodeIterator() {
			wordDFS(root);
		}

		@Override
		public void run() {
			// TODO: Concurrency
		}

		@Override
		public String next() {
			Node cur = stack.pop();

			wordDFS(cur);

			return cur.word;
		}

		public void wordDFS(Node start) {

			for (Node child : start.children.values()) {

				if (!child.isWord)
					wordDFS(child);
				else
					stack.push(child);

			}

			return;
		}

		@Override
		public boolean hasNext() {
			return !stack.isEmpty();
		}

		public String current() {
			return node.word;
		}

	}

	public Trie() {
		root = new Node(null);
		numNodes++;
	}

	public void put(String word) {
		Node node = root;
		Node next = null;

		String chars = "";
		for (char letter : word.toCharArray()) {
			chars += letter;
			next = node.get(letter);

			if (next == null) {
				next = new Node(node);
				numNodes++;
			}

			next.word = chars;
			node.insert(letter, next);
			node = next;
		}

		node.isWord = true;
	}

	public String getRandomLargeWord() {

		String word;
		do {
			word = "";
			Search search = new Search(Trie.root);
			boolean canContinue = true;

			while (canContinue) {

				List<Character> nodeLetters = search.node.getNodeLetters();
				int size = nodeLetters.size();

				if (size < 1)
					break;

				Random random = new Random();
				char randLetter = nodeLetters.get(random.nextInt(size));

				word += randLetter;

				if (!search.continueWith(randLetter))
					canContinue = false;

			}

		} while (word.length() < 3 || word.length() > 16);

		return word;
	}

}
