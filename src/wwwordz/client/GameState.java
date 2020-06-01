package wwwordz.client;

import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.ui.DeckLayoutPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

import wwwordz.shared.Configs;
import wwwordz.shared.Puzzle;
import wwwordz.shared.Rank;

/**
 * Class responsible to store current game state.
 * 
 * @author vrls
 *
 */
public class GameState {

	/**
	 * Enumeration of comparable game states
	 * 
	 * @author vrls
	 *
	 */
	public enum State implements Comparable<State> {
		JOIN, PLAY, REPORT, RANKING
	}

	State currentState;

	private long timeToNextPlay = -1;
	private Date loginTime = null;
	private boolean roundPlayer = false;
	private String nick = "";
	private String pass = "";
	private Puzzle puzzle = null;
	private boolean pointsSent = false;

	private List<Rank> rankingList = null;

	DeckLayoutPanel deckLayout;

	GameState() {

	}

	public static GameState instance;

	/**
	 * Singleton
	 * 
	 * @return instance
	 */
	public static GameState getInstance() {
		if (instance == null)
			instance = new GameState();

		return instance;
	}

	/**
	 * Get current stage according to timeToNextPlay.
	 * 
	 * @param time current time
	 * @return current state
	 */
	public State curStateByTime(long time) {
		if (time > 0 && time <= Configs.joinDuration())
			return State.JOIN;

		else if (time > Configs.joinDuration() && time <= Configs.joinDuration() + Configs.rankingDuration())
			return State.RANKING;

		else if (time > Configs.joinDuration() + Configs.rankingDuration()
				&& time <= Configs.joinDuration() + Configs.rankingDuration() + Configs.reportDuration())
			return State.REPORT;

		else
			return State.PLAY;
	}

	/**
	 * Update state based on current time.
	 */
	public void updateState() {
		setCurState(timeToNextPlay);
	}

	public State getCurState() {
		return currentState;
	}

	public int getCurStateIndex() {
		return currentState.ordinal();
	}

	public void setCurState(State currentState) {
		this.currentState = currentState;
	}

	public void setCurState(long time) {
		this.currentState = curStateByTime(time);
	}

	public State getNextState() {
		return State.values()[currentState.ordinal() + 1];
	}

	public void next() {
		currentState = getNextState();
	}

	public long getTimeToNextPlay() {
		return timeToNextPlay;
	}

	public void setTimeToNextPlay(long timeToNextPlay) {
		this.timeToNextPlay = timeToNextPlay;
	}

	public Date getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	public boolean isLoggedIn() {
		return loginTime != null;
	}

	public Puzzle getPuzzle() {
		return puzzle;
	}

	public void setPuzzle(Puzzle puzzle) {
		this.puzzle = puzzle;
	}

	public void setDeckLayout(DeckLayoutPanel deckLayout) {
		this.deckLayout = deckLayout;
	}

	public Panel getPanel() {
		return (Panel) deckLayout.getWidget(currentState.ordinal());
	}

	public void displayCurPanel() {
		deckLayout.showWidget(currentState.ordinal());
	}

	public void displayPanel(int idx) {
		deckLayout.showWidget(idx);
	}

	public void displayPanel(Widget w) {
		deckLayout.showWidget(w);
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public boolean isPointsSent() {
		return pointsSent;
	}

	public void setPointsSent(boolean pointsSent) {
		this.pointsSent = pointsSent;
	}

	public List<Rank> getRankingList() {
		return rankingList;
	}

	public void setRankingList(List<Rank> rankingList) {
		this.rankingList = rankingList;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public boolean isRoundPlayer() {
		return roundPlayer;
	}

	public void setRoundPlayer(boolean roundPlayer) {
		this.roundPlayer = roundPlayer;
	}

}
