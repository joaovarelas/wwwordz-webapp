package wwwordz.game;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import wwwordz.shared.Puzzle;
import wwwordz.shared.Rank;
import wwwordz.shared.WWWordzException;

/*
 * Facade
 */
public class Manager {

	static final ScheduledExecutorService worker = Executors.newScheduledThreadPool(1);
	static final long INITIAL_TIME = 0;
	Round round = new Round();

	/*
	 * Generated Singleton Pattern from Template
	 */
	private static Manager instance;

	public static Manager getInstance() {
		if (instance == null) {
			instance = new Manager();
		}
		return instance;
	}

	public Manager() {

		worker.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				round = new Round();
			}
		}, 0, Round.getRoundDuration(), TimeUnit.MILLISECONDS);

	}

	public long timeToNextPlay() throws WWWordzException {
		return round.getTimetoNextPlay();
	}

	public long register(String nick, String password) throws WWWordzException {
		return round.register(nick, password);
	}

	public Puzzle getPuzzle() throws WWWordzException {
		return round.getPuzzle();
	}

	public void setPoints(String nick, int points) throws WWWordzException {
		round.setPoints(nick, points);
	}

	public List<Rank> getRanking() throws WWWordzException {
		return round.getRanking();
	}

}
