package Entry;

import myGame.myGame;
import javax.swing.JFrame;

import AppConstant.GameConstant;

public class Entry extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void run() {
		myGame game = new myGame();
		//myControlPanel controlPanel = new myControlPanel();		
		//this.setLayout(null);
		//game.setBounds(0, 0, game.getWidth(), GameConstant.FRAME_HEIGHT);
		//controlPanel.setBounds(game.getWidth(), 0, 100, GameConstant.FRAME_HEIGHT);
		this.add(game);
		//this.add(controlPanel);
		this.setLocation(GameConstant.FRAME_LOCATION_X, GameConstant.FRAME_LOCATION_Y);
		this.setSize(GameConstant.FRAME_WIDTH, GameConstant.FRAME_HEIGHT);
		this.setVisible(true);
	}

	public static void main(String[] args) {
		Entry game = new Entry();
		game.run();
	}
}
