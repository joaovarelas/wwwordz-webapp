package wwwordz.server;

import java.util.List;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import wwwordz.client.ManagerService;
import wwwordz.game.Manager;
import wwwordz.shared.Puzzle;
import wwwordz.shared.Rank;
import wwwordz.shared.WWWordzException;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class ManagerServiceImpl extends RemoteServiceServlet implements ManagerService {

	Manager manager = Manager.getInstance();

	@Override
	public long timeToNextPlay() throws WWWordzException {
		return manager.timeToNextPlay();
	}

	@Override
	public long register(String nick, String password) throws WWWordzException {
		return manager.register(nick, password);
	}

	@Override
	public Puzzle getPuzzle() throws WWWordzException {
		return manager.getPuzzle();
	}

	@Override
	public void setPoints(String nick, int points) throws WWWordzException {
		manager.setPoints(nick, points);
	}

	@Override
	public List<Rank> getRanking() throws WWWordzException {
		return manager.getRanking();
	}

}
