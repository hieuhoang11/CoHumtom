package ai;

import java.util.List;

import Entity.Point;
import Common.*;

public class Evaluation {
	public static final int eat = 60;

	public static int evalOfAPoint(int position, List<Point> listPointOfGameBoard, List<Integer> listTom) {
		int eval = 0;
		List<Integer> listMoveable = common.getListMoveable(position, listPointOfGameBoard);
		eval = eval + listMoveable.size() * 10;
		List<Integer> listCatchable = common.getListCatchable(position, listMoveable, listPointOfGameBoard);
		eval = eval + listCatchable.size() * 20;
		eval = eval + listPointOfGameBoard.get(position).getListNear().size() * 3;
		return eval;
	}
	public static int eval (List<Point> listPointOfGameBoard, List<Integer> listTom,
			List<Integer> listHum) {
		int eval = 0 ;
		eval = eval + (15 - listTom.size()) * eat ;
		eval = eval - (3 - listHum.size()) * eat * 4;
		for (int i = 0 ; i < listHum.size() ; i ++ ) {
			eval = eval + evalOfAPoint (listHum.get(i), listPointOfGameBoard , listTom) ;
		}	
		return eval ;
	}
}
