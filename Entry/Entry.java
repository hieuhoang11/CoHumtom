package Entry;

import myGame.GamePanel;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import AppConstant.GameConstant;

public class Entry extends JFrame implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GamePanel game;
	private JPanel myControl;
	private int humanChoose = GameConstant.HUMAN_CHOOSE_TOM;
	private int level = GameConstant.MEDIUM;
	private JButton btnNewGame, btnRestart, btnMenu, btnResume ;
	private JLabel lbLevel, lbGameMode , lbUserChoosen;
	private JRadioButton btnNormal, btnMedium, btnHard, btnHvsH, btnHvsC , btnTom , btnHum;
	private String HxH = "Human vs Human";
	private String HxC = "Human vs Com";
	private String normal = "Normal";
	private String medium = "Medium";
	private String hard = "Hard";
	private int TURN;

	public Entry() {
		this.setLayout(null);
		this.setBackground(Color.WHITE);
		game = new GamePanel();
		game.setBounds(0, 0, 500, GameConstant.FRAME_HEIGHT);
		game.setTURN_PLAYER(GameConstant.GAME_PAUSE);

		myControl = new JPanel();
		myControl.setLayout(null);
		myControl.setBounds(500, 50, 200, GameConstant.FRAME_HEIGHT);

		myControl.add(lbGameMode = createLabel("Chế độ chơi", 10, 10, 90, 30));
		ButtonGroup group = new ButtonGroup();
		myControl.add(btnHvsH = createRadioButton(HxH, 50, 35, 125, 30, group));
		myControl.add(btnHvsC = createRadioButton(HxC, 50, 60, 125, 30, group, true));

		myControl.add(lbLevel = createLabel("Mức độ chơi", 10, 90, 90, 30));
		ButtonGroup group1 = new ButtonGroup();
		myControl.add(btnNormal = createRadioButton(normal, 50, 115, 70, 30, group1));
		myControl.add(btnMedium = createRadioButton(medium, 50, 140, 70, 30, group1, true));
		myControl.add(btnHard = createRadioButton(hard, 50, 165, 70, 30, group1));
		
		myControl.add(lbUserChoosen = createLabel("Người chơi chọn", 10, 195, 120, 30));
		ButtonGroup group2 = new ButtonGroup();
		myControl.add(btnTom = createRadioButton("Tom", 50, 220, 70, 30, group2, true));
		myControl.add(btnHum = createRadioButton("Hum", 50, 245, 70, 30, group2));
		

		myControl.add(btnNewGame = createButton("NewGame", 10, 330, 100, 30));
		myControl.add(btnResume = createButton("Resume", 10, 370, 100, 30));
		myControl.add(btnRestart = createButton("Restart", 10, 400, 80, 30));
		myControl.add(btnMenu = createButton("Menu", 95, 400, 80, 30));		
		disAppear(true);
		btnResume.setVisible(false);
	}

	public void run() {

		this.add(game);
		this.add(myControl);

		this.setLocation(GameConstant.FRAME_LOCATION_X, GameConstant.FRAME_LOCATION_Y);
		this.setSize(GameConstant.FRAME_WIDTH, GameConstant.FRAME_HEIGHT);
		this.setVisible(true);
	}

	public JButton createButton(String text, int x, int y, int width, int height) {
		JButton btn = new JButton(text);
		btn.setBounds(x, y, width, height);
		btn.addActionListener(this);
		return btn;
	}

	public JLabel createLabel(String text, int x, int y, int width, int height) {
		JLabel label = new JLabel(text);
		label.setBounds(x, y, width, height);
		return label;
	}

	public JRadioButton createRadioButton(String text, int x, int y, int width, int height, ButtonGroup group,
			boolean isSelected) {
		JRadioButton rbtn = new JRadioButton(text);
		rbtn.setBounds(x, y, width, height);
		rbtn.setSelected(isSelected);
		rbtn.addActionListener(this);
		group.add(rbtn);
		return rbtn;
	}

	public JRadioButton createRadioButton(String text, int x, int y, int width, int height, ButtonGroup group) {
		return createRadioButton(text, x, y, width, height, group, false);
	}

	public static void main(String[] args) {
		Entry game = new Entry();
		game.run();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			JRadioButton btn = (JRadioButton) e.getSource();
			switch (btn.getText()) {
			case "Human vs Human":
				appear(false);
				humanChoose = GameConstant.HUMAN_VS_HUMAN;
				return;
			case "Human vs Com":
				appear(true);
				if (btnTom.isSelected() == true) 
					humanChoose = GameConstant.HUMAN_CHOOSE_TOM;				
				else 
					humanChoose = GameConstant.HUMAN_CHOOSE_HUM;
				return;
			case "Normal":
				level = GameConstant.NORMAL;
				return;
			case "Medium":
				level = GameConstant.MEDIUM;
				return;
			case "Hard":
				level = GameConstant.HARD;
				return;
			case "Tom":
				humanChoose = GameConstant.HUMAN_CHOOSE_TOM;
				return;
			case "Hum":
				humanChoose = GameConstant.HUMAN_CHOOSE_HUM;
				return;
			}
		}
		catch (Exception ex) {}
		
		JButton button = (JButton) e.getSource();
		switch (button.getText()) {
		case "NewGame":
			
			game.setTURN_PLAYER(TURN);
			game.setLevel(level);
			game.setIsVsCom(humanChoose);
			disAppear(false);
			game.restart();
			game.repaint();
			break;
		case "Menu":		
			disAppear(true);
			if (game.getIsVsCom() == GameConstant.HUMAN_VS_HUMAN) {
				btnHvsH.setSelected(true);
				appear(false);
			} else {
				btnHvsC.setSelected(true);
				if (game.getLevel() == GameConstant.NORMAL) {
					btnNormal.setSelected(true);
				} else if (game.getLevel() == GameConstant.MEDIUM) {
					btnMedium.setSelected(true);
				} else if (game.getLevel() == GameConstant.HARD) {
					btnHard.setSelected(true);
				}
				if (game.getTURN_PLAYER() == GameConstant.TURN_HUM) {
					btnHum.setSelected(true);
				} else if (game.getTURN_PLAYER() == GameConstant.TURN_TOM) {
					btnTom.setSelected(true);
				}
			}
			
			TURN = game.getTURN_PLAYER() ;
			game.setTURN_PLAYER(GameConstant.GAME_PAUSE);
			break;
		case "Restart":
			game.restart();
//			if (game.getIsVsCom() == GameConstant.HUMAN_CHOOSE_HUM) {
//				TURN = GameConstant.TURN_HUM ;
//			} else {
//				TURN = GameConstant.TURN_TOM ;
//			}
			game.setTURN_PLAYER(TURN);
			disAppear(false);
			game.repaint();
			break;
		case "Resume":			
			game.setTURN_PLAYER(TURN);
			disAppear(false);			
//			if (isVsCom == GameConstant.HUMAN_VS_HUMAN) {
//				btnHvsH.setSelected(true);
//			}
			break;
		}
		
	}

	public void appear(boolean b) {
		lbLevel.setVisible(b);
		btnNormal.setVisible(b);
		btnMedium.setVisible(b);
		btnHard.setVisible(b);
		
		lbUserChoosen.setVisible(b);
		btnTom.setVisible(b);
		btnHum.setVisible(b);
	}

	public void disAppear(boolean b) {
		appear(b);
		lbGameMode.setVisible(b);
		btnHvsH.setVisible(b);
		btnHvsC.setVisible(b);
		btnNewGame.setVisible(b);
		btnResume.setVisible(b);
		btnRestart.setVisible(!b);
		btnMenu.setVisible(!b);
	}
}
