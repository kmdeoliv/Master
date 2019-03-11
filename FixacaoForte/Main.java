import java.util.ArrayList;
import java.util.Comparator;

public class Main {

	static int size;
	static int bin_max_dimension;
	static double toleranceItemProductDemands;
	static double maximumDensityAllowed;
	static int maxNumberOfRowsInAlayer;
	static int maxNumberOfItemsInARow;
	static double layerSizeDeviationTolerance;
	static double rowSizeDeviationTolerance;
	static double itemSizeDeviationTolerance;
	static double maximumWeigthAboveItem;
	static ArrayList<InputItem> inputItems;
	static ArrayList<InputBin> inputBins;
	static ArrayList<Item> items;
	static ArrayList<Row> rows;
	static ArrayList<Layer> layers;
	static ArrayList<Stack> stacks;
	static ArrayList<Bin> bins;
	static ArrayList<InputItem> inputItemsModel;
	static ArrayList<Item> itemsModel;
	static ArrayList<Row> rowsModel;
	static ArrayList<Layer> layersModel;
	static ArrayList<Stack> stacksModel;
	static ArrayList<Bin> binsModel;
	static int numBinsModel;
	static double objectiveValue;
	static double runtime;
	static double gap;

	public static void main(String[] args) {

		Util ut = new Util(args[0]);
		ut.readFile();

		System.out.println(args[0]);

		BFW bestFit = new BFW(inputItems, inputBins);

		bestFit.buildBase();

		items = bestFit.getItems();
		rows = bestFit.getRows();
		layers = bestFit.getLayers();
		stacks = bestFit.getStacks();
		bins = bestFit.getBins();

		ut.readFile();

		items.sort(Comparator.comparing(Item::getItemId));
		bins.sort(Comparator.comparing(Bin::getBinId));

		Model m;
		String method = "FF";

		m = new Model();
		
		long startTime = System.currentTimeMillis();
		long endTime = startTime + Integer.parseInt(args[2]) * 1000;
		
		m.fixacaoForte(items, bins, args[0], method, Double.parseDouble(args[1]));

		while (System.currentTimeMillis() < endTime) {
			
			for (Item item : itemsModel) {
				item.xO = item.xO / 100;
				item.yO = item.yO / 100;
				item.zO = item.zO / 100;
				item.xE = item.xE / 100;
				item.yE = item.yE / 100;
				item.zE = item.zE / 100;
				item.p = item.p / 100;
				item.q = item.q / 100;
				item.r = item.r / 100;
				item.v = item.v / 1000000;
				binsModel.get(item.binId).V = binsModel.get(item.binId).V + item.v;
			}

			binsModel.removeIf(bin -> bin.V == 0);

			binsModel.sort(Comparator.comparing(Bin::getBinId));
			itemsModel.sort(Comparator.comparing(Item::getItemId));

			int b = 0;
			for (Bin bin : binsModel) {
				if (bin.binId != b) {
					for (Item item : itemsModel) {
						if (item.binId == bin.binId) {
							item.binId = b;
						}
					}
					bin.binId = b;
				}
				b++;
			}
			
			m.fixacaoForte(itemsModel, binsModel, args[0], method, Double.parseDouble(args[1]));
		}

		long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime - startTime;

		inputItemsModel.sort(Comparator.comparing(InputItem::getCode));
		itemsModel.sort(Comparator.comparing(Item::getItemId));
		rowsModel.sort(Comparator.comparing(Row::getRowId));
		layersModel.sort(Comparator.comparing(Layer::getLayerId));
		stacksModel.sort(Comparator.comparing(Stack::getStackId));
		binsModel.sort(Comparator.comparing(Bin::getBinId));

		ut.writeSolution(inputItemsModel, itemsModel, rowsModel, layersModel, stacksModel, binsModel, method);
		ut.writeResult(method, size, bins.size(), numBinsModel, objectiveValue, gap, elapsedTime * 0.001);

		System.out.println("Fim");
	}
}
