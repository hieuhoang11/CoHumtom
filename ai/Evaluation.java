package ai;

import java.util.List;

import AppConstant.GameConstant;
import Entity.Point;
import Common.*;

public class Evaluation {
	public static final int eat = 60;

	public static Object[] evaluate(List<Integer> listChess, List<Point> listPointOfGameBoard) {
		Object[] objects = new Object[4];
		int max = Integer.MIN_VALUE;
		int newPosition = -1;
		int oldPosition = -1;
		boolean isCatched = false;
		for (int i = 0; i < listChess.size(); i++) {// maxEvaluateOfAChess
			Object obj[] = maxSum(listChess.get(i), listPointOfGameBoard, listChess);
			int m = (int) obj[0];
			if (m > max) {
				max = m;
				newPosition = (int) obj[1];
				oldPosition = listChess.get(i);
				isCatched = (boolean) obj[2];
			}
		}
		// System.out.println(max);
		objects[0] = max;
		objects[1] = newPosition;
		objects[2] = oldPosition;
		objects[3] = isCatched;
		return objects;
	}

	// giá trị lượng giá lớn nhất tại một điểm khi chưa di chuyển
	public static Object[] maxEvaluateOfAChess(int currentPosition, List<Point> listPointOfGameBoard,
			List<Integer> listChess) {

		int max = 0;
		int newPosition = -1;
		boolean isCatched = false;
		listPointOfGameBoard.get(currentPosition).setStatus(GameConstant.EMPTY_STATUS);

		List<Integer> lstMoveable = common.getListMoveable(currentPosition, listPointOfGameBoard);
		Object[] obj = calculateEval(currentPosition, lstMoveable, 0, listPointOfGameBoard);
		// Object[] obj = maxSum(position ,lstMoveable, 0, max,
		// listPointOfGameBoard,listChess);
		max = (int) obj[0];// + (int) canBeArrested(currentPosition,
							// listPointOfGameBoard)[0];
		newPosition = (int) obj[1];

		List<Integer> lstCatchable = common.getListCatchable(currentPosition, lstMoveable, listPointOfGameBoard);
		Object[] obj1 = calculateEval(currentPosition, lstCatchable, eat, listPointOfGameBoard);
		// Object[] obj1 = maxSum(position ,lstCatchable, eat, max,
		// listPointOfGameBoard,listChess);
		int max1 = (int) obj1[0];// + (int) canBeArrested(currentPosition,
									// listPointOfGameBoard)[1];
		if (max1 > max) {
			max = max1;
			newPosition = (int) obj1[1];
			isCatched = true;
		}

		System.out.println(max + " " + currentPosition);
		listPointOfGameBoard.get(currentPosition).setStatus(GameConstant.HUM_STATUS);

		Object[] objects = new Object[3];
		objects[0] = max;
		objects[1] = newPosition;
		objects[2] = isCatched;
		return objects;
	}

	// tổng giá trị lượng giá lớn nhất tại một điểm
	public static Object[] maxSum(int currentPosition, List<Point> listPointOfGameBoard, List<Integer> listHum) {
		int max = Integer.MIN_VALUE;

		int newPosition = -1;
		boolean isCatched = false;
		Object[] objects = new Object[3];

		listPointOfGameBoard.get(currentPosition).setStatus(GameConstant.EMPTY_STATUS);

		List<Integer> lstMoveable = common.getListMoveable(currentPosition, listPointOfGameBoard);
		for (int i = 0; i < lstMoveable.size(); i++) {
			listPointOfGameBoard.get(lstMoveable.get(i)).setStatus(GameConstant.HUM_STATUS);
			int m = Sum(lstMoveable.get(i), currentPosition, 0, listPointOfGameBoard, listHum, 0);
			listPointOfGameBoard.get(lstMoveable.get(i)).setStatus(GameConstant.EMPTY_STATUS);
			if (m > max) {
				max = m;
				newPosition = lstMoveable.get(i);
			}

		}

		List<Integer> lstCatchable = common.getListCatchable(currentPosition, lstMoveable, listPointOfGameBoard);
		for (int i = 0; i < lstCatchable.size(); i++) {
			listPointOfGameBoard.get(lstCatchable.get(i)).setStatus(GameConstant.HUM_STATUS);
			// int m = eat + Sum (listPointOfGameBoard ,listHum);//(int)
			// canBeArrested(currentPosition, listPointOfGameBoard)[1] +
			int m = Sum(lstCatchable.get(i), currentPosition, eat, listPointOfGameBoard, listHum, 1);
			listPointOfGameBoard.get(lstCatchable.get(i)).setStatus(GameConstant.TOM_STATUS);
			if (m > max) {
				max = m;
				newPosition = lstCatchable.get(i);
				isCatched = true;
			}

		}
		System.out.println(max + "  " + currentPosition);

		listPointOfGameBoard.get(currentPosition).setStatus(GameConstant.HUM_STATUS);

		objects[0] = max;
		objects[1] = newPosition;
		objects[2] = isCatched;
		return objects;
	}

	public static int Sum(int newPosition, int currentPosition, int evalution, List<Point> listPointOfGameBoard,
			List<Integer> listHum, int c) {

		// int stt = listPointOfGameBoard.get(newPosition).getStatus();
		// listPointOfGameBoard.get(newPosition).setStatus(GameConstant.HUM_STATUS);

		int sum = 0;
		// int sum = (int) canBeArrested(currentPosition,
		// listPointOfGameBoard)[c];
		// if ((int) canBeArrested(currentPosition, listPointOfGameBoard)[1] !=
		// 0 ) {
		// //System.out.println(sum + "cong vao day ma " + );
		// }
		sum = sum + evalOfAPoint(newPosition, evalution, listPointOfGameBoard);
		for (int i = 0; i < listHum.size(); i++) {
			if (currentPosition != listHum.get(i)) {
				sum = sum + evalOfAPoint(listHum.get(i), 0, listPointOfGameBoard);
			}
		}
		// listPointOfGameBoard.get(newPosition).setStatus(stt);
		return sum;
	}

	public static int Sum(List<Point> listPointOfGameBoard, List<Integer> listHum) {
		int sum = 0;
		for (int i = 0; i < listHum.size(); i++) {
			sum = sum + evalOfAPoint(listHum.get(i), 0, listPointOfGameBoard);
		}

		return sum;
	}

	// giá trị lượng giá lớn nhất tại một điểm
	public static Object[] calculateEval(int currentPosition, List<Integer> lstMoveable, int eval,
			List<Point> listPointOfGameBoard) {
		int max = -200;
		int newPosition = -1;
		Object[] obj = new Object[2];

		for (int i = 0; i < lstMoveable.size(); i++) {
			int position = lstMoveable.get(i);
			int stt = listPointOfGameBoard.get(position).getStatus();
			listPointOfGameBoard.get(position).setStatus(GameConstant.HUM_STATUS);

			int e = evalOfAPoint(position, eval, listPointOfGameBoard);
			if (e > max) {
				max = e;
				newPosition = lstMoveable.get(i);
			}

			listPointOfGameBoard.get(position).setStatus(stt);
		}
		obj[0] = max;
		obj[1] = newPosition;
		return obj;
	}

	// hàm lượng giá tại 1 điểm
	public static int evalOfAPoint(int position, int evalution, List<Point> listPointOfGameBoard) {
		int eval = evalution;

		// Không đi vào nước bị bắt
		List<Integer> listMoveable = common.getListMoveable(position, listPointOfGameBoard);
		if (listMoveable.size() == 1) {
			List<Integer> listNear = listPointOfGameBoard.get(listMoveable.get(0)).getListNear();
			for (int k = 0; k < listNear.size(); k++) {
				if (listPointOfGameBoard.get(listNear.get(k)).getStatus() == GameConstant.TOM_STATUS) {
					eval = eval - eat;
					break;
				}
			}
		}

		// không đi vào nước đẩy quân mình vào chỗ bị bắt
		List<Integer> listNear = listPointOfGameBoard.get(position).getListNear();
		for (int k = 0; k < listNear.size(); k++) {
			if (listPointOfGameBoard.get(listNear.get(k)).getStatus() == GameConstant.HUM_STATUS) {
				// List<Integer> list = common.getListMoveable(listNear.get(k),
				// listPointOfGameBoard);
				// if (list.size() == 1) {
				// List<Integer> l =
				// listPointOfGameBoard.get(list.get(0)).getListNear();
				// List<Integer> l1 =
				// listPointOfGameBoard.get(listNear.get(k)).getListNear();
				// for (int i = 0 ; i < l.size() ; i++) {
				// if (listPointOfGameBoard.get(l.get(i)).getStatus() ==
				// GameConstant.TOM_STATUS) {
				// int kt = -1;
				// for (int j = 0 ; j < l1.size() ; j++) {
				// if (l1.get(j) == l.get(i)) {
				// kt = 1;break;
				// }
				// }
				// if (kt != -1) {
				// return -eat*2;
				// }
				// }
				// }
				// }
				// int a = canBeArrested(listNear.get(k), listPointOfGameBoard);
				// if (a != 0) {
				// return -a;
				// }
			}
		}

		eval = eval + listMoveable.size() * 10;
		List<Integer> listCatchable = common.getListCatchable(position, listMoveable, listPointOfGameBoard);
		eval = eval + listCatchable.size() * 20;

		// cộng điểm khi tạo lợi thế cho quân mình
		// eval = eval + bonus(currentPosition, listPointOfGameBoard,
		// listChess);

		// trừ điểm nếu gây bất lợi cho quân của mình
		// List<Integer> listNear =
		// listPointOfGameBoard.get(newPosition).getListNear();
		// for (int j = 0; j < listNear.size(); j++) {
		// if (listPointOfGameBoard.get(listNear.get(j)).getStatus() ==
		// GameConstant.HUM_STATUS) {
		// List<Integer> listMoveableOfHum =
		// common.getListMoveable(listNear.get(j), listPointOfGameBoard);
		// if (listMoveableOfHum.size() == 1) {
		// List<Integer> list =
		// listPointOfGameBoard.get(listMoveableOfHum.get(0)).getListNear();
		// for (int h = 0; h < list.size(); h++) {
		// if (listPointOfGameBoard.get(list.get(h)).getStatus() ==
		// GameConstant.TOM_STATUS) {
		// eval = eval - eat;
		// for (int g = 0; g < list.size(); g++) {
		// if (listPointOfGameBoard.get(list.get(g)).getStatus() ==
		// GameConstant.EMPTY_STATUS) {
		// eval = eval + eat;
		// break;
		// }
		// }
		// break;
		// }
		// }
		// }
		// }
		// }
		return eval;
	}
	
	

	public static int evalOfAPoint(int position, List<Point> listPointOfGameBoard, List<Integer> listTom) {
		int eval = 0;

		List<Integer> listMoveable = common.getListMoveable(position, listPointOfGameBoard);
		eval = eval + listMoveable.size() * 10;
		List<Integer> listCatchable = common.getListCatchable(position, listMoveable, listPointOfGameBoard);
		eval = eval + listCatchable.size() * 20;

		eval = eval + listPointOfGameBoard.get(position).getListNear().size() * 3;

		// Không đi vào nước bị bắt

//		if (listMoveable.size() == 1) {
//			List<Integer> listNear = listPointOfGameBoard.get(listMoveable.get(0)).getListNear();
//			//List<Integer> list = listPointOfGameBoard.get(position).getListNear();
//			for (int k = 0; k < listNear.size(); k++) {
//				if (listPointOfGameBoard.get(listNear.get(k)).getStatus() == GameConstant.TOM_STATUS) {
//					//int count = 1 ;
//					//for (int i = 0 ; i < list.size() ; i++ ) {
//						//if (list.get(i) == listNear.get(k)) {
//							eval = eval - eat;
//							break;
//						//}
//						
//					//}
//					
//				}
//			}
//		}

		return eval;
	}

	// cộng điểm nếu có nguy cơ bị bắt
	// public static int canBeArrested(int currentPosition , List<Point>
	// listPointOfGameBoard) {
	// //int result = 0;
	// //int result1 = 0;
	//
	// // List<Integer> list =
	// listPointOfGameBoard.get(currentPosition).getListNear();
	// List<Integer> listMoveable = common.getListMoveable(currentPosition,
	// listPointOfGameBoard);
	// if (listMoveable.size() == 1) {
	// if (common.getListMoveable(listMoveable.get(0),
	// listPointOfGameBoard).size() == 0) {
	//// if (common.getListCatchable(currentPosition, listMoveable,
	// listPointOfGameBoard).size() > 0) {
	//// result1 = eat*2;
	//// }
	// return 0 ;
	// }
	// else return eat;
	//// List<Integer> listNear =
	// listPointOfGameBoard.get(listMoveable.get(0)).getListNear();
	//// for (int i = 0; i < listNear.size(); i++) {
	//// if (listPointOfGameBoard.get(listNear.get(i)).getStatus() ==
	// GameConstant.TOM_STATUS) {
	//// for (int j = 0 ; j < list.size() ; j ++ ) {
	//// if (list.get(j) == listNear.get(i)) {
	//// obj[0] = result;
	//// obj[1] = result1;if (result > 0 ) System.out.println("Chay ngay di");
	////
	//// return obj;
	//// }
	////
	//// }
	//// result = eat*2;
	//// obj[0] = result;
	//// obj[1] = result1;if (result > 0 ) System.out.println("Chay ngay di");
	////
	//// return obj;
	//// }
	//// }
	// }
	// else if (listMoveable.size() == 2) {
	// List<Integer> l1 = common.getListMoveable(listMoveable.get(0),
	// listPointOfGameBoard);
	// List<Integer> l2 = common.getListMoveable(listMoveable.get(1),
	// listPointOfGameBoard);
	// if (l1.size() == 0 || l2.size() == 0) {
	//// result = eat*2;
	//// obj[0] = result;
	//// obj[1] = result1;if (result > 0 ) System.out.println("Chay ngay di");
	////
	//// return obj;
	// return eat ;
	// }
	// }
	//// if (result > 0 ) System.out.println("Chay ngay di");
	//// if (result1 > 0 ) System.out.println("an cai da");
	//// obj[0] = result;
	//// obj[1] = result1;
	//// return obj;
	// return 0 ;
	// }


	// cộng điểm ăn cho quân hiện tại nếu có thể ăn quân đang vây quân mình
	// cộng điểm đi cho quân hiện tại nếu đang cản quân mình khiến nó có thể bị
	// bắt

	
	public static int eval (List<Point> listPointOfGameBoard, List<Integer> listTom,
			List<Integer> listHum) {
		
		int eval = 0 ;
		eval = eval + (15 - listTom.size()) * eat ;
		eval = eval - (3 - listHum.size()) * eat * 5;
		
		for (int i = 0 ; i < listHum.size() ; i ++ ) {
			eval = eval + evalOfAPoint (listHum.get(i), listPointOfGameBoard , listTom) ;
		}
				
		return eval ;
	}
}
