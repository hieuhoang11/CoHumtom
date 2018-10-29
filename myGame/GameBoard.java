package myGame;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;
import AppConstant.GameConstant;
import Entity.*;

public class GameBoard {
	public static void clearGameBoard (Graphics g) {				
		g.setColor(GameConstant.BACKGROUND_COLOR);
		//g.fillRect(0, 0, width, GameConstant.FRAME_HEIGHT);//GameConstant.FRAME_WIDTH
		g.fillRect(0, 0, 500 , GameConstant.FRAME_HEIGHT);
	}
	public static void paintGameBoard (Graphics g , List<Point> listPointOfGameBoard) {
		g.setColor(GameConstant.GAME_BOARD_COLOR);
		for (int i = 0 ; i < listPointOfGameBoard.size() ; i++) {
			Point point = listPointOfGameBoard.get(i);			
			List<Integer> list = point.getListNear();
			for (int j = 0 ; j < list.size() ; j++) {
				Point point2 = listPointOfGameBoard.get(list.get(j));
				g.drawLine(point.getX(), point.getY(), point2.getX(), point2.getY());	
			}
		}
	}
	public static void paintChess (Graphics g , List<Integer> listTom , List<Integer> listHum , List<Point> listPointOfGameBoard , int chessIsChecked) {
		drawChess (g , GameConstant.TOM_COLOR , GameConstant.TOM_COLOR_IS_CHECKED , GameConstant.TOM_WIDTH , listTom , listPointOfGameBoard , chessIsChecked);
		drawChess (g , GameConstant.HUM_COLOR , GameConstant.HUM_COLOR_IS_CHECKED , GameConstant.HUM_WIDTH , listHum , listPointOfGameBoard , chessIsChecked);		
	}
	public static void drawChess (Graphics g , Color color ,Color color2 , int width , List<Integer> list , List<Point> listPointOfGameBoard , int chessIsChecked) {
		g.setColor(color);
		int w = (int)(width /2);
		for(int i = 0 ; i < list.size() ; i++) {
			Point point = listPointOfGameBoard.get(list.get(i));
			if (list.get(i) == chessIsChecked) {
				g.setColor(color2);
				g.fillOval((point.getX() - w),(point.getY() - w) , width, width);
				g.setColor(color);
			}
			else {
				g.fillOval((point.getX() - w),(point.getY() - w) , width, width);
			}			
		}
	}
	public static void paintListMoveNext (Graphics g , List<Integer> listMoveNext , List<Point> listPointOfGameBoard) {
		g.setColor(GameConstant.MOVE_NEXT_COLOR);
		int w = (int) GameConstant.MOVE_NEXT_WIDTH / 2;
		for (int i=0 ; i < listMoveNext.size();i++) {
			Point point = listPointOfGameBoard.get(listMoveNext.get(i));
			g.fillOval((point.getX() - w), (point.getY() - w) , GameConstant.MOVE_NEXT_WIDTH, GameConstant.MOVE_NEXT_WIDTH);
		}	
//		System.out.println();
//		for (int i = 0 ;i< listMoveNext.size();i++) {
//			System.out.print(listMoveNext.get(i) + " ");
//		}

	}
	public static void paintListCatch (Graphics g , List<Integer> listCatch , List<Point> listPointOfGameBoard) {
		g.setColor(GameConstant.HUM_COLOR);	
		int w = (int) (GameConstant.TOM_WIDTH / 2) +1;
		for (int i=0 ; i < listCatch.size();i++) {
			Point point = listPointOfGameBoard.get(listCatch.get(i));
			g.drawOval((point.getX() - w),(point.getY() - w) , 2*w, 2*w);
		}	
	}
}
