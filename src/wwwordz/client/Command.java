package wwwordz.client;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.Timer;

import wwwordz.client.GameState.State;
import wwwordz.shared.Puzzle;
import wwwordz.shared.Puzzle.Solution;

/**
 * Concrete commands to map each call type to exec. respective cmd.
 * 
 * @author vrls
 *
 */
public class Command {

	final private ClientManager manager;
	final private GameState gameState;
	final private Callback callBack;
	final private UI gameUI;

	private Map<CallbackType, Cmd> cmdMap;

	Command() {
		manager = ClientManager.getInstance();
		gameState = GameState.getInstance();
		callBack = Callback.getInstance();
		gameUI = UI.getInstance();

		/* Map call back type to a Concrete Command */
		cmdMap = new HashMap<>();
		cmdMap.put(CallbackType.FIRST, new FirstCmd());
		cmdMap.put(CallbackType.TIME, new TimeCmd());
		cmdMap.put(CallbackType.REGISTER, new RegisterCmd());
		cmdMap.put(CallbackType.PUZZLE, new PuzzleCmd());
		cmdMap.put(CallbackType.POINTS, new PointsCmd());
		cmdMap.put(CallbackType.RANKING, new RankingCmd());
	}

	static Command instance = null;

	/**
	 * Singleton
	 * 
	 * @return instance
	 */
	public static Command getInstance() {
		if (instance == null)
			instance = new Command();

		return instance;
	}

	/**
	 * Execute respective command given call type
	 * 
	 * @param type
	 */
	public void execute(CallbackType type) {
		cmdMap.get(type).execute();
	}

	/**
	 * First callback. Performed only once in beginning (retry if fail).
	 * 
	 * @author vrls
	 *
	 */
	public class FirstCmd implements Cmd {

		@Override
		public void execute() {
			if (gameState.getTimeToNextPlay() != -1) {
				manager.startTimer();

			} else {
				gameUI.openDialogBox("Error receiving first time from server. Retrying...");

				/* Retry */
				Timer retryDelay = new Timer() {

					@Override
					public void run() {
						callBack.firstCall = true;
						callBack.timeToNextPlay();
					}
				};
				retryDelay.schedule(ClientManager.delta);

			}
		}

	}

	/**
	 * Called every clock tick.
	 * 
	 * @author vrls
	 *
	 */
	public class TimeCmd implements Cmd {

		@Override
		public void execute() {

			if (gameState.getTimeToNextPlay() != -1) {
				/* Update Current State */
				gameState.updateState();
				gameUI.updateTimeLabelText();

				/* Store & use in further operations */
				State curState = gameState.getCurState();

				/* Enable/disable register button considering join stage */
				if (curState != State.JOIN) {
					gameUI.disableRegister();
				} else {
					gameUI.enableRegister();
				}

			} else {

				gameUI.openDialogBox("Error receiving time from server. Retrying...");
			}
		}

	}

	/**
	 * Handle user register.
	 * 
	 * @author vrls
	 *
	 */
	public class RegisterCmd implements Cmd {

		@Override
		public void execute() {

			if (gameState.getLoginTime() != null) {
				/* Reset puzzle */
				gameState.setPuzzle(null);
				gameUI.resetPuzzleGrid();

				/* Hide notification box for already registered player */
				if (!gameState.getNick().isEmpty() && !gameState.getPass().isEmpty())
					return;

				/* Save player credentials */
				gameState.setNick(gameUI.getUserBox().getText());
				gameState.setPass(gameUI.getPassBox().getText());
				gameUI.disableRegister();
				gameUI.openDialogBox("Registered successfully!");

			} else {
				/* Reset register button to enable again */
				gameUI.enableRegister();
				gameUI.openDialogBox("Error registering. Try again.");
			}
		}

	}

	/**
	 * Receive new puzzle every Play stage.
	 * 
	 * @author vrls
	 *
	 */
	@SuppressWarnings("unused")
	public class PuzzleCmd implements Cmd {

		@Override
		public void execute() {
			/* Get new puzzle */
			Puzzle puzzle = gameState.getPuzzle();

			/* DEBUG: log solutions in developers console */
			for (Solution s : puzzle.getSolutions())
				ClientManager.consoleLog(s.getWord());

			ClientManager.consoleLog("Total solutions: " + puzzle.getSolutions().size());

			if (puzzle != null) {
				/* Reset points of previous round */
				gameState.setPointsSent(false);
				gameUI.resetReportGrid();

				/* New instance of GamePlay */
				manager.setPlay(new Play(puzzle));
				gameUI.setPuzzleGrid(puzzle);
				gameUI.playLabel.setText("Click to select, double click to confirm.");

				gameState.displayCurPanel();

			} else {
				gameUI.openDialogBox("Error receiving new puzzle from server. Retrying...");

				/* Retry */
				Timer retryDelay = new Timer() {
					@Override
					public void run() {
						if (gameState.getCurState() == State.PLAY) {
							callBack.getPuzzle();
						}
					}
				};
				retryDelay.schedule(ClientManager.delta);

			}
		}

	}

	/**
	 * Points sent to server after Play stage.
	 * 
	 * @author vrls
	 *
	 */
	public class PointsCmd implements Cmd {

		@Override
		public void execute() {

			if (gameState.isPointsSent()) {
				/* Reset ranking table */
				gameState.setRankingList(null);

				/* Update report table */
				final Play play = manager.getPlay();
				gameUI.updateReportGrid(play.getPuzzle().getSolutions(), play.getPlayerSolutions(), play.getPoints());
				gameState.displayCurPanel();
				// gameUI.openDialogBox("Points sent to server!");

			} else {
				gameUI.openDialogBox("Error reporting points to server. Retrying...");

				/* Retry */
				Timer retryDelay = new Timer() {

					@Override
					public void run() {
						if (gameState.getCurState() == State.REPORT) {
							callBack.setPoints();
						}
					}
				};
				retryDelay.schedule(ClientManager.delta);

			}
		}

	}

	/**
	 * Display ranking of current players that solved the same puzzle.
	 * 
	 * @author vrls
	 *
	 */
	public class RankingCmd implements Cmd {

		@Override
		public void execute() {

			if (gameState.getRankingList() != null) {
				/* Require new register for next round to play */
				gameState.setRoundPlayer(false);

				/* Update ranking table */
				gameUI.updateRankingGrid(gameState.getRankingList());
				gameState.displayCurPanel();

			} else {
				gameUI.openDialogBox("Error getting ranking list from server. Retrying...");

				/* Retry */
				Timer retryDelay = new Timer() {

					@Override
					public void run() {
						if (gameState.getCurState() == State.RANKING) {
							callBack.getRanking();
						}
					}
				};
				retryDelay.schedule(ClientManager.delta);

			}

		}

	}

}
