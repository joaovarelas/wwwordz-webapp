package wwwordz.game;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import wwwordz.shared.WWWordzException;

public class Players implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final String FILENAME = "players.ser";

	/*
	 * Generated Singleton Pattern from Template
	 */
	private static Players instance;

	private Map<String, Player> playersMap = new HashMap<>();

	private Players() {

		// Read serialized data (persistent)
		/*
		 * if (new File(getHome(), FILENAME).exists()) try { readPlayers(); } catch
		 * (WWWordzException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */
	}

	public static Players getInstance() {
		if (instance == null) {
			instance = new Players();
		}
		return instance;
	}

	public static File getHome() {
		return new File(System.getProperty("user.dir"));
	}

	public static void setHome() {
		// TODO Auto-generated method stub

	}

	public boolean verify(String nick, String password) {

		if (getPlayer(nick) == null) {
			playersMap.put(nick, new Player(nick, password));
			// writePlayers(); // failing tests because I/O delay
			return true;
		}

		return getPlayer(nick).getPassword().equals(password);

	}

	public void resetPoints(String nick) throws WWWordzException {

		Player player = getPlayer(nick);

		if (player == null)
			throw new WWWordzException("Player is null at Players.resetPoints()");

		player.setAccumulated(0);
		player.setPoints(0);
		/*
		 * try { writePlayers(); } catch (WWWordzException e) { // TODO Auto-generated
		 * catch block e.printStackTrace(); }
		 */

		return;
	}

	public void addPoints(String nick, int points) throws WWWordzException {

		Player player = getPlayer(nick);

		if (player == null)
			throw new WWWordzException("Player is null at Players.addPoints()");

		player.setPoints(points);
		/*
		 * try { writePlayers(); } catch (WWWordzException e) { // TODO Auto-generated
		 * catch block e.printStackTrace(); }
		 */

		return;
	}

	public Player getPlayer(String nick) {

		return playersMap.get(nick);
	}

	/*
	 * Serialization
	 */
	@SuppressWarnings("unchecked")
	public void readPlayers() throws WWWordzException {

		FileInputStream canal;
		try {
			canal = new FileInputStream(FILENAME);
			ObjectInputStream deserialize = new ObjectInputStream(canal);
			playersMap = (HashMap<String, Player>) deserialize.readObject();
			deserialize.close();

		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return;
	}

	public void writePlayers() throws WWWordzException {

		FileOutputStream channel;
		try {
			channel = new FileOutputStream(FILENAME);
			ObjectOutputStream serialize = new ObjectOutputStream(channel);
			serialize.writeObject(playersMap);
			serialize.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return;
	}

	public void cleanup() {
		instance = null;
		playersMap.clear();
		File f = new File(getHome(), FILENAME);
		f.delete();
	}

}
