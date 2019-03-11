public class InputBin{

	String type;
	int length;
	int width;
	int height;
	int maximalWeight;

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return type + ";" + (length*100) + ";" + (width*100) + ";" + (height*100) + ";" + maximalWeight + ";";
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public double getMaximalWeight() {
		return maximalWeight;
	}

	public void setMaximalWeight(int maximalWeight) {
		this.maximalWeight = maximalWeight;
	}

}
