


public class Skyline {

	//int id;
	int xO;
	int xE;
	int yO;
	int size;
	int lposition;
	int hposition;

	//private static AtomicInteger uniqueId = new AtomicInteger();

	Skyline(int xO, int xE, int yO) {
		//this.id = uniqueId.getAndIncrement();
		this.xO = xO;
		this.xE = xE;
		this.yO = yO;
		this.size = xE - xO;
	}

	public int getxO() {
		return xO;
	}

	public void setxO(int xO) {
		this.xO = xO;
		this.setSize(this.xE - this.xO);
	}

	public int getxE() {
		return xE;
	}

	public void setxE(int xE) {
		this.xE = xE;
		this.setSize(this.xE - this.xO);
	}

	public int getyO() {
		return yO;
	}

	public void setyO(int width) {
		this.yO = width;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
	
	
	public void update(int xO, int xE) {
		//if (this.equals(s)) {
			this.setxO(xO);
			this.setxE(xE);
			return;
		//}
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "\n" + xO + " " + xE + " " + yO + " " + size;
	}

}
