package wwwordz.game;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Player implements Serializable {

	private static final long serialVersionUID = 1L;

	String nick;
	String password;
	int points;
	int accumulated;

	public Player(String nick, String password) {
		super();
		this.nick = nick;
		this.password = password;
		this.points = 0;
		this.accumulated = 0;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getPoints() {
		return this.points;
	}

	public void setPoints(int points) {
		this.accumulated += points;
		this.points = points;
	}

	public int getAccumulated() {
		return this.accumulated;
	}

	public void setAccumulated(int accumulated) {
		this.accumulated = accumulated;
	}

	/*
	 * Serialization
	 */
	private void writeObject(ObjectOutputStream serializer) {
		try {
			serializer.defaultWriteObject();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void readObject(ObjectInputStream deserializer) {
		try {
			deserializer.defaultReadObject();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
