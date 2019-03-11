

public class Layer {

	int layerId;
	int stackId;
	int xO;
	int yO;
	int zO;
	int xE;
	int yE;
	int zE;
	int topFlag;

	Layer(int layerId, int stackId, int xO, int yO, int zO, int xE, int yE, int zE, int topFlag) {
		this.layerId = layerId;
		this.stackId = stackId;
		this.xO = xO;
		this.yO = yO;
		this.zO = zO;
		this.xE = xE;
		this.yE = yE;
		this.zE = zE;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return layerId + ";" + stackId + ";" + xO + ";" + yO + ";" + zO + ";" + xE + ";" + yE + ";" + zE + ";"
				+ topFlag;
	}

	public int getLayerId() {
		return layerId;
	}

	public int getStackId() {
		return stackId;
	}

	public int getxO() {
		return xO;
	}

	public int getyO() {
		return yO;
	}

	public int getzO() {
		return zO;
	}

	public int getxE() {
		return xE;
	}

	public int getyE() {
		return yE;
	}

	public int getzE() {
		return zE;
	}

	public int getTopFlag() {
		return topFlag;
	}
	

}
