//package wwwordz.client;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import com.google.gwt.event.dom.client.ClickEvent;
//import com.google.gwt.event.dom.client.ClickHandler;
//import com.google.gwt.event.dom.client.KeyCodes;
//import com.google.gwt.event.dom.client.KeyUpEvent;
//import com.google.gwt.event.dom.client.KeyUpHandler;
//import com.google.gwt.user.client.ui.Button;
//import com.google.gwt.user.client.ui.DeckLayoutPanel;
//import com.google.gwt.user.client.ui.Grid;
//import com.google.gwt.user.client.ui.HTML;
//import com.google.gwt.user.client.ui.HasHorizontalAlignment;
//import com.google.gwt.user.client.ui.HasVerticalAlignment;
//import com.google.gwt.user.client.ui.Label;
//import com.google.gwt.user.client.ui.Panel;
//import com.google.gwt.user.client.ui.PasswordTextBox;
//import com.google.gwt.user.client.ui.TextBox;
//import com.google.gwt.user.client.ui.VerticalPanel;
//
//import wwwordz.shared.Puzzle;
//
///*
// * Class responsible do draw and display game panels
// */
//public class GamePanel {
//
//	static DeckLayoutPanel deckLayout;
//
//	static Grid joinPanel;
//
//	static VerticalPanel playPanel;
//
//	static Panel reportPanel;
//
//	static Panel rankingPanel;
//
//	private static ArrayList<Panel> panelList = new ArrayList<Panel>();
//
//	static Map<Button, int[]> buttonMap = new HashMap<>();
//
//	static Label timeLabel = new Label();
//
//	protected GamePlay gamePlay = GamePlay.getInstance();
//
//	public GamePanel() {
//
//	}
//
//	/*
//	 * Singleton
//	 * 
//	 * static GamePanel instance;
//	 * 
//	 * public static GamePanel getInstance() { if (instance == null) instance = new
//	 * GamePanel();
//	 * 
//	 * return instance; }
//	 */
//
//	void setupPanels() {
//		setupDeckLayout();
//		setupJoinPanel();
//		setupPlayPanel();
//		setupReportPanel();
//		setupRankingPanel();
//	}
//
//	/*
//	 * Setup main panel layout to store game panels
//	 */
//	static void setupDeckLayout() {
//		DeckLayoutPanel deckPanel = new DeckLayoutPanel();
//		deckPanel.setSize("500px", "500px");
//		deckPanel.setStyleName("border");
//		GamePanel.deckLayout = deckPanel;
//	}
//
//	/*
//	 * Join panel containing registration form
//	 */
//	static void setupJoinPanel() {
//
//		final TextBox userBox = new TextBox();
//		final PasswordTextBox passBox = new PasswordTextBox();
//		final Button registerBtn = new Button("Register");
//
//		joinPanel = new Grid(3, 2);
//		joinPanel.setWidget(0, 0, new HTML("Nickname: "));
//		joinPanel.setWidget(0, 1, userBox);
//		joinPanel.setWidget(1, 0, new HTML("Password: "));
//		joinPanel.setWidget(1, 1, passBox);
//		joinPanel.setWidget(2, 1, registerBtn);
//
//		joinPanel.setStyleName("center-panel");
//
//		registerBtn.addClickHandler(new ClickHandler() {
//			public void onClick(ClickEvent event) {
//				registerBtn.setEnabled(false);
//				registerBtn.setText("Please wait...");
//				GameCallback.registerPlayer(userBox.getText(), passBox.getText());
//			}
//
//		});
//
//		passBox.addKeyUpHandler(new KeyUpHandler() {
//
//			@Override
//			public void onKeyUp(KeyUpEvent event) {
//				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
//					registerBtn.setEnabled(false);
//					registerBtn.setText("Please wait...");
//					GameCallback.registerPlayer(userBox.getText(), passBox.getText());
//				}
//			}
//		});
//
//		panelList.add(joinPanel);
//	}
//
//	/*
//	 * Play panel containing puzzle grid
//	 */
//	static void setupPlayPanel() {
//		playPanel = new VerticalPanel();
//
//		playPanel.setWidth("100%");
//		playPanel.setHeight("100%");
//		playPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
//		playPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
//
//		Grid playGrid = new Grid(6, 6);
//
//		ClickHandler btnClick = new ClickHandler() {
//			public void onClick(ClickEvent event) {
//				Button btn = (Button) event.getSource();
//				int[] pos = buttonMap.get(btn);
//				btn.setStyleName("button-pressed");
//				GamePlay.selectCell(pos[0], pos[1]);
//			}
//		};
//
//		for (int row = 1; row <= 4; row++) {
//			for (int column = 1; column <= 4; column++) {
//				Button newBtn = new Button(" ");
//				newBtn.setSize("50px", "50px");
//				newBtn.addClickHandler(btnClick);
//				buttonMap.put(newBtn, new int[] { row, column });
//				playGrid.setWidget(row, column, newBtn);
//			}
//		}
//
//		Label validWordLabel = new Label("Click to select, double click to confirm.");
//		playPanel.add(playGrid);
//		playPanel.add(validWordLabel);
//
//		panelList.add(playPanel);
//	}
//
//	/*
//	 * Update play panel every round with a new puzzle
//	 */
//	static void updatePuzzleGrid(Puzzle puzzle) {
//
//		Grid playGrid = (Grid) playPanel.getWidget(0);
//		for (int row = 1; row <= 4; row++) {
//			for (int column = 1; column <= 4; column++) {
//				Button btn = (Button) playGrid.getWidget(row, column);
//				String letter = puzzle.getTable().getCell(row, column).toString();
//				btn.setText(letter);
//				btn.setStyleName("gwt-Button");
//			}
//		}
//
//		Label checkWordLabel = (Label) playPanel.getWidget(1);
//		checkWordLabel.setText("Click to select, double click to confirm.");
//
//		return;
//	}
//
//	static void resetPuzzleGrid() {
//
//		Grid playGrid = (Grid) playPanel.getWidget(0);
//		for (int row = 1; row <= 4; row++) {
//			for (int column = 1; column <= 4; column++) {
//				Button btn = (Button) playGrid.getWidget(row, column);
//				btn.setStyleName("gwt-Button");
//			}
//		}
//	}
//
//	/*
//	 * Report panel after game ends
//	 */
//	static void setupReportPanel() {
//		reportPanel = new VerticalPanel();
//		reportPanel.add(new Label("reportPanel"));
//
//		panelList.add(reportPanel);
//	}
//
//	/*
//	 * Display player ranking of the finished game
//	 */
//	static void setupRankingPanel() {
//		rankingPanel = new VerticalPanel();
//		rankingPanel.add(new Label("rankingPanel"));
//
//		panelList.add(rankingPanel);
//	}
//
//	/*
//	 * Return list with game panels
//	 */
//	static List<Panel> getPanels() {
//		return panelList;
//	}
//
//	/*
//	 * Sanitize user input
//	 */
//	static String escapeHTML(String html) {
//		if (html == null) {
//			return null;
//		}
//		return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;");
//	}
//
//}
