package Entity;
import Abstract.AbstractChess;

public class Tom implements AbstractChess{
	private int pBefore ;
	private int pAfter ;
	private int pInOldListTom ;
	
	public Tom(int pBefore, int pAfter, int pInOldListTom) {	
		this.pBefore = pBefore;
		this.pAfter = pAfter;
		this.pInOldListTom = pInOldListTom;
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
		return pInOldListTom;
	}
	public void setpInListOld(int pInListOld) {
		this.pInOldListTom = pInListOld;
	}
	
	
}
