package Common;

import java.util.ArrayList;
import java.util.List;

import AppConstant.GameConstant;
import Entity.Point;

public class common {
	public static ArrayList<Integer> getListMoveable(int position , List<Point> listPointOfGameBoard) {
		Point point = listPointOfGameBoard.get(position);
		ArrayList<Integer> list = new ArrayList<>();
		List<Integer> listNear = point.getListNear();
		for (int i = 0; i < listNear.size(); i++) {
			Point point1 = listPointOfGameBoard.get(listNear.get(i));
			if (point1.getStatus() == GameConstant.EMPTY_STATUS) {
				list.add(listNear.get(i));
			}
		}
		return list;
	}
	public static ArrayList<Integer> getListCatchable (int chessIsChecked ,List<Integer> listMoveable, List<Point> listPointOfGameBoard ) {
		return getListCatchable (chessIsChecked ,listMoveable, listPointOfGameBoard ,GameConstant.TOM_STATUS);
	}
	public static ArrayList<Integer> getListCatchable (int chessIsChecked ,List<Integer> listMoveable, List<Point> listPointOfGameBoard ,int status) {
		ArrayList<Integer> list = new ArrayList<>();
		Point pointIsChecked = listPointOfGameBoard.get(chessIsChecked);
		List<Integer> listNear = new ArrayList<Integer>();
		for (int i = 0; i < listMoveable.size(); i++) {
			Point point = listPointOfGameBoard.get(listMoveable.get(i));
			listNear = point.getListNear();
			if (point.getX() == pointIsChecked.getX()) {
				if (point.getY() < pointIsChecked.getY()) {
					for (int j = 0; j < listNear.size(); j++) {
						Point c = listPointOfGameBoard.get(listNear.get(j));
						if (c.getStatus() == status & c.getX() == pointIsChecked.getX()
								& c.getY() < point.getY()) {
							list.add(listNear.get(j));
							break;
						}
					}
				} else if (point.getY() > pointIsChecked.getY()) {
					for (int j = 0; j < listNear.size(); j++) {
						Point c = listPointOfGameBoard.get(listNear.get(j));
						if (c.getStatus() == status & c.getX() == pointIsChecked.getX()
								& c.getY() > point.getY()) {
							list.add(listNear.get(j));
							break;
						}
					}
				}
			} else if (point.getY() == pointIsChecked.getY()) {
				if (point.getX() < pointIsChecked.getX()) {
					for (int j = 0; j < listNear.size(); j++) {
						Point c = listPointOfGameBoard.get(listNear.get(j));
						if (c.getStatus() == status & c.getX() < point.getX()
								& c.getY() == pointIsChecked.getY()) {
							list.add(listNear.get(j));
							break;
						}
					}
				} else if (point.getX() > pointIsChecked.getX()) {
					for (int j = 0; j < listNear.size(); j++) {
						Point c = listPointOfGameBoard.get(listNear.get(j));
						if (c.getStatus() == status & c.getX() > point.getX()
								& c.getY() == pointIsChecked.getY()) {
							list.add(listNear.get(j));
							break;
						}
					}
				}
			} else {
				switch (QuarterOfAPoint(pointIsChecked, point)) {
				case 1:
					diagonally(list,listNear, point, 1 ,listPointOfGameBoard,status);
					break;
				case 2:
					diagonally(list,listNear, point, 2,listPointOfGameBoard,status);
					break;
				case 3:
					diagonally(list,listNear, point, 3,listPointOfGameBoard,status);
					break;
				case 4:
					diagonally(list,listNear, point, 4,listPointOfGameBoard,status);
					break;
				}
			}
		}
		return list;
	}
	
	public static void diagonally(List<Integer> listCatch,List<Integer> list, Point point, int quarter , List<Point> listPointOfGameBoard , int status) {
		for (int j = 0; j < list.size(); j++) {
			Point c = listPointOfGameBoard.get(list.get(j));
			if (c.getStatus() == status & QuarterOfAPoint(point, c) == quarter) {
				listCatch.add(list.get(j));
				break;
			}
		}
	}
	
	public static int QuarterOfAPoint(Point point, Point point2) {
		if (point.getX() > point2.getX() && point.getY() > point2.getY())
			return 1;
		else if (point.getX() < point2.getX() && point.getY() > point2.getY())
			return 2;
		else if (point.getX() < point2.getX() && point.getY() < point2.getY())
			return 3;
		else if (point.getX() > point2.getX() && point.getY() < point2.getY())
			return 4;
		return 0;
	}
}
