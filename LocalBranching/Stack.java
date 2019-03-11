

public class Stack {

	int stackId;
	int binId;
	int xO;
	int yO;
	int zO;
	int xE;
	int yE;
	int zE;

	Stack(int stackId, int binId, int xO, int yO, int zO, int xE, int yE, int zE) {
		this.stackId = stackId;
		this.binId = binId;
		this.xO = xO;
		this.yO = yO;
		this.zO = zO;
		this.xE = xE;
		this.yE = yE;
		this.zE = zE;
	}
	
	

	public int getStackId() {
		return stackId;
	}



	public void setStackId(int stackId) {
		this.stackId = stackId;
	}



	public int getBinId() {
		return binId;
	}



	public void setBinId(int binId) {
		this.binId = binId;
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



	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return stackId + ";" + binId + ";" + xO + ";" + yO + ";" + zO + ";" + xE + ";" + yE + ";" + zE;
	}

}
