package Entity;

import Abstract.AbstractChess;

public class Hum implements AbstractChess{
	private int pBefore ;
	private int pAfter ;
	private int pInOldListHum ;
	private int pCatchInListTom ;
	
	public Hum(int pBefore, int pAfter, int pInOldListHum, int pCatchInListTom) {	
		this.pBefore = pBefore;
		this.pAfter = pAfter;
		this.pInOldListHum = pInOldListHum;
		this.pCatchInListTom = pCatchInListTom;
	}
	public int getpBefore() {
		return pBefore;
	}
	public void setpBefore(int pBefore) {
		this.pBefore = pBefore;
	}
	public int getpAfter() {
		return pAfter;
	}
	public void setpAfter(int pAfter) {
		this.pAfter = pAfter;
	}
	public int getpInOldList() {
		return pInOldListHum;
	}
	public void setpInOldListHum(int pInOldListHum) {
		this.pInOldListHum = pInOldListHum;
	}
	public int getpCatchInListTom() {
		return pCatchInListTom;
	}
	public void setpCatchInListTom(int pCatchInListTom) {
		this.pCatchInListTom = pCatchInListTom;
	}
	
	
}
