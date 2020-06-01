package wwwordz.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DeckLayoutPanel;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import wwwordz.client.GameState.State;
import wwwordz.shared.Configs;
import wwwordz.shared.Puzzle;
import wwwordz.shared.Puzzle.Solution;
import wwwordz.shared.Rank;

/**
 * Wrap position X Y of cells
 * 
 * @author vrls
 *
 */
class Pos {
	public final int x;
	public final int y;

	public Pos(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public boolean equals(Pos pos) {
		return this.x == pos.x && this.y == pos.y;
	}
}

/**
 * Build and draw game graphical interface.
 * 
 * @author vrls
 *
 */
public class UI {

	DeckLayoutPanel deckLayout;

	Grid joinPanel;
	VerticalPanel playPanel;
	VerticalPanel reportPanel;
	VerticalPanel rankingPanel;

	private static DialogBox dialogBox;
	private static HTML dialogText;

	Label timeLabel;
	Label playLabel;

	final TextBox userBox = new TextBox();
	final PasswordTextBox passBox = new PasswordTextBox();
	final Button registerBtn = new Button();

	private List<Panel> listPanels = new ArrayList<Panel>();
	private Map<ToggleButton, Pos> buttonMap = new HashMap<>();

	public UI() {

	}

	static UI instance;

	/**
	 * Singleton
	 * 
	 * @return instance
	 */
	public static UI getInstance() {
		if (instance == null)
			instance = new UI();

		return instance;
	}

	/**
	 * Compose all panels and children.
	 * 
	 * @param mainContainer
	 * @param timeContainer
	 */
	public void setupUI(Panel mainContainer, Panel timeContainer) {
		setDeckLayout();
		setJoinPanel();
		setPlayPanel();
		setReportPanel();
		setRankingPanel();
		setupDialogBox();

		/* Add panels to deck */
		for (Panel p : listPanels)
			deckLayout.add(p);

		/* Add deck layout to main container */
		mainContainer.add(deckLayout);

		timeLabel = new Label();

		/* Add timer */
		timeContainer.add(timeLabel);
	}

	/**
	 * Display widget given index reference.
	 * 
	 * @param idx
	 */
	public void displayPanel(int idx) {
		deckLayout.showWidget(idx);
	}

	/**
	 * Display widget given widget.
	 * 
	 * @param w
	 */
	public void displayPanel(Widget w) {
		deckLayout.showWidget(w);
	}

	/**
	 * Setup main panel layout to store game panels.
	 */
	private void setDeckLayout() {
		deckLayout = new DeckLayoutPanel();
		deckLayout.setSize("500px", "500px");
		deckLayout.setStyleName("border");
	}

	/**
	 * Join panel containing registration form.
	 */
	private void setJoinPanel() {

		joinPanel = new Grid(3, 2);
		joinPanel.setWidget(0, 0, new HTML("Nickname: "));
		joinPanel.setWidget(0, 1, userBox);
		joinPanel.setWidget(1, 0, new HTML("Password: "));
		joinPanel.setWidget(1, 1, passBox);
		joinPanel.setWidget(2, 1, registerBtn);
		joinPanel.setStyleName("center-panel");

		final Callback gameCallback = Callback.getInstance();

		ClickHandler clickHandler = new ClickHandler() {
			public void onClick(ClickEvent event) {
				handleRegister(gameCallback);
			}

		};

		KeyUpHandler keyHandler = new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					handleRegister(gameCallback);
				}
			}
		};
		registerBtn.addClickHandler(clickHandler);
		userBox.addKeyUpHandler(keyHandler);
		passBox.addKeyUpHandler(keyHandler);

		listPanels.add(joinPanel);
	}

	private void handleRegister(final Callback gameCallback) {
		/* Prevent code injection from user input */
		String username = escapeHTML(userBox.getText());
		String password = escapeHTML(passBox.getText());
		if (username.length() < 4 || password.length() < 4) {
			openDialogBox("Username and password must be at least 4 in length.");
			return;
		}
		gameCallback.register(username, password);
	}

	/**
	 * Enable/disable register button.
	 */
	public void enableRegister() {
		registerBtn.setEnabled(true);
		registerBtn.setText("Join now!");
	}

	public void disableRegister() {
		registerBtn.setEnabled(false);
		registerBtn.setText("Please wait for next round...");
	}

	/**
	 * Play panel containing puzzle grid.
	 */
	private void setPlayPanel() {
		playPanel = new VerticalPanel();
		playPanel.setWidth("100%");
		playPanel.setHeight("100%");
		playPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		playPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

		Grid playGrid = new Grid(5, 5);

		for (int row = 1; row <= 4; row++) {
			for (int column = 1; column <= 4; column++) {
				ToggleButton newBtn = new ToggleButton(" ", " ");
				newBtn.setStyleName("gwt-ToggleButton-up");
				newBtn.setSize("50px", "50px");
				newBtn.addClickHandler(playBtnHandler());
				playGrid.setWidget(row, column, newBtn);
				/* Hack: map buttons to correspondent position */
				buttonMap.put(newBtn, new Pos(row, column));
			}
		}

		playLabel = new Label();
		playPanel.add(playLabel);
		playPanel.add(playGrid);
		listPanels.add(playPanel);
	}

	void resetButton(ToggleButton btn) {
		btn.setStyleName("gwt-ToggleButton-up");
		btn.setDown(false);
	}

	/**
	 * Report panel after game ends.
	 */
	private void setReportPanel() {
		reportPanel = new VerticalPanel();
		reportPanel.setWidth("100%");
		reportPanel.setHeight("100%");
		reportPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		reportPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

		Grid reportGrid = new Grid(1, 1);
		reportGrid.setWidget(0, 0, new Label(" "));
		reportPanel.add(reportGrid);
		listPanels.add(reportPanel);
	}

	/**
	 * Update report table with user play statistics
	 * 
	 * @param puzzleSols
	 * @param playerSols
	 * @param playerPoints
	 */
	void updateReportGrid(List<Solution> puzzleSols, List<Solution> playerSols, int playerPoints) {

		/* Sort by longest words first, compare by points */
		Collections.sort(puzzleSols, new Comparator<Solution>() {
			@Override
			public int compare(Solution o1, Solution o2) {
				if (o1 == null || o2 == null)
					return 0;
				return o2.getPoints() - o1.getPoints();
			}
		});

		Grid reportGrid = (Grid) reportPanel.getWidget(0);

		/* First 10 only (TODO: Maybe add vertical scroll bar) */
		int maxRows = 10;
		int rows = Math.min(puzzleSols.size(), maxRows);
		int gridRows = rows + 2;
		reportGrid.resize(gridRows, 3);

		int row = 0;
		/* Header */
		reportGrid.setWidget(row, 0, new Label("Solution"));
		reportGrid.getCellFormatter().setStyleName(row, 0, "tableCell-even");
		reportGrid.setWidget(row, 1, new Label("Points"));
		reportGrid.getCellFormatter().setStyleName(row, 1, "tableCell-even");
		reportGrid.setWidget(row, 2, new Label("Submitted?"));
		reportGrid.getCellFormatter().setStyleName(row, 2, "tableCell-even");

		for (Solution s : puzzleSols) {
			++row;
			/* Set solution word */
			reportGrid.setWidget(row, 0, new Label(s.getWord()));

			/* Points */
			reportGrid.setWidget(row, 1, new Label(String.valueOf(s.getPoints())));

			/* User submitted? Y/N */
			if (playerSols.contains(s))
				reportGrid.setWidget(row, 2, new Label(new String(Character.toChars(0x2705))));
			else
				reportGrid.setWidget(row, 2, new Label(new String(Character.toChars(0x274C))));

			formatGridCell(reportGrid, row);

			if (row >= maxRows)
				break;
		}

		++row;
		/* Ensure its within limits to prevent out-of-bounds exception */
		row = Math.min(row, reportGrid.getRowCount() - 1);
		reportGrid.setWidget(row, 2, new Label("Total points: " + playerPoints));
		reportGrid.getCellFormatter().setStyleName(row, 2, "tableCell-even");

	}

	/**
	 * Reset report grid
	 */
	public void resetReportGrid() {
		Grid reportGrid = (Grid) reportPanel.getWidget(0);
		for (int row = 0; row < reportGrid.getRowCount(); row++) {
			for (int column = 0; column < reportGrid.getColumnCount(); column++) {
				reportGrid.setWidget(row, column, new Label(""));
				reportGrid.getCellFormatter().removeStyleName(row, column, "tableCell-even");
				reportGrid.getCellFormatter().removeStyleName(row, column, "tableCell-odd");
			}
		}
	}

	/**
	 * Display player ranking of the finished game.
	 */
	private void setRankingPanel() {
		rankingPanel = new VerticalPanel();
		rankingPanel.setWidth("100%");
		rankingPanel.setHeight("100%");
		rankingPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		rankingPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

		Grid rankingGrid = new Grid(1, 1);
		rankingGrid.setWidget(0, 0, new Label(" "));
		rankingPanel.add(rankingGrid);
		listPanels.add(rankingPanel);
	}

	/**
	 * Update ranking table with given ranking list
	 * 
	 * @param rankingList
	 */
	public void updateRankingGrid(List<Rank> rankingList) {
		Grid rankingGrid = (Grid) rankingPanel.getWidget(0);
		int rows = Math.min(rankingList.size(), 10);
		rankingGrid.resize(rows + 1, 3);

		int row = 0;
		/* Header */
		rankingGrid.setWidget(row, 0, new Label("Nickname"));
		rankingGrid.getCellFormatter().setStyleName(row, 0, "tableCell-even");
		rankingGrid.setWidget(row, 1, new Label("Points"));
		rankingGrid.getCellFormatter().setStyleName(row, 1, "tableCell-even");
		rankingGrid.setWidget(row, 2, new Label("Accumulated"));
		rankingGrid.getCellFormatter().setStyleName(row, 2, "tableCell-even");

		for (Rank r : rankingList) {
			String nickStr = "";
			/* Award player medals */
			switch (row) {
			case 0:
				nickStr += new String(Character.toChars(0x1F947));
				break;
			case 1:
				nickStr += new String(Character.toChars(0x1F948));
				break;
			case 2:
				nickStr += new String(Character.toChars(0x1F949));
				break;
			}

			nickStr += " " + r.getNick();
			++row;
			rankingGrid.setWidget(row, 0, new Label(nickStr));
			rankingGrid.setWidget(row, 1, new Label(String.valueOf(r.getPoints())));
			rankingGrid.setWidget(row, 2, new Label(String.valueOf(r.getAccumulated())));
			formatGridCell(rankingGrid, row);
		}
	}

	private void formatGridCell(Grid grid, int row) {
		for (int column = 0; column < grid.getColumnCount(); column++) {
			if ((row % 2) == 0) {
				grid.getCellFormatter().setStyleName(row, column, "tableCell-even");
			} else {
				grid.getCellFormatter().setStyleName(row, column, "tableCell-odd");
			}
		}
	}

	/**
	 * Define time label content according to current state.
	 */
	public void updateTimeLabelText() {
		String newTimeStr = "";
		GameState gameState = GameState.getInstance();
		long time = gameState.getTimeToNextPlay();

		if (gameState.getCurState() == State.PLAY && gameState.isLoggedIn()) {
			long remainingSecs = (time - Configs.joinDuration() - Configs.rankingDuration() - Configs.reportDuration())
					/ 1000;
			newTimeStr = "Time remaining: " + Long.toString(remainingSecs) + "s. ";

		} else {
			newTimeStr = (time == -1) ? "Loading, please wait..."
					: "New game starting in: " + Double.toString(Math.ceil(time / 1000)) + "s. ";
		}

		newTimeStr += new String(Character.toChars(0x1F552));

		timeLabel.setText(newTimeStr);
	}

	/**
	 * Reset puzzle grid buttons state.
	 */
	public void resetPuzzleGrid() {
		for (int row = 1; row <= 4; row++) {
			for (int column = 1; column <= 4; column++) {
				ToggleButton btn = getButton(row, column);
				resetButton(btn);
			}
		}
	}

	ClickHandler playBtnHandler() {

		return new ClickHandler() {
			final private Play gamePlay = ClientManager.getInstance().getPlay();

			@Override
			public void onClick(ClickEvent event) {
				ToggleButton btn = (ToggleButton) event.getSource();
				if (btn.isDown()) {
					btn.setStyleName("button-pressed");
				} else {
					btn.setStyleName("gwt-ToggleButton-up");
				}
				gamePlay.selectCell(btn);
			}
		};

	}

	/**
	 * Update play panel every round with a new puzzle.
	 * 
	 * @param puzzle
	 */
	public void setPuzzleGrid(Puzzle puzzle) {
		buttonMap.clear();
		for (int row = 1; row <= 4; row++) {
			for (int column = 1; column <= 4; column++) {
				String letter = puzzle.getTable().getCell(row, column).toString();
				ToggleButton btn = new ToggleButton(letter, letter);

				btn.setSize("50px", "50px");
				btn.addClickHandler(playBtnHandler());

				getPuzzleGrid().setWidget(row, column, btn);
				buttonMap.put(btn, new Pos(row, column));
			}
		}

	}

	/**
	 * Getters & setters
	 */

	public Grid getPuzzleGrid() {
		return (Grid) playPanel.getWidget(1);
	}

	public ToggleButton getButton(int row, int column) {
		return ((ToggleButton) getPuzzleGrid().getWidget(row, column));
	}

	public Pos getButtonPos(ToggleButton btn) {
		return buttonMap.get(btn);
	}

	public TextBox getUserBox() {
		return userBox;
	}

	public PasswordTextBox getPassBox() {
		return passBox;
	}

	public Button getRegisterBtn() {
		return registerBtn;
	}

	public Label getPlayLabel() {
		return playLabel;
	}

	/**
	 * Sanitize user input.
	 * 
	 * @param html
	 * @return sanitized string
	 */
	public static String escapeHTML(String html) {
		if (html == null) {
			return null;
		}
		return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;");
	}

	/**
	 * Set dialog box text message and display.
	 * 
	 * @param text
	 */
	public void openDialogBox(String text) {
		dialogText.setHTML(text);
		dialogBox.center();
		return;
	}

	/**
	 * Setup dialog box style and elements.
	 */
	private void setupDialogBox() {
		dialogBox = new DialogBox();
		dialogText = new HTML();

		VerticalPanel dialogVPanel = new VerticalPanel();
		Button closeBtn = new Button("Close");

		dialogBox.setText("Remote Procedure Call");
		dialogBox.setAnimationEnabled(true);
		closeBtn.getElement().setId("closeButton");

		dialogVPanel.addStyleName("dialogVPanel");
		dialogVPanel.add(dialogText);
		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		dialogVPanel.add(closeBtn);
		dialogBox.setWidget(dialogVPanel);

		closeBtn.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogBox.hide();
			}
		});

		return;
	}

}
