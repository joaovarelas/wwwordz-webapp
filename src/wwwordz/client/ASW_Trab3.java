package wwwordz.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;

import wwwordz.client.GameState.State;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ASW_Trab3 implements EntryPoint {

	/**
	 * Create a remote service proxy to talk to the server-side Manager service.
	 */

	private final ManagerServiceAsync managerService = GWT.create(ManagerService.class);

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {

		/* Initialize objects */
		ClientManager clientManager = ClientManager.getInstance();
		GameState gameState = GameState.getInstance();
		Callback gameCallback = Callback.getInstance();
		UI gameUI = UI.getInstance();

		/* Set manager service to perform RPC calls */
		gameCallback.setService(managerService);

		/* Setup deck layout & panels */
		gameUI.setupUI(RootPanel.get("content"), RootPanel.get("time"));

		/* Transitions between game stages */
		gameState.setDeckLayout(gameUI.deckLayout);

		/* Display register panel first */
		gameState.setCurState(State.JOIN);
		gameState.displayCurPanel();

		/* Start the game */
		clientManager.startGame();
	}

}
