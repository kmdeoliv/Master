
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author kelly_000
 *
 */
public class Bin {

	int binId;
	String type;
	int L;
	int W;
	int H;
	long V;
	double weight;
	long volume;
	boolean open;
	LinkedList<Item> items;
	LinkedList<Row> rows;
	LinkedList<Layer> layers;
	LinkedList<Stack> stacks;
	PriorityQueue<Skyline> skylineHeap;
	LinkedList<Skyline> skylines;

	Bin(int binId, String type, int length, int width, int height, double weight) {
		this.binId = binId;
		this.type = type;
		this.L = length;
		this.W = width;
		this.H = height;
		this.V = 0;
		this.weight = weight;
		this.volume = length * width * height;
		this.open = true;
		this.items = new LinkedList<Item>();
		this.rows = new LinkedList<Row>();
		this.layers = new LinkedList<Layer>();
		this.stacks = new LinkedList<Stack>();
		Comparator<Skyline> comparator = Comparator.comparing(Skyline::getyO).thenComparing(Skyline::getSize);
		this.skylines = new LinkedList<Skyline>();
		this.skylineHeap = new PriorityQueue<Skyline>(comparator);
	}


	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return binId + ";" + type;
	}

	public int getBinId() {
		return binId;
	}

	public void setBinId(int binId) {
		this.binId = binId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getLength() {
		return L;
	}

	public void setLength(int length) {
		this.L = length;
	}

	public int getWidth() {
		return W;
	}

	public void setWidth(int width) {
		this.W = width;
	}

	public int getHeight() {
		return H;
	}

	public void setHeight(int height) {
		this.H = height;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	public PriorityQueue<Skyline> getSkylineHeap() {
		return skylineHeap;
	}

	public void setSkylineHeap(PriorityQueue<Skyline> skylineHeap) {
		this.skylineHeap = skylineHeap;
	}

	public long getV() {
		return V;
	}


	public void setV(long v) {
		V = v;
	}


	public LinkedList<Skyline> getSkylines() {
		return skylines;
	}

	public void setSkylines(LinkedList<Skyline> skylines) {
		this.skylines = skylines;
	}
}
