

public class Row {
	
	int rowId;
	int layerId;
	int xO;
	int yO;
	int zO;
	int xE;
	int yE;
	int zE;
	
	Row( int rowId, int layerId, int xO, int yO, int zO, int xE, int yE, int zE){
		this.rowId = rowId;
		this.layerId = layerId;
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
		return rowId + ";" + layerId + ";" + xO + ";" + yO + ";" + zO + ";" + xE + ";" + yE + ";" + zE;
	}
}
