package Entity;

import java.util.List;

public class Point {
	private int x;
	private int y;
	private int status;
	private List<Integer> listNear;

	public Point() {
		x = 0;
		y = 0;
		status = 0;
	}

	public Point(int x, int y, int status, List<Integer> listNear) {
		this.x = x;
		this.y = y;
		this.status = status;
		this.listNear = listNear;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public List<Integer> getListNear() {
		return listNear;
	}

	public void setListNear(List<Integer> listNear) {
		this.listNear = listNear;
	}

}
