package wwwordz.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import wwwordz.shared.Puzzle;
import wwwordz.shared.Rank;
import wwwordz.shared.WWWordzException;

enum CallbackType {
	FIRST, TIME, REGISTER, PUZZLE, POINTS, RANKING
};

/**
 * Handle asynchronous call backs and notify observers.
 * 
 * @author vrls
 *
 */
public class Callback extends Observable {

	private ManagerServiceAsync service;
	private GameState gameState = GameState.getInstance();
	boolean firstCall = true;

	Callback() {
		observers = new ArrayList<>();
	}

	public static Callback instance;

	/**
	 * Singleton
	 * 
	 * @return instance
	 */
	public static Callback getInstance() {
		if (instance == null)
			instance = new Callback();

		return instance;
	}

	public void setService(ManagerServiceAsync service) {
		this.service = service;
	}

	/**
	 * Update time to next play stage.
	 */
	public void timeToNextPlay() {

		final CallbackType type = firstCall ? CallbackType.FIRST : CallbackType.TIME;
		firstCall = false;

		try {
			service.timeToNextPlay(new AsyncCallback<Long>() {

				@Override
				public void onSuccess(Long result) {
					gameState.setTimeToNextPlay(result);
					notifyObservers(type);
				}

				@Override
				public void onFailure(Throwable caught) {
					gameState.setTimeToNextPlay(-1);
					notifyObservers(type);
				}

			});

		} catch (WWWordzException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Send player registration during join stage and receive response.
	 * 
	 * @param username User nick
	 * @param password User pass
	 */
	public void register(String username, String password) {
		try {
			service.register(username, password, new AsyncCallback<Long>() {

				@Override
				public void onSuccess(Long result) {
					gameState.setLoginTime(new Date());
					gameState.setRoundPlayer(true);
					notifyObservers(CallbackType.REGISTER);
				}

				@Override
				public void onFailure(Throwable caught) {
					gameState.setLoginTime(null);
					gameState.setRoundPlayer(false);
					notifyObservers(CallbackType.REGISTER);
				}

			});

		} catch (WWWordzException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Update the puzzle each new round.
	 */
	public void getPuzzle() {
		try {
			service.getPuzzle(new AsyncCallback<Puzzle>() {

				@Override
				public void onSuccess(Puzzle result) {
					gameState.setPuzzle(result);
					notifyObservers(CallbackType.PUZZLE);
				}

				@Override
				public void onFailure(Throwable caught) {
					gameState.setPuzzle(null);
					notifyObservers(CallbackType.PUZZLE);
				}

			});

		} catch (WWWordzException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Send player points to server.
	 */
	public void setPoints() {
		final Play gamePlay = ClientManager.getInstance().getPlay();
		try {
			service.setPoints(gameState.getNick(), gamePlay.getPoints(), new AsyncCallback<Void>() {

				@Override
				public void onSuccess(Void result) {
					gameState.setPointsSent(true);
					notifyObservers(CallbackType.POINTS);
				}

				@Override
				public void onFailure(Throwable caught) {
					gameState.setPointsSent(false);
					notifyObservers(CallbackType.POINTS);
				}

			});

		} catch (WWWordzException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Get ranking at the final of the round.
	 */
	public void getRanking() {

		try {
			service.getRanking(new AsyncCallback<List<Rank>>() {

				@Override
				public void onSuccess(List<Rank> result) {
					gameState.setRankingList(result);
					notifyObservers(CallbackType.RANKING);
				}

				@Override
				public void onFailure(Throwable caught) {
					gameState.setRankingList(null);
					notifyObservers(CallbackType.RANKING);
				}

			});

		} catch (WWWordzException e) {
			e.printStackTrace();
		}

	}

}
