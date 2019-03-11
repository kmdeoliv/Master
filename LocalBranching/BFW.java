
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

public class BFW {

	public ArrayList<InputItem> inputItems;
	public ArrayList<InputBin> inputBins;
	public static ArrayList<Item> items;
	public static ArrayList<Row> rows;
	public static ArrayList<Layer> layers;
	public static ArrayList<Stack> stacks;
	public static ArrayList<Bin> bins;
	public static int binId;
	double[][] a;
	double[][] b;
	double[][] c;
	double[][] d;
	double[][] e;
	double[][] f;

	public BFW(ArrayList<InputItem> inputItems, ArrayList<InputBin> inputBins) {
		this.inputItems = inputItems;
		this.inputBins = inputBins;
		items = new ArrayList<Item>();
		rows = new ArrayList<Row>();
		layers = new ArrayList<Layer>();
		stacks = new ArrayList<Stack>();
		bins = new ArrayList<Bin>();
		binId = 0;
	}

	public void buildBase() {

		a = new double[inputItems.size()][inputItems.size()];
		b = new double[inputItems.size()][inputItems.size()];
		c = new double[inputItems.size()][inputItems.size()];
		d = new double[inputItems.size()][inputItems.size()];
		e = new double[inputItems.size()][inputItems.size()];
		f = new double[inputItems.size()][inputItems.size()];


		inputItems.sort(Comparator.comparing(InputItem::getLength).thenComparing(InputItem::getWidth).reversed());

		for (InputItem inputItem : inputItems)
			if (openBin(inputItem, inputBins.get(0)))
				break;

		while (inputItems.size() > 0) {

			for (int j = 0; j < bins.size(); j++) {
				if (bins.get(j).open) {
					boolean open = true;
					while (open == true) {
						open = placeItem(j);
					}
				}
			}
			if (inputItems.size() > 0) {
				for (InputItem inputItem : inputItems)
					if (openBin(inputItem, inputBins.get(0)))
						break;
			}
		}
		

		for (Bin bin : bins) {
	
			for (Item item : bin.items) {
				items.add(item);
				bin.V = bin.V + item.v;
			}
			
			for (Row row : bin.rows)
				rows.add(row);

			for (Layer layer : bin.layers)
				layers.add(layer);

			for (Stack stack : bin.stacks)
				stacks.add(stack);
		}
		
		bins.sort(Comparator.comparing(Bin::getV).reversed());

		for (int j = bins.size() - 2; j < bins.size(); j++) {
			Bin bin = bins.get(j);
			for (int i = 0; i < bin.items.size(); i++) {
				for (int k = i + 1; k < bin.items.size(); k++) {
					Item item1 = bin.items.get(i);
					Item item2 = bin.items.get(k);

					a[item1.itemId][item2.itemId] = -1;
					b[item1.itemId][item2.itemId] = -1;
					c[item1.itemId][item2.itemId] = -1;
					d[item1.itemId][item2.itemId] = -1;
					e[item1.itemId][item2.itemId] = -1;
					f[item1.itemId][item2.itemId] = -1;
				}
			}
		}		
	}

	
	private boolean openBin(InputItem i, InputBin b) {
		Skyline skyline;
		Item item;
		Row row;
		Layer layer;
		Stack stack;
		Bin bin;

		if (testDimensions(i.p, i.q, i.r, b.length, b.width, b.height)) {

			item = new Item(i.code, i.code, 0, 0, 0, i.p, i.q, i.r);
			item.binId = binId;
			row = new Row(i.code, i.code, 0, 0, 0, i.p, i.q, i.r);
			layer = new Layer(i.code, i.code, 0, 0, 0, i.p, i.q, i.r, 0);
			stack = new Stack(i.code, binId, 0, 0, 0, i.p, i.q, i.r);
			bin = new Bin(binId, b.type, b.length, b.width, b.height, b.maximalWeight - i.weight);

			skyline = new Skyline(0, i.q, i.p);
			bin.skylines.add(skyline);
			bin.skylineHeap.add(skyline);

			if (b.width - i.q > 0) {
				skyline = new Skyline(i.q, b.width, 0);
				bin.skylines.add(skyline);
				bin.skylineHeap.add(skyline);
			}

			bins.add(bin);

			addObjects(bin.binId, item, row, layer, stack);

			int k = inputItems.indexOf(i);
			int code = i.code;
			inputItems.remove(i);

			stackItems(bin.binId, item, i, k);

			inputItems.removeIf(it -> it.code == code);

			binId++;

			return true;
		}

		return false;
	}

	private boolean placeItem(int j) {

		Skyline newSkyline;
		Item item;
		Row row;
		Layer layer;
		Stack stack;

		boolean stop = false;

		while (!stop) {
			stop = true;
			Skyline s = bins.get(j).skylineHeap.peek();
			int indice = bins.get(j).skylines.indexOf(s);
			Skyline previous = null;
			Skyline next = null;

			if (indice > 0)
				previous = bins.get(j).skylines.get(indice - 1);

			if (indice < (bins.get(j).skylines.size() - 1))
				next = bins.get(j).skylines.get(indice + 1);

			for (InputItem i : inputItems) {
				if (testDimensions(i.q, i.p, i.r, s.size, (bins.get(j).L - s.yO), bins.get(j).H)) {
					item = new Item(i.code, i.code, s.yO, s.xO, 0, s.yO + i.p, s.xO + i.q, i.r);
					item.binId = bins.get(j).binId;
					row = new Row(i.code, i.code, s.yO, s.xO, 0, s.yO + i.p, s.xO + i.q, i.r);
					layer = new Layer(i.code, i.code, s.yO, s.xO, 0, s.yO + i.p, s.xO + i.q, i.r, 0);
					stack = new Stack(i.code, bins.get(j).binId, s.yO, s.xO, 0, s.yO + i.p, s.xO + i.q, i.r);

					boolean exclude = false;
					boolean merge = false;
					int add;

					int xO = s.getxO();
					int xE = s.getxE();

					// testa tamanho do skyline
					if (s.xE - (s.xO + i.q) > 0) {
						xO = s.getxO();
						bins.get(j).skylineHeap.peek().setxO(xO + i.q);
						bins.get(j).skylines.get(indice).setxO(xO + i.q);

						next = bins.get(j).skylines.get(indice);
						add = indice;

					} else {
						add = indice;
						bins.get(j).skylineHeap.poll();
						bins.get(j).skylines.remove(indice);
						exclude = true;
					}

					// testa skyline direito
					if (previous != null && previous.getyO() == (s.yO + i.p)) {
						xE = bins.get(j).skylines.get(indice - 1).getxE();
						for (Skyline sky : bins.get(j).skylineHeap) {
							if (sky.equals(bins.get(j).skylines.get(indice - 1))) {
								sky.setxE(xE + i.q);
								break;
							}
						}
						bins.get(j).skylines.get(indice - 1).setxE(xE + i.q);
						s = bins.get(j).skylines.get(indice - 1);
						merge = true;
					}

					// testa skyline esquerdo
					if (next != null && next.getyO() == (s.yO + i.p)) {

						if (exclude == true)
							indice--;

						for (Skyline sky : bins.get(j).skylineHeap) {
							if (sky.equals(bins.get(j).skylines.get(indice + 1))) {
								sky.setxO(s.getxO());
								break;
							}
						}

						bins.get(j).skylines.get(indice + 1).setxO(s.getxO());

						if (merge == true) {
							s = bins.get(j).skylines.remove(indice);
							bins.get(j).skylineHeap.remove(s);
						}
						merge = true;
					}

					if (merge == false) {
						newSkyline = new Skyline(xO, xO + i.q, s.yO + i.p);
						bins.get(j).skylines.add(add, newSkyline);
						bins.get(j).skylineHeap.add(newSkyline);
					}

					addObjects(j, item, row, layer, stack);
					bins.get(j).setWeight(bins.get(j).getWeight() - i.weight);

					int k = inputItems.indexOf(i);
					int code = i.code;
					inputItems.removeIf(it -> it.code == code);

					stackItems(j, item, i, k);

					stop = false;
					return true;
				}
			}

			stop = true;
			boolean merge = false;

			if (previous != null) {
				for (Skyline sky : bins.get(j).skylineHeap) {
					if (sky.equals(bins.get(j).skylines.get(indice - 1))) {
						sky.setxE(s.getxE());
						break;
					}
				}
				bins.get(j).skylines.get(indice - 1).setxE(s.getxE());
				s = bins.get(j).skylines.get(indice - 1);

				bins.get(j).skylineHeap.poll();
				bins.get(j).skylines.remove(indice);
				indice--;
				merge = true;
				stop = false;
			}

			if (next != null) {
				if (merge == false) {
					for (Skyline sky : bins.get(j).skylineHeap) {
						if (sky.equals(bins.get(j).skylines.get(indice + 1))) {
							sky.setxO(s.getxO());
							break;
						}
					}
					bins.get(j).skylines.get(indice + 1).setxO(s.getxO());

					bins.get(j).skylines.remove(indice);
					bins.get(j).skylineHeap.poll();
					stop = false;

				} else {
					if (next.getyO() == s.yO) {
						for (Skyline sky : bins.get(j).skylineHeap) {
							if (sky.equals(bins.get(j).skylines.get(indice + 1))) {
								sky.setxO(s.getxO());
								break;
							}
						}
						bins.get(j).skylines.get(indice + 1).setxO(s.getxO());

						bins.get(j).skylines.remove(indice);
						bins.get(j).skylineHeap.remove(s);
						stop = false;
					}
				}
			}
		}

		bins.get(j).setOpen(false);
		return false;
	}

	public void addObjects(int binId, Item item, Row row, Layer layer, Stack stack) {
		bins.get(binId).items.add(item);
		bins.get(binId).rows.add(row);
		bins.get(binId).layers.add(layer);
		bins.get(binId).stacks.add(stack);
	}

	public ArrayList<Item> getItems() {
		return items;
	}

	public ArrayList<Row> getRows() {
		return rows;
	}

	public ArrayList<Layer> getLayers() {
		return layers;
	}

	public ArrayList<Stack> getStacks() {
		return stacks;
	}

	public ArrayList<Bin> getBins() {
		return bins;
	}

	public boolean testDimensions(int l, int w, int h, int L, int W, int H) {

		if (l <= L) {
			if (w <= W) {
				if (h <= H) {
					return true;
				}
			}
		}

		return false;
	}

	public void stackItems(int j, Item item, InputItem i, int k) {

		while (k < inputItems.size() && i.p == inputItems.get(k).p && i.q == inputItems.get(k).q) {
			InputItem inputItem = inputItems.get(k);

			if (bins.get(j).H - item.zE >= inputItem.r)
				item = stackup(j, item, inputItem);

			k++;
		}

		bins.get(j).layers.getLast().topFlag = 1;
	}

	public Item stackup(int j, Item item, InputItem inputItem) {
		Row row;
		Layer layer;
		item = new Item(inputItem.code, inputItem.code, item.xO, item.yO, item.zE, item.xE, item.yE, item.zE + inputItem.r);
		item.binId = bins.get(j).binId;
		row = new Row(inputItem.code, inputItem.code, item.xO, item.yO, item.zO, item.xE, item.yE, item.zE);
		layer = new Layer(inputItem.code, inputItem.code, item.xO, item.yO, item.zO, item.xE, item.yE, item.zE, 0);
		bins.get(j).stacks.getLast().zE = item.zE;

		bins.get(j).items.add(item);
		bins.get(j).rows.add(row);
		bins.get(j).layers.add(layer);
		bins.get(j).weight = bins.get(j).weight - inputItem.weight;

		int code = inputItem.code;
		inputItems.removeIf(it -> it.code == code);

		return item;
	}

}
