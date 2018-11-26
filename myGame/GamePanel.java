package myGame;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import FileIO.*;
import ai.*;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import AppConstant.GameConstant;
import Common.*;
import Entity.*;

public class GamePanel extends JPanel implements MouseListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Point> listPointOfGameBoard;
	private List<Integer> listHum;
	private List<Integer> listTom;
	private List<Integer> listMoveable;
	private List<Integer> listCatch;
	private int chessIsChecked = -1;
	private int TURN_PLAYER = GameConstant.GAME_STOP;
	private int PLAYER_WIN = 0;
	private int isVsCom = 0;
	private int level;
	private boolean turnCom = false;
	private int count = 0;

	public GamePanel() {
		restart();
		this.addMouseListener(this);
	}

	public void paint(Graphics g) {
		if (TURN_PLAYER == GameConstant.GAME_STOP && PLAYER_WIN == 0) {
			return;
		}
		GameBoard.clearGameBoard(g);
		GameBoard.paintGameBoard(g, listPointOfGameBoard);
		GameBoard.paintChess(g, listTom, listHum, listPointOfGameBoard, chessIsChecked);
		if (chessIsChecked == -1)
			return;
		GameBoard.paintListMoveNext(g, listMoveable, listPointOfGameBoard);
		if (TURN_PLAYER == GameConstant.TURN_TOM)
			return;
		GameBoard.paintListCatch(g, listCatch, listPointOfGameBoard);
	}

	@Override
	public void mouseClicked(MouseEvent e) {

		if (TURN_PLAYER == GameConstant.GAME_STOP) {
			return;
		}

		if (isClickedOutSideTheGameBoard(e.getX(), e.getY())) {
			
			return;
		}
		switch (TURN_PLAYER) {
		case GameConstant.TURN_HUM:
			int newPosition = Move(e.getX(), e.getY(), listHum, listMoveable, GameConstant.HUM_WIDTH, GameConstant.HUM_STATUS);
			Kill(e.getX(), e.getY());
			Count (newPosition) ;
			if (count == 5) {
				PLAYER_WIN = GameConstant.TOM_WIN;
				showMessage ("Hùm thua do 5 nước liên tiếp không thoát được hình thoi\nBạn có muốn chơi lại không ?") ;
			}
			Kill();
			if (checkHumWin()) {
				showMessage ("Hùm thắng\nBạn có muốn chơi lại không ?") ;				
			}
			if (isVsCom == GameConstant.HUMAN_VS_HUMAN) {
				break;
			}
			if (TURN_PLAYER == GameConstant.TURN_TOM) {
				Object obj[] = AlphaBeta.Alpha_beta_Tom(listPointOfGameBoard, listTom, listHum, level);
				listTom = (ArrayList<Integer>) obj[0];
				listHum = (ArrayList<Integer>) obj[1];				
				AlphaBeta.update(listPointOfGameBoard, listTom, listHum);
				TURN_PLAYER = 0 - TURN_PLAYER;
				if (checkTomWin()) {
					showMessage ("Máy thắng\nBạn có muốn chơi lại không ?") ;					
				}
			}
			break;
		case GameConstant.TURN_TOM:
			Move(e.getX(), e.getY(), listTom, listMoveable, GameConstant.TOM_WIDTH, GameConstant.TOM_STATUS);
			Kill();
			if (checkTomWin()) {
				showMessage("Tôm thắng\nBạn có muốn chơi lại không ?");			
			}
			if (isVsCom == GameConstant.HUMAN_VS_HUMAN) {
				break;
			}
			if (TURN_PLAYER == GameConstant.TURN_HUM) {
				Object obj[] = AlphaBeta.Alpha_beta_Hum(listPointOfGameBoard, listTom, listHum, level);
				listTom = (ArrayList<Integer>) obj[0];
				listHum = (ArrayList<Integer>) obj[1];
				AlphaBeta.update(listPointOfGameBoard, listTom, listHum);
				TURN_PLAYER = 0 - TURN_PLAYER;
				if (checkHumWin()) {
					showMessage ("Máy thắng\nBạn có muốn chơi lại không ?") ;					
				}
			}
			break;
		}
		repaint();
	}

	@SuppressWarnings("unchecked")
	public void restart() {
		List<Object> data = FileIO.getData();
		if (data.size() != 3) {
			System.out.println("Error read file data!");
			return;
		}
		listPointOfGameBoard = (ArrayList<Point>) data.get(0);
		listTom = (ArrayList<Integer>) data.get(1);
		listHum = (ArrayList<Integer>) data.get(2);
		listMoveable = new ArrayList<Integer>();
		listCatch = new ArrayList<Integer>();
		count = 0 ;
		PLAYER_WIN = 0;
		if (isVsCom == GameConstant.HUMAN_CHOOSE_HUM) {
			TURN_PLAYER = GameConstant.TURN_HUM;
		} else {
			TURN_PLAYER = GameConstant.TURN_TOM;
		}
		chessIsChecked = -1;
	}

	public int Move(int x, int y, List<Integer> listChess, List<Integer> lstMoveable, int width, int status) {
		int position = findChessIsChecked(x, y, listChess, width);
		if (position == -1 && chessIsChecked == -1)
			return -1;
		if (chessIsChecked == -1) {
			replace(position);
			return -1;
		} else {
			if (position == chessIsChecked)
				return -1;
			if (isSame(position, listChess)) {
				replace(position);
				return -1;
			}
		}
		return whenMoved(x, y, listChess, lstMoveable, status);
	}

	public void replace(int position) {
		chessIsChecked = position;
		listMoveable = common.getListMoveable(position, listPointOfGameBoard);
	}

	public int whenMoved(int x, int y, List<Integer> listChess, List<Integer> lstMoveable, int status) {
		int nextPostion = Moveable(x, y, lstMoveable);
		if (nextPostion == -1) {
			return nextPostion;
		}
		for (int i = 0; i < listChess.size(); i++) {
			if (listChess.get(i) == chessIsChecked) {
				listChess.remove(i);
				listChess.add(nextPostion);
				listPointOfGameBoard.get(chessIsChecked).setStatus(GameConstant.EMPTY_STATUS);
				listPointOfGameBoard.get(nextPostion).setStatus(status);
				chessIsChecked = -1;
				TURN_PLAYER = 0 - TURN_PLAYER;
				turnCom = !turnCom;
				break;
			}
		}
		return nextPostion;
	}

	public int Moveable(int x, int y, List<Integer> lstMoveAble) {
		int width = (int) (GameConstant.MOVE_NEXT_WIDTH / 2);
		for (int i = 0; i < lstMoveAble.size(); i++) {
			Point point = listPointOfGameBoard.get(lstMoveAble.get(i));
			if (isCheckedOnPoint(x, y, point, width)) {
				return lstMoveAble.get(i);
			}
		}
		return -1;
	}

	// catch hum
	public void Kill() {
		for (int i = 0; i < listHum.size(); i++) {
			if (common.getListMoveable(listHum.get(i), listPointOfGameBoard).size() == 0) {
				listPointOfGameBoard.get(listHum.get(i)).setStatus(GameConstant.EMPTY_STATUS);
				listHum.remove(i);
			}
		}
	}

	// catch tom
	public void Kill(int postionCatched) {
		for (int k = 0; k < listTom.size(); k++) {
			if (listTom.get(k) == postionCatched) {
				listTom.remove(k);
				break;
			}
		}
	}

	public void Kill(int x, int y) {
		if (chessIsChecked == -1)
			return;
		listCatch = common.getListCatchable(chessIsChecked, listMoveable, listPointOfGameBoard);
		int postionCatched = whenMoved(x, y, listHum, listCatch, GameConstant.HUM_STATUS);
		if (postionCatched == -1)
			return;
		for (int k = 0; k < listTom.size(); k++) {
			if (listTom.get(k) == postionCatched) {
				listTom.remove(k);
				break;
			}
		}
	}

	public void Count(int newPosition) {
		if (listHum.size() == 1) {
			if (newPosition != -1) {
				if (newPosition <= 28 && newPosition > 24) {
					count++;
				}
				else count = 0 ;
			}
		}
	}

	public int findChessIsChecked(int x, int y, List<Integer> list, int width) {
		width = (int) width / 2;
		for (int i = 0; i < list.size(); i++) {
			Point point = listPointOfGameBoard.get(list.get(i));
			if (isCheckedOnPoint(x, y, point, width)) {
				return list.get(i);
			}
		}
		return -1;
	}

	public boolean checkHumWin() {
		if (listTom.size() <= GameConstant.NUMBER_OF_TOM_WHEN_HUM_WIN) {
			PLAYER_WIN = GameConstant.HUM_WIN;
			return true;
		}
		return false;
	}

	public boolean checkTomWin() {
		for (int i = 0; i < listHum.size(); i++) {
			if (common.getListMoveable(listHum.get(i), listPointOfGameBoard).size() > 0)
				return false;
		}
		PLAYER_WIN = GameConstant.TOM_WIN;
		return true;
	}

	public boolean isClickedOutSideTheGameBoard(int x, int y) {
		Point point = listPointOfGameBoard.get(0);
		int width = (int) (GameConstant.HUM_WIDTH / 2);
		int x_Left = point.getX() - width;
		int y_Above = point.getY() - width;
		int x_Right = listPointOfGameBoard.get(4).getX() + width;
		int y_Below = listPointOfGameBoard.get(listPointOfGameBoard.size() - 1).getY() + width;
		if (x < x_Left || x > x_Right || y > y_Below || y < y_Above) {
			return true;
		}
		return false;
	}

	public boolean isCheckedOnPoint(int x, int y, Point point, int width) {
		int x_Left = point.getX() - width;
		int x_Right = point.getX() + width;
		int y_Below = point.getY() + width;
		int y_Above = point.getY() - width;
		if (x > x_Left && x < x_Right && y < y_Below && y > y_Above) {
			return true;
		}
		return false;
	}

	public boolean isSame(int position, List<Integer> list) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i) == position)
				return true;
		}
		return false;
	}

	public void showMessage (String messeage) {
		repaint();
		int option = JOptionPane.showConfirmDialog(null, messeage,
				"Thông Báo", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
		if (JOptionPane.YES_OPTION == option) {
			restart();
		} else
			TURN_PLAYER = GameConstant.GAME_STOP;
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public int getIsVsCom() {
		return isVsCom;
	}

	public void setIsVsCom(int isVsCom) {
		this.isVsCom = isVsCom;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getTURN_PLAYER() {
		return TURN_PLAYER;
	}

	public void setTURN_PLAYER(int tURN_PLAYER) {
		TURN_PLAYER = tURN_PLAYER;
	}

}
