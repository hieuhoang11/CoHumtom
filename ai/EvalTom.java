package ai;

import java.util.List;

import AppConstant.GameConstant;
import Common.common;
import Entity.Point;

public class EvalTom {
	public static int die = 40;

	public static Object[] evaluate(List<Integer> listChess, List<Point> listPointOfGameBoard, List<Integer> listHum) {
		Object[] objects = new Object[4];
		int max = 0;
		int newPosition = -1;
		int oldPosition = -1;

		for (int i = 0; i < listChess.size(); i++) {
			Object obj[] = maxSum(listChess.get(i), listPointOfGameBoard, listHum,listChess);
			int m = (int) obj[0];
			if (m > max) {
				max = m;
				newPosition = (int) obj[1];
				oldPosition = listChess.get(i);

			}
		}

		objects[0] = max;
		objects[1] = newPosition;
		objects[2] = oldPosition;
		return objects;
	}
	
	// tổng giá trị lượng giá lớn nhất tại một điểm
	public static Object[] maxSum(int currentPosition , List<Point> listPointOfGameBoard,
			List<Integer> listHum , List<Integer> listTom) {
		int max = Integer.MIN_VALUE;
		int newPosition = -1;		
		Object[] objects = new Object[2];
		
		listPointOfGameBoard.get(currentPosition).setStatus(GameConstant.EMPTY_STATUS);

		List<Integer> lstMoveable = common.getListMoveable(currentPosition, listPointOfGameBoard);		
		for (int i = 0 ; i < lstMoveable.size() ; i++ ) {			
			int m = Sum (lstMoveable.get(i), currentPosition,  listPointOfGameBoard ,listHum , listTom);			
			if (m >= max) {
				max = m;
				newPosition = lstMoveable.get(i);				
			}
			
		}
			
		System.out.println(max + "  " + currentPosition);

		listPointOfGameBoard.get(currentPosition).setStatus(GameConstant.TOM_STATUS);
		
		objects[0] = max;
		objects[1] = newPosition;	
		return objects;
	}
	
	public static int Sum (int newPosition , int currentPosition , List<Point> listPointOfGameBoard , List<Integer> listHum , List<Integer> listTom) {
		int sum = 0;
		int stt = listPointOfGameBoard.get(newPosition).getStatus();
		listPointOfGameBoard.get(newPosition).setStatus(GameConstant.TOM_STATUS);
		
		
		sum = evalOfAPoint(newPosition, listPointOfGameBoard, listHum);
		if (sum == Integer.MIN_VALUE) {
			return sum;
		}
		for (int i = 0 ; i < listHum.size() ; i++) {
			if (currentPosition != listHum.get(i)) {
				int val = evalOfAPoint(listHum.get(i), listPointOfGameBoard, listHum);
				if (val != Integer.MIN_VALUE) {
					sum = sum + val ;
				}
			}
		}
		
		
		listPointOfGameBoard.get(newPosition).setStatus(stt);
		return sum;
	}

	// hàm lượng giá lớn nhất tại điểm hiện tại
//	public static Object[] maxEvaluateOfAChess(int currentPosition, List<Point> listPointOfGameBoard,
//			List<Integer> listHum) {
//		int max = -200;
//		int newPosition = -1;
//		List<Integer> lstMoveable = common.getListMoveable(currentPosition, listPointOfGameBoard);
//		Object[] obj = new Object[2];
//
//		for (int i = 0; i < lstMoveable.size(); i++) {
//			int position = lstMoveable.get(i);
//			listPointOfGameBoard.get(currentPosition).setStatus(GameConstant.EMPTY_STATUS);
//			listPointOfGameBoard.get(position).setStatus(GameConstant.TOM_STATUS);
//			
//			int e = evalOfAPoint(position, currentPosition, listPointOfGameBoard, listHum) + bonus(currentPosition, listPointOfGameBoard);
//			
//			listPointOfGameBoard.get(position).setStatus(GameConstant.EMPTY_STATUS);
//			listPointOfGameBoard.get(currentPosition).setStatus(GameConstant.TOM_STATUS);
//			if (e > max) {
//				max = e;
//				newPosition = position;
//			}
//		}
//		System.out.println(max + " " + currentPosition);
//		obj[0] = max;
//		obj[1] = newPosition;
//		return obj;
//	}

	// hàm lượng giá tại 1 điểm
	public static int evalOfAPoint (int position, List<Point> listPointOfGameBoard,List<Integer> listHum) {
		int eval = 0;				
		List<Integer> listMoveable = common.getListMoveable(position, listPointOfGameBoard);
		if (listMoveable.size() == 0) {			
			return Integer.MIN_VALUE ;
		}
		// mỗi vị trí có thể đi được cộng 5 điểm ;
		eval = eval + listMoveable.size() * 5;
		
		// trừ điểm nếu vị đó bị bắt
//		List<Integer> listCatched = common.getListCatchable(position, listMoveable, listPointOfGameBoard,GameConstant.HUM_STATUS);
//		if (listCatched.size() > 0) {
//			eval = eval - die ;
//		}
		// đứng gần 1 hùm cộng 10 điểm ;
//		List<Integer> listNear = listPointOfGameBoard.get(newPosition).getListNear();
//		for (int i = 0 ; i < listNear.size() ; i++) {			
//			if (listPointOfGameBoard.get(listNear.get(i)).getStatus() == GameConstant.HUM_STATUS) {
//				eval = eval  + 10 ;
//				// nếu vị trí đó ăn đc quân đối phương cộng 80 điểm;
//				if (common.getListMoveable(listNear.get(i), listPointOfGameBoard).size() == 0) {
//					eval = eval + die * 3;
//					return eval;
//				}
//			}
//		}			
		// nếu vị trí đó làm giảm sự uy hiếp của quân địch
//		int max = 0 ;	
//		for (int i = 0 ; i < listHum.size() ; i++ ) {	
//			listPointOfGameBoard.get(newPosition).setStatus(GameConstant.EMPTY_STATUS);
//			listPointOfGameBoard.get(currentPosition).setStatus(GameConstant.TOM_STATUS);
//			List<Integer> listCatchableBefore = common.getListCatchable(listHum.get(i), common.getListMoveable(listHum.get(i), listPointOfGameBoard), listPointOfGameBoard);
//			listPointOfGameBoard.get(currentPosition).setStatus(GameConstant.EMPTY_STATUS);
//			listPointOfGameBoard.get(newPosition).setStatus(GameConstant.TOM_STATUS);
//			List<Integer> listCatchableAfter = common.getListCatchable(listHum.get(i), common.getListMoveable(listHum.get(i), listPointOfGameBoard), listPointOfGameBoard);
//			int m = listCatchableBefore.size()  - listCatchableAfter.size() ;			
//			max = max >= m ? max : m ; 
//		}
//		
//		eval = eval + max * 20;
	
		
		
		// trừ điểm nếu đi làm quân mình bị bắt
//		max = 0 ;
//		for (int i = 0 ; i < listHum.size() ; i++ ) {	
//			listPointOfGameBoard.get(newPosition).setStatus(GameConstant.EMPTY_STATUS);
//			listPointOfGameBoard.get(currentPosition).setStatus(GameConstant.TOM_STATUS);
//			List<Integer> listCatchableBefore = common.getListCatchable(listHum.get(i), common.getListMoveable(listHum.get(i), listPointOfGameBoard), listPointOfGameBoard);
//			listPointOfGameBoard.get(currentPosition).setStatus(GameConstant.EMPTY_STATUS);
//			listPointOfGameBoard.get(newPosition).setStatus(GameConstant.TOM_STATUS);
//			List<Integer> listCatchableAfter = common.getListCatchable(listHum.get(i), common.getListMoveable(listHum.get(i), listPointOfGameBoard), listPointOfGameBoard);
//			int m = listCatchableAfter.size()  - listCatchableBefore.size() ;
//			max = max >= m ? max : m ; 
//			// cộng điểm nếu bẫy quân địch
//			if (1 == max && listCatchableAfter.size() == 1) {
//				if (common.getListMoveable(listCatchableAfter.get(0), listPointOfGameBoard).size() == 1) {
//					max = 0 ;
//					eval = eval + die;
//				}
//			}
//		}	
//		eval = eval - max * die;
		return eval;
	}

	// cộng điểm để đi vào vị trí có thể bắt được quân đối phương
	// cộng điểm vị trí hiện tại nếu có thể bị bắt
	public static int bonus (int currentPosition , List<Point> listPointOfGameBoard) {	
		List<Integer> listCatch = common.getListCatchable(currentPosition, common.getListMoveable(currentPosition, listPointOfGameBoard), listPointOfGameBoard,GameConstant.HUM_STATUS);
		if (listCatch.size() > 0 ) {
			return die;
		}
		return 0;
	}
}
