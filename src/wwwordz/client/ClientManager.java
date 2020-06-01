package wwwordz.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.user.client.Timer;

import wwwordz.client.GameState.State;

/**
 * Manage the game logic and behavior.
 * 
 * @author vrls
 *
 */
public class ClientManager implements Observer {

	private Callback callBack = Callback.getInstance();
	private GameState gameState = GameState.getInstance();
	private Play play;

	static final int delta = 1000;

	public ClientManager() {
		/* Add this instance to callback observers list */
		callBack.addObserver(this);
	}

	public static ClientManager instance = null;

	/**
	 * Singleton
	 * 
	 * @return instance
	 */
	public static ClientManager getInstance() {
		if (instance == null)
			instance = new ClientManager();

		return instance;
	}

	/**
	 * Entry point for client manager.
	 */
	public void startGame() {
		/* Send first call to establish connection */
		callBack.timeToNextPlay();
	}

	/**
	 * Synchronize clock with server
	 */
	public void startTimer() {
		Timer clock = new Timer() {
			@Override
			public void run() {
				/* Update clock tick */
				callBack.timeToNextPlay();

				State curState = gameState.getCurState();

				/* Only consider other stages after login */
				/* Unauthenticated users stay in register panel */
				if (gameState.isLoggedIn()) {

					/* Select call considering current state */
					switch (curState) {

					case JOIN:
						/* Register user again to include in round players list */
						if (!gameState.isRoundPlayer())
							callBack.register(gameState.getNick(), gameState.getPass());
						break;

					case PLAY:
						/* Get new puzzle every round for authenticated users */
						if (gameState.getPuzzle() == null)
							callBack.getPuzzle();
						break;

					case REPORT:
						/* Send points to server after Play stage */
						if (!gameState.isPointsSent())
							callBack.setPoints();
						break;

					case RANKING:
						/* Get ranking list after Report stage */
						if (gameState.getRankingList() == null)
							callBack.getRanking();
						break;

					default:
						break;
					}
				}
			}

		};
		clock.scheduleRepeating(delta);
	}

	/**
	 * Handle observed call backs by executing respective commands.
	 */
	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof Callback) {
			CallbackType type = (CallbackType) arg;
			Command.getInstance().execute(type);
		}
	}

	/**
	 * DEBUG: Log messages in web browser developer console.
	 * 
	 * @param msg Debug msg
	 */
	public static void consoleLog(String msg) {
		Logger.getLogger("DEBUG").log(Level.SEVERE, msg);
	}

	/**
	 * Get Play object of current play stage.
	 * 
	 * @return play instance
	 */
	public Play getPlay() {
		return play;
	}

	/**
	 * Set Play object of current play stage.
	 * 
	 * @param play instance
	 */
	public void setPlay(Play play) {
		this.play = play;
	}

}