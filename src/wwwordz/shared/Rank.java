package wwwordz.shared;

import java.io.Serializable;

public class Rank implements Serializable {

	private static final long serialVersionUID = 1L;

	String nick;
	int points;
	int accumulated;

	public Rank() {

	}

	public Rank(String nick, int points, int accumulated) {
		this.nick = nick;
		this.points = points;
		this.accumulated = accumulated;
	}

	/*
	 * Getters & Setters
	 */
	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public int getAccumulated() {
		return accumulated;
	}

	public void setAccumulated(int accumulated) {
		this.accumulated = accumulated;
	}

}
