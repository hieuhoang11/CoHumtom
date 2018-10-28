package myGame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import FileIO.*;
import ai.*;

import javax.swing.JPanel;

import Abstract.AbstractChess;
import AppConstant.GameConstant;
import Common.*;
import Entity.*;

public class myGame extends JPanel implements MouseListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Point> listPointOfGameBoard;
	private List<Integer> listHum;
	private List<Integer> listTom;
	private List<Integer> listMoveable;
	private List<Integer> listCatch;
	private List<AbstractChess> listUndo;
	private int chessIsChecked = -1;
	private int TURN_PLAYER = GameConstant.GAME_STOP;
	private int PLAYER_WIN = 0;

	@SuppressWarnings("unchecked")
	public myGame() {
		List<Object> data = FileIO.getData();
		if (data.size() != 3) {
			System.out.println("Error read file data!");
			return;
		}
		listPointOfGameBoard = (ArrayList<Point>) data.get(0);
		listTom = (ArrayList<Integer>) data.get(1);
		listHum = (ArrayList<Integer>) data.get(2);		
		listUndo = new ArrayList<AbstractChess>();
		TURN_PLAYER = GameConstant.TURN_TOM;
		this.addMouseListener(this);
	}

	public void paint(Graphics g) {
		if (TURN_PLAYER == GameConstant.GAME_STOP && PLAYER_WIN == 0) {
			return;
		}
		GameBoard.clearGameBoard(g);
		GameBoard.paintGameBoard(g, listPointOfGameBoard);
		GameBoard.paintChess(g, listTom, listHum, listPointOfGameBoard, chessIsChecked);
		g.setColor(Color.RED);
		g.fillOval(550, 100, 50, 50);
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
		if (e.getX() > 550 && e.getX() < 600 && e.getY() > 100 && e.getY() < 150) {		
			Undo() ;
			repaint();
			return ;
		}
		
		if (isClickedOutSideTheGameBoard(e.getX(), e.getY())) {
			System.out.println("Click outside the gameboard!");
			return;
		}		
		switch (TURN_PLAYER) {
		case GameConstant.TURN_HUM:
			Move(e.getX(), e.getY(), listHum, listMoveable, GameConstant.HUM_WIDTH, GameConstant.HUM_STATUS);
			Kill(e.getX(), e.getY());
			if (checkHumWin()) {
				System.out.println("Hum win!");
				TURN_PLAYER = GameConstant.GAME_STOP;
			}
			repaint();
//			if (TURN_PLAYER == GameConstant.TURN_TOM) {
//				Object obj[] = EvalTom.evaluate(listTom, listPointOfGameBoard,listHum);
//				Move ((int)obj[2],(int)obj[1],listTom,GameConstant.TOM_STATUS) ;
//				Kill();
//				repaint();
//			}		
			break;
		case GameConstant.TURN_TOM:
			Move(e.getX(), e.getY(), listTom, listMoveable, GameConstant.TOM_WIDTH, GameConstant.TOM_STATUS);
			Kill();
			if (checkTomWin()) {
				System.out.println("Tom win!");
				TURN_PLAYER = GameConstant.GAME_STOP;
			}
			//repaint();
//			if (TURN_PLAYER == GameConstant.TURN_HUM) {
//				Object obj[] = Evaluation.evaluate(listHum, listPointOfGameBoard);
//				if ((boolean)obj[3] == true) {
//					Kill ((int)obj[1]);
//				}
//				Move ((int)obj[2],(int)obj[1],listHum,GameConstant.HUM_STATUS) ;
//				repaint();
//			}		
			
			if (TURN_PLAYER == GameConstant.TURN_HUM) {				
				Object obj[] = AlphaBeta.Alpha_beta(listPointOfGameBoard, listTom, listHum);
				listTom = (ArrayList<Integer>) obj[0];
				listHum = (ArrayList<Integer>) obj[1];
				AlphaBeta.update(listPointOfGameBoard, listTom, listHum);
				//listPointOfGameBoard = (ArrayList<Point>) obj[2];
				
//				System.out.println("//////hum//////");
//				for (int i = 0 ; i <listHum.size() ; i++) {
//					System.out.print(listHum.get(i) + "   ");
//				}
//				System.out.println();
//				System.out.println("//////tom//////");
//				for (int i = 0 ; i <listTom.size() ; i++) {
//					System.out.print(listTom.get(i) + "   ");
//				}
//				AlphaBeta.update(listPointOfGameBoard, listTom, listHum);
//				System.out.println();
//				System.out.println("//////ban co//////");
//				for (int i = 0 ; i <listPointOfGameBoard.size() ; i++) {
//					System.out.print(i + " " + listPointOfGameBoard.get(i).getStatus() + "   ");
//				}
				TURN_PLAYER = 0 - TURN_PLAYER ;
				
				if (checkHumWin()) {
					System.out.println("Hum win!");
					TURN_PLAYER = GameConstant.GAME_STOP;
				}
				
			}
			repaint();
			break;
		}
	}
	
	public void Undo () {
		if (listUndo.size() == 0) {
			return;
		}
		switch (TURN_PLAYER) {
		case GameConstant.TURN_HUM:						
			Undo (GameConstant.TOM_STATUS , listTom) ;
			TURN_PLAYER = GameConstant.TURN_TOM;
			break;
		case GameConstant.TURN_TOM:						
			Hum hum = (Hum) Undo (GameConstant.HUM_STATUS , listHum) ;
			if (hum.getpCatchInListTom() != -1) {
				listTom.add(-1);
				for (int i = listTom.size() - 1; i > hum.getpCatchInListTom(); i--) {					
					listTom.set(i,listTom.get( i - 1 ));					
				}							
				listTom.set(hum.getpCatchInListTom(),hum.getpAfter());
				listPointOfGameBoard.get(hum.getpAfter()).setStatus(GameConstant.TOM_STATUS);
			}
			TURN_PLAYER = GameConstant.TURN_HUM;
			break;
		}
			
	}
	
	public AbstractChess Undo (int status , List<Integer> list) {
		AbstractChess chess = listUndo.get(listUndo.size() - 1);
		listUndo.remove(listUndo.size() - 1);
		
		for (int i = list.size() - 1; i > chess.getpInOldList(); i--) {					
			list.set(i,list.get( i - 1 ));					
		}		
		list.set(chess.getpInOldList(),chess.getpBefore());			
		
		for (int i = 0; i < listPointOfGameBoard.size(); i++) {
			if (i == chess.getpAfter()) {
				listPointOfGameBoard.get(i).setStatus(GameConstant.EMPTY_STATUS);
				listPointOfGameBoard.get(chess.getpBefore()).setStatus(status);
				break;
			}
		}
		return chess;
	}
	
	public void Move (int oldPosition , int newPosition , List<Integer> listChess , int status) {	
		for (int i = 0; i < listChess.size(); i++) {
			if (listChess.get(i) == oldPosition) {
				listChess.remove(i);
				listChess.add(newPosition);
				listPointOfGameBoard.get(oldPosition).setStatus(GameConstant.EMPTY_STATUS);
				listPointOfGameBoard.get(newPosition).setStatus(status);

				if (TURN_PLAYER == GameConstant.TURN_TOM) {
					Tom tom = new Tom(oldPosition, newPosition, i);						
					listUndo.add(tom);
				} else if (TURN_PLAYER == GameConstant.TURN_HUM) {
					Hum hum = new Hum(oldPosition, newPosition, i, -1);			
					listUndo.add(hum);
				}				
				chessIsChecked = -1;
				TURN_PLAYER = 0 - TURN_PLAYER;	
				break;
			}
		}	
	}

	public void Move(int x, int y, List<Integer> listChess, List<Integer> lstMoveable, int width, int status) {
		int position = findChessIsChecked(x, y, listChess, width);
		if (position == -1 && chessIsChecked == -1)
			return;
		if (chessIsChecked == -1) {
			replace(position);
			return;
		} else {
			if (position == chessIsChecked)
				return;
			if (isSame(position, listChess)) {
				replace(position);
				return;
			}
		}
		whenMoved(x, y, listChess, lstMoveable, status);
	}

	public void replace(int position) {
		chessIsChecked = position;
		listMoveable = common.getListMoveable(position, listPointOfGameBoard);
	}
	
	public int whenMoved(int x, int y, List<Integer> listChess, List<Integer> lstMoveable, int status) {
		int nextPostion = Moveable (x,y,lstMoveable) ;
		if (nextPostion == -1) {
			return nextPostion;
		}	
		Move (chessIsChecked , nextPostion , listChess ,status);
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

	public void Kill () {
		for (int i = 0 ; i < listHum.size() ; i++ ) {
			if (common.getListMoveable(listHum.get(i), listPointOfGameBoard).size() == 0) {
				listPointOfGameBoard.get(listHum.get(i)).setStatus(GameConstant.EMPTY_STATUS);
				listHum.remove(i);		
			}
		}		
	}
	public void Kill (int postionCatched) {
		for (int k = 0; k < listTom.size(); k++) {
			if (listTom.get(k) == postionCatched) {				
				listTom.remove(k);										
				//Hum hum = new Hum(oldPosition, postionCatched, pInOldListHum , k);
				//listUndo.remove(listUndo.size() - 1);	
				//listUndo.add(hum);
				break;			
			}
		}	
	}

	public void Kill(int x, int y) {
		if (chessIsChecked == -1)
			return;
		listCatch = common.getListCatchable(chessIsChecked, listMoveable, listPointOfGameBoard);
		int chessChecked = chessIsChecked;
		int pInOldListHum = -1;
		for (int i = 0; i < listHum.size(); i++) {
			if (listHum.get(i) == chessChecked) {
				pInOldListHum = i;
				break;
			}
		}
		int postionCatched = whenMoved(x, y, listHum, listCatch, GameConstant.HUM_STATUS);
		if (postionCatched == -1)
			return;
		for (int k = 0; k < listTom.size(); k++) {
			if (listTom.get(k) == postionCatched) {				
				listTom.remove(k);										
				Hum hum = new Hum(chessChecked, postionCatched, pInOldListHum , k);
				listUndo.remove(listUndo.size() - 1);	
				listUndo.add(hum);
				break;			
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
			if (common.getListMoveable(listHum.get(i) , listPointOfGameBoard).size() > 0)
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
	
//	public void test () {		
//		EvalTom.evalOfAPoint(16, 10, listPointOfGameBoard, listTom);	
//	}
}
