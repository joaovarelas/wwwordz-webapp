package wwwordz.game;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import wwwordz.puzzle.Generator;
import wwwordz.shared.Configs;
import wwwordz.shared.Puzzle;
import wwwordz.shared.Rank;
import wwwordz.shared.WWWordzException;

public class Round implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public enum Relative implements Serializable, Comparable<Relative> {
		BEFORE, AFTER;
	}

	public enum Stage implements Serializable, Comparable<Stage> {
		JOIN, PLAY, REPORT, RANKING, END;
	}

	private static long joinStageDuration = Configs.JOIN_STAGE_DURATION;
	private static long playStageDuration = Configs.PLAY_STAGE_DURATION;
	private static long reportStageDuration = Configs.REPORT_STAGE_DURATION;
	private static long rankingStageDuration = Configs.RANKING_STAGE_DURATION;

	Date join = now();
	Date play = new Date(join.getTime() + getJoinStageDuration());
	Date report = new Date(play.getTime() + getPlayStageDuration());
	Date ranking = new Date(report.getTime() + getReportStageDuration());
	Date end = new Date(ranking.getTime() + getRankingStageDuration());

	Generator gen = new Generator();
	Players players = Players.getInstance();
	Puzzle puzzle = gen.generate();

	Map<String, Player> roundPlayers = new HashMap<>();
	List<Rank> rankingList;

	public Round() {

	}

	public static long getRoundDuration() {
		return getJoinStageDuration() + getPlayStageDuration() + getReportStageDuration() + getRankingStageDuration();

	}

	/*
	 * Comparable
	 */
	public Stage currentStage() {

		if (now().compareTo(play) < 0)
			return Stage.JOIN;
		else if (now().compareTo(report) < 0)
			return Stage.PLAY;
		else if (now().compareTo(ranking) < 0)
			return Stage.REPORT;
		else if (now().compareTo(end) < 0)
			return Stage.RANKING;
		else
			return Stage.END;

	}

	public long getTimetoNextPlay() {

		if (currentStage() == Stage.JOIN)
			return play.getTime() - now().getTime();

		long nextPlay = end.getTime() - now().getTime() + getJoinStageDuration();

		return nextPlay;
	}

	private Date now() {
		return new Date();
	}

	public long register(String nick, String password) throws WWWordzException {

		if (currentStage() != Stage.JOIN)
			throw new WWWordzException("Cant register after game started");

		if (!players.verify(nick, password))
			throw new WWWordzException("Player cant verify in Round.register()");

		Player player = players.getPlayer(nick);
		roundPlayers.put(nick, player);

		// return play.getTime() - now().getTime();
		return getTimetoNextPlay();
	}

	public Puzzle getPuzzle() throws WWWordzException {

		if (currentStage() != Stage.PLAY)
			throw new WWWordzException("Cant get puzzle outside play stage");

		return puzzle;

	}

	public void setPoints(String nick, int points) throws WWWordzException {

		if (currentStage() != Stage.REPORT)
			throw new WWWordzException("Cant set points outside report stage");

		Player player = players.getPlayer(nick);
		player.setPoints(points);
		return;
	}

	public List<Rank> getRanking() throws WWWordzException {

		if (currentStage() != Stage.RANKING)
			throw new WWWordzException("Cant get ranking outside ranking stage");

		if (rankingList != null)
			return rankingList;

		rankingList = new LinkedList<>();
		Map<String, Integer> playerPoints = new HashMap<>();
		Iterator<String> it = roundPlayers.keySet().iterator();

		while (it.hasNext()) {
			String nick = it.next();
			Player player = players.getPlayer(nick);
			playerPoints.put(nick, player.getPoints());
		}

		while (!playerPoints.isEmpty()) {

			Map.Entry<String, Integer> maxPlayer = Collections.max(playerPoints.entrySet(),
					Comparator.comparingInt(Map.Entry::getValue));

			String nick = maxPlayer.getKey();
			int points = maxPlayer.getValue();
			int accumulated = roundPlayers.get(nick).getAccumulated();

			rankingList.add(new Rank(nick, points, accumulated));
			playerPoints.remove(nick);
		}

		return rankingList;
	}

	/*
	 * Getters & Setters
	 */

	public static long getJoinStageDuration() {
		return joinStageDuration;
	}

	public static void setJoinStageDuration(long joinStageDuration) {
		Round.joinStageDuration = joinStageDuration;
	}

	public static long getPlayStageDuration() {
		return playStageDuration;
	}

	public static void setPlayStageDuration(long playStageDuration) {
		Round.playStageDuration = playStageDuration;
	}

	public static long getReportStageDuration() {
		return reportStageDuration;
	}

	public static void setReportStageDuration(long reportStageDuration) {
		Round.reportStageDuration = reportStageDuration;
	}

	public static long getRankingStageDuration() {
		return rankingStageDuration;
	}

	public static void setRankingStageDuration(long rankingStageDuration) {
		Round.rankingStageDuration = rankingStageDuration;
	}

}
