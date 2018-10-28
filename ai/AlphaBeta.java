package ai;

import java.util.ArrayList;
import java.util.List;

import AppConstant.GameConstant;
import Common.common;
import Entity.Point;

public class AlphaBeta {

	public static boolean stop(List<Integer> listTom, List<Integer> listHum, int dept) {
		if (dept == 5)
			return true;
		if (listTom.size() == 1)
			return true;
		if (listHum.size() == 0)
			return true;
		return false;
	}

	public static int MinVal(List<Point> listPointOfGameBoard, List<Integer> listTom, List<Integer> listHum, int alpha,
			int beta, int dept) {
		int minVal = Integer.MIN_VALUE;
		if (stop(listTom, listHum, dept) == true) {
			minVal = Evaluation.eval(listPointOfGameBoard, listTom, listHum);

		} else {

			for (int i = 0; i < listTom.size(); i++) {
				List<Integer> list = common.getListMoveable(listTom.get(i), listPointOfGameBoard);

				for (int j = 0; j < list.size(); j++) {

					ArrayList<Integer> cloneListTom = new ArrayList<>(listTom);
					ArrayList<Integer> cloneListHum = new ArrayList<>(listHum);

					move(listTom.get(i), list.get(j), cloneListTom, listPointOfGameBoard, GameConstant.TOM_STATUS);
					kill(cloneListTom, cloneListHum, listPointOfGameBoard);
					beta = Math.min(beta, MaxVal(listPointOfGameBoard, cloneListTom, cloneListHum, alpha, beta, dept + 1));
					
					update(listPointOfGameBoard, listTom, listHum);
					
					if (alpha >= beta) {

						return beta;
					}
				}
			}

			minVal = beta;
		}
		return minVal;
	}

	public static int MaxVal(List<Point> listPointOfGameBoard, List<Integer> listTom, List<Integer> listHum, int alpha,
			int beta, int dept) {

		int maxVal = Integer.MAX_VALUE;
		if (stop(listTom, listHum, dept) == true) {
			maxVal = Evaluation.eval(listPointOfGameBoard, listTom, listHum);

		} else {

			for (int i = 0; i < listHum.size(); i++) {
				List<Integer> list = common.getListMoveable(listHum.get(i), listPointOfGameBoard);

				List<Integer> listCath = common.getListCatchable(listHum.get(i), list, listPointOfGameBoard);

				for (int j = 0; j < listCath.size(); j++) {
					ArrayList<Integer> cloneListTom = new ArrayList<>(listTom);
					ArrayList<Integer> cloneListHum = new ArrayList<>(listHum);			

					kill(listCath.get(j), cloneListTom);
					move(listHum.get(i), listCath.get(j), cloneListHum, listPointOfGameBoard, GameConstant.HUM_STATUS);

					alpha = Math.max(alpha, MinVal(listPointOfGameBoard, cloneListTom, cloneListHum, alpha, beta, dept + 1));
					update(listPointOfGameBoard, listTom, listHum);
					if (alpha >= beta) {
						return alpha;
					}
				}

				for (int j = 0; j < list.size(); j++) {

					ArrayList<Integer> cloneListHum = new ArrayList<>(listHum);					

					move(listHum.get(i), list.get(j), cloneListHum, listPointOfGameBoard, GameConstant.HUM_STATUS);

					alpha = Math.max(alpha, MinVal(listPointOfGameBoard, listTom, cloneListHum, alpha, beta, dept + 1));
					update(listPointOfGameBoard, listTom, listHum);
					if (alpha >= beta) {
						return alpha;
					}

				}

			}
			maxVal = alpha;
		}
		return maxVal;
	}

	public static Object[] Alpha_beta(List<Point> listPointOfGameBoard, List<Integer> listTom, List<Integer> listHum) {
		Object[] objects = new Object[2];

		int alpha = Integer.MIN_VALUE;
		int beta = Integer.MAX_VALUE;

		for (int i = 0; i < listHum.size(); i++) {
			List<Integer> listMoveable = common.getListMoveable(listHum.get(i), listPointOfGameBoard);
			List<Integer> listCatch = common.getListCatchable(listHum.get(i), listMoveable, listPointOfGameBoard);
			for (int j = 0; j < listMoveable.size(); j++) {
				ArrayList<Integer> clonelistHum = new ArrayList<>(listHum);
				//ArrayList<Point> clonelistPoint = new ArrayList<>(listPointOfGameBoard);

				move(listHum.get(i), listMoveable.get(j), clonelistHum, listPointOfGameBoard, GameConstant.HUM_STATUS);

				
				
				List<Integer> re1 = clonelistHum;
				//List<Point> re2 = clonelistPoint;
				int re = MinVal(listPointOfGameBoard, listTom, clonelistHum, alpha, beta, 0);
				update(listPointOfGameBoard, listTom, listHum);
				
				
				if (alpha < re) {
					alpha = re;
					objects[0] = listTom;
					objects[1] = re1;					
				}
			}

			for (int j = 0; j < listCatch.size(); j++) {
				ArrayList<Integer> cloneListHum = new ArrayList<>(listHum);
				ArrayList<Integer> cloneListTom = new ArrayList<>(listTom);
				//ArrayList<Point> cloneListPoint = new ArrayList<>(listPointOfGameBoard);

				kill(listCatch.get(j), cloneListTom);
				move(listHum.get(i), listCatch.get(j), cloneListHum, listPointOfGameBoard, GameConstant.HUM_STATUS);

				List<Integer> re0 = cloneListTom;
				List<Integer> re1 = cloneListHum;
				

				int re = MinVal(listPointOfGameBoard, cloneListTom, cloneListHum, alpha, beta, 0);
				update(listPointOfGameBoard, listTom, listHum);
				
				if (alpha < re) {
					alpha = re;
					objects[0] = re0;
					objects[1] = re1;
					
				}

			}
		}
		System.out.println("gia tri alpha " + alpha);
		return objects;
	}

	public static void kill(int position, List<Integer> listTom) {
		for (int k = 0; k < listTom.size(); k++) {
			if (listTom.get(k) == position) {
				listTom.remove(k);
				break;
			}
		}
	}

	public static void kill(List<Integer> listTom, List<Integer> listHum, List<Point> listPointOfGameBoard) {
		for (int i = 0; i < listHum.size(); i++) {
			if (common.getListMoveable(listHum.get(i), listPointOfGameBoard).size() == 0) {
				listPointOfGameBoard.get(listHum.get(i)).setStatus(GameConstant.EMPTY_STATUS);
				listHum.remove(i);
			}
		}
	}

	public static void move(int oldPosition, int newPosition, List<Integer> listChess, List<Point> listPointOfGameBoard,
			int status) {
		for (int i = 0; i < listChess.size(); i++) {
			if (listChess.get(i) == oldPosition) {
				listChess.remove(i);
				listChess.add(newPosition);
				Point p = listPointOfGameBoard.get(oldPosition);
				Point p2 = new Point(p.getX(), p.getY(), p.getStatus(), p.getListNear());
				p2.setStatus(GameConstant.EMPTY_STATUS);
				listPointOfGameBoard.set(oldPosition, p2);
				p = listPointOfGameBoard.get(newPosition);
				p2 = new Point(p.getX(), p.getY(), p.getStatus(), p.getListNear());
				p2.setStatus(status);
				listPointOfGameBoard.set(newPosition, p2);
				break;
			}
		}
	}

	public static void update(List<Point> listPointOfGameBoard, List<Integer> listTom, List<Integer> listHum) {
		for (int i = 0; i < listPointOfGameBoard.size(); i++) {
			listPointOfGameBoard.get(i).setStatus(GameConstant.EMPTY_STATUS);
		}
		for (int i = 0; i < listTom.size(); i++) {
			listPointOfGameBoard.get(listTom.get(i)).setStatus(GameConstant.TOM_STATUS);
		}
		for (int i = 0; i < listHum.size(); i++) {
			listPointOfGameBoard.get(listHum.get(i)).setStatus(GameConstant.HUM_STATUS);
		}
	}
}
