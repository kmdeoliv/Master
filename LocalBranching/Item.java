

public class Item {
	
	int itemId;
	int rowId;
	int binId;
	int xO;
	int yO;
	int zO;
	int xE;
	int yE;
	int zE;
	int p;
	int q;
	int r;
	long v;
	
	Item( int code, int rowId, int xO, int yO, int zO, int xE, int yE, int zE){
		this.itemId = code;
		this.rowId = rowId;
		this.xO = xO;
		this.yO = yO;
		this.zO = zO;
		this.xE = xE;
		this.yE = yE;
		this.zE = zE;
		this.p = xE-xO;
		this.q = yE-yO;
		this.r = zE-zO;
		this.v = p*q*r;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return itemId + ";" + rowId + ";" + xO + ";" + yO + ";" + zO + ";" + xE + ";" + yE + ";" + zE ;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public int getRowId() {
		return rowId;
	}

	public void setRowId(int rowId) {
		this.rowId = rowId;
	}

	public int getxO() {
		return xO;
	}

	public void setxO(int xO) {
		this.xO = xO;
	}

	public int getyO() {
		return yO;
	}

	public void setyO(int yO) {
		this.yO = yO;
	}

	public int getzO() {
		return zO;
	}

	public void setzO(int zO) {
		this.zO = zO;
	}

	public int getxE() {
		return xE;
	}

	public void setxE(int xE) {
		this.xE = xE;
	}

	public int getyE() {
		return yE;
	}

	public void setyE(int yE) {
		this.yE = yE;
	}

	public int getzE() {
		return zE;
	}

	public void setzE(int zE) {
		this.zE = zE;
	}
}
