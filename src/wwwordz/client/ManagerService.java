package wwwordz.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import wwwordz.shared.Puzzle;
import wwwordz.shared.Rank;
import wwwordz.shared.WWWordzException;

/**
 * Synchronous interface.
 * 
 * @author vrls
 *
 */
@RemoteServiceRelativePath("game")
public interface ManagerService extends RemoteService {

	long timeToNextPlay() throws WWWordzException;

	long register(String nick, String password) throws WWWordzException;

	Puzzle getPuzzle() throws WWWordzException;

	void setPoints(String nick, int points) throws WWWordzException;

	List<Rank> getRanking() throws WWWordzException;

}