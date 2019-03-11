

public class InputItem {

	int code;
	int p;
	int q;
	int r;
	double weight;
	String packageMaterial;
	String orientationConstrained;
	String product;


	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return code + ";" + p + ";" + q + ";" + r + ";" + weight + ";" + packageMaterial + ";"
				+ orientationConstrained + ";" + product + ";";
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public int getLength() {
		return p;
	}

	public void setLength(int length) {
		this.p = length;
	}

	public int getWidth() {
		return q;
	}

	public void setWidth(int width) {
		this.q = width;
	}

	public int getHeight() {
		return r;
	}

	public void setHeight(int height) {
		this.r = height;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public String getPackageMaterial() {
		return packageMaterial;
	}

	public void setPackageMaterial(String packageMaterial) {
		this.packageMaterial = packageMaterial;
	}

	public String getOrientationConstrained() {
		return orientationConstrained;
	}

	public void setOrientationConstrained(String orientationConstrained) {
		this.orientationConstrained = orientationConstrained;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}
}
