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
	static ArrayList<Item> itemsModel;
	static ArrayList<Row> rowsModel;
	static ArrayList<Layer> layersModel;
	static ArrayList<Stack> stacksModel;
	static ArrayList<Bin> binsModel;
	static int numBinsModel;
	static ArrayList<InputItem> inputItemsModel;
	static double objectiveValue;
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

		items.sort(Comparator.comparing(Item::getItemId));
		bins.sort(Comparator.comparing(Bin::getBinId));

		Model m;
		m = new Model();
		long startTime = System.currentTimeMillis();
		m.localBranching(items, bins, args[0], "LBI", Double.parseDouble(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
	
		long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime - startTime;
		
		ut.writeSolution(inputItemsModel, itemsModel, rowsModel, layersModel, stacksModel, binsModel, "LBI");
		ut.writeResult("LBI", size, bins.size(), numBinsModel, objectiveValue, gap, elapsedTime * 0.001, Integer.parseInt(args[2]), Integer.parseInt(args[3]));

		System.out.println("Fim");
	}

}
