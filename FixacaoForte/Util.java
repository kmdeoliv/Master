
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class Util {

	String instance;

	public Util(String instance) {
		this.instance = instance;
	}
	
	public void readFile() {

		try {

			File binInput = new File("./" + instance + "/input_bin.csv");
			File itemInput = new File("./" + instance + "/input_items_original.csv");
			File parameters = new File("./" + instance + "/parameters.txt");

			Scanner input = new Scanner(new FileReader(parameters));

			String[] object = input.nextLine().split("\\=");
			Main.toleranceItemProductDemands = Double.parseDouble(object[1]);

			object = input.nextLine().split("\\=");
			Main.maximumDensityAllowed = Double.parseDouble(object[1]);

			object = input.nextLine().split("\\=");
			Main.maxNumberOfRowsInAlayer = Integer.parseInt(object[1]);

			object = input.nextLine().split("\\=");
			Main.maxNumberOfItemsInARow = Integer.parseInt(object[1]);

			object = input.nextLine().split("\\=");
			Main.layerSizeDeviationTolerance = Double.parseDouble(object[1]);

			object = input.nextLine().split("\\=");
			Main.rowSizeDeviationTolerance = Double.parseDouble(object[1]);

			object = input.nextLine().split("\\=");
			Main.itemSizeDeviationTolerance = Double.parseDouble(object[1]);

			object = input.nextLine().split("\\=");
			Main.maximumWeigthAboveItem = Double.parseDouble(object[1]);

			input = new Scanner(new FileReader(itemInput));
			input.nextLine();

			Main.inputItems = new ArrayList<InputItem>();

			int minDimension = 0;
			int cont = 0;
			while (input.hasNextLine()) {
				cont++;
				object = input.nextLine().split("\\;");
				InputItem inputItem = new InputItem();
				inputItem.code = Integer.parseInt(object[0]);
				inputItem.p = Integer.parseInt(object[1]) / 100;
				inputItem.q = Integer.parseInt(object[2]) / 100;
				inputItem.r = Integer.parseInt(object[3]) / 100;
				inputItem.weight = Double.parseDouble(object[4]);
				inputItem.packageMaterial = object[5];
				inputItem.orientationConstrained = object[6];
				inputItem.product = object[7];
				minDimension = Math.min(minDimension, Math.min(inputItem.p, Math.min(inputItem.q, inputItem.r)));
				Main.inputItems.add(inputItem);

				if ((inputItem.p != inputItem.q) && (inputItem.p != inputItem.r) && (inputItem.q != inputItem.r)) {
					InputItem i = new InputItem();
					i.code = inputItem.code;
					i.p = inputItem.q;
					i.q = inputItem.p;
					i.r = inputItem.r;
					i.weight = inputItem.weight;
					i.packageMaterial = inputItem.packageMaterial;
					i.orientationConstrained = inputItem.orientationConstrained;
					i.product = inputItem.product;
					Main.inputItems.add(i);

					i = new InputItem();
					i.code = inputItem.code;
					i.p = inputItem.r;
					i.q = inputItem.q;
					i.r = inputItem.p;
					i.weight = inputItem.weight;
					i.packageMaterial = inputItem.packageMaterial;
					i.orientationConstrained = inputItem.orientationConstrained;
					i.product = inputItem.product;
					Main.inputItems.add(i);

					i = new InputItem();
					i.code = inputItem.code;
					i.p = inputItem.q;
					i.q = inputItem.r;
					i.r = inputItem.p;
					i.weight = inputItem.weight;
					i.packageMaterial = inputItem.packageMaterial;
					i.orientationConstrained = inputItem.orientationConstrained;
					i.product = inputItem.product;
					Main.inputItems.add(i);

					i = new InputItem();
					i.code = inputItem.code;
					i.p = inputItem.p;
					i.q = inputItem.r;
					i.r = inputItem.q;
					i.weight = inputItem.weight;
					i.packageMaterial = inputItem.packageMaterial;
					i.orientationConstrained = inputItem.orientationConstrained;
					i.product = inputItem.product;
					Main.inputItems.add(i);

					i = new InputItem();
					i.code = inputItem.code;
					i.p = inputItem.r;
					i.q = inputItem.p;
					i.r = inputItem.q;
					i.weight = inputItem.weight;
					i.packageMaterial = inputItem.packageMaterial;
					i.orientationConstrained = inputItem.orientationConstrained;
					i.product = inputItem.product;
					Main.inputItems.add(i);
				}

				if ((inputItem.p != inputItem.q) && (inputItem.p != inputItem.r) && (inputItem.q == inputItem.r)) {
					InputItem i = new InputItem();
					i.code = inputItem.code;
					i.p = inputItem.q;
					i.q = inputItem.p;
					i.r = inputItem.r;
					i.weight = inputItem.weight;
					i.packageMaterial = inputItem.packageMaterial;
					i.orientationConstrained = inputItem.orientationConstrained;
					i.product = inputItem.product;
					Main.inputItems.add(i);

					i = new InputItem();
					i.code = inputItem.code;
					i.p = inputItem.q;
					i.q = inputItem.r;
					i.r = inputItem.p;
					i.weight = inputItem.weight;
					i.packageMaterial = inputItem.packageMaterial;
					i.orientationConstrained = inputItem.orientationConstrained;
					i.product = inputItem.product;
					Main.inputItems.add(i);

				}

				if ((inputItem.p != inputItem.q) && (inputItem.p == inputItem.r) && (inputItem.q != inputItem.r)) {
					InputItem i = new InputItem();
					i.code = inputItem.code;
					i.p = inputItem.q;
					i.q = inputItem.p;
					i.r = inputItem.r;
					i.weight = inputItem.weight;
					i.packageMaterial = inputItem.packageMaterial;
					i.orientationConstrained = inputItem.orientationConstrained;
					i.product = inputItem.product;
					Main.inputItems.add(i);

					i = new InputItem();
					i.code = inputItem.code;
					i.p = inputItem.p;
					i.q = inputItem.r;
					i.r = inputItem.q;
					i.weight = inputItem.weight;
					i.packageMaterial = inputItem.packageMaterial;
					i.orientationConstrained = inputItem.orientationConstrained;
					i.product = inputItem.product;
					Main.inputItems.add(i);

				}

				if ((inputItem.p == inputItem.q) && (inputItem.p != inputItem.r) && (inputItem.q != inputItem.r)) {
					InputItem i = new InputItem();
					i.code = inputItem.code;
					i.p = inputItem.r;
					i.q = inputItem.q;
					i.r = inputItem.p;
					i.weight = inputItem.weight;
					i.packageMaterial = inputItem.packageMaterial;
					i.orientationConstrained = inputItem.orientationConstrained;
					i.product = inputItem.product;
					Main.inputItems.add(i);

					i = new InputItem();
					i.code = inputItem.code;
					i.p = inputItem.p;
					i.q = inputItem.r;
					i.r = inputItem.q;
					i.weight = inputItem.weight;
					i.packageMaterial = inputItem.packageMaterial;
					i.orientationConstrained = inputItem.orientationConstrained;
					i.product = inputItem.product;
					Main.inputItems.add(i);

				}
			}
			
			Main.size = cont;


			input = new Scanner(new FileReader(binInput));
			input.nextLine();

			Main.inputBins = new ArrayList<InputBin>();

			int bin_max_dimension = 0;
			while (input.hasNextLine()) {
				object = input.nextLine().split("\\;");
				InputBin bin = new InputBin();
				bin.type = object[0];
				bin.length = Integer.parseInt(object[1]) / 100;
				bin.width = Integer.parseInt(object[2]) / 100;
				bin.height = Integer.parseInt(object[3]) / 100;
				bin.maximalWeight = Integer.parseInt(object[4]);
				bin_max_dimension = Math.max(bin_max_dimension, Math.max(bin.length, Math.max(bin.width, bin.height)));
				Main.inputBins.add(bin);
			}
			Main.bin_max_dimension = bin_max_dimension;

			input.close();

		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}

	public void writeSolution(ArrayList<InputItem> inputItems, ArrayList<Item> items, ArrayList<Row> rows,
			ArrayList<Layer> layers, ArrayList<Stack> stacks, ArrayList<Bin> bins, String method) {

		Path p1 = Paths.get(method + File.separator + instance);
		File parametersOut = new File(p1 + File.separator + "parameters.txt");
		File inputBinsOut = new File(p1 + File.separator + "input_bin.csv");
		File inputItemsOut = new File(p1 + File.separator + "input_items.csv");
		File itemsOut = new File(p1 + File.separator + "items.csv");
		File rowsOut = new File(p1 + File.separator + "rows.csv");
		File layersOut = new File(p1 + File.separator + "layers.csv");
		File stacksOut = new File(p1 + File.separator + "stacks.csv");
		File binsOut = new File(p1 + File.separator + "bins.csv");
	
		FileWriter parametersWriter = null;
		FileWriter inputBinsWriter = null;
		FileWriter inputItemsWriter = null;
		FileWriter itemsWriter = null;
		FileWriter rowsWriter = null;
		FileWriter layersWriter = null;
		FileWriter stacksWriter = null;
		FileWriter binsWriter = null;
	
		StringBuilder sb;

		try {
			Files.createDirectories(p1);
			
			sb = new StringBuilder();
			parametersWriter = new FileWriter(parametersOut);
			sb.append("tolerance_item_product_demands=1.0\r\n" + 
					"maximum_density_allowed_kg=1\r\n" + 
					"max_number_of_rows_in_a_layer=1000\r\n" + 
					"max_number_of_items_in_a_row=1000\r\n" + 
					"layer_size_deviation_tolerance=1.0\r\n" + 
					"row_size_deviation_tolerance=1.0\r\n" + 
					"item_size_deviation_tolerance=1.0\r\n" + 
					"maximum_weigth_above_item_kg=1\r\n" + 
					"");

			parametersWriter.append(sb.toString());
			
			sb = new StringBuilder();
			inputBinsWriter = new FileWriter(inputBinsOut);
			sb.append("bin type;length (mm);width (mm);height (mm);maximal weight (kg);\n");

			for (InputBin inputBin : Main.inputBins) {
				sb.append(inputBin.toString());
				sb.append("\n");
			}

			inputBinsWriter.append(sb.toString());
			
			sb = new StringBuilder();
			inputItemsWriter = new FileWriter(inputItemsOut);
			sb.append("item code;length (mm);width (mm);height (mm);weight (kg);Package material;Orientation constrained;Product;\n");

			for (InputItem item : inputItems) {
				sb.append(item.toString());
				sb.append("\n");
			}

			inputItemsWriter.append(sb.toString());

			sb = new StringBuilder();
			itemsWriter = new FileWriter(itemsOut);
			sb.append("id item;id row;xO;yO;zO;xE;yE;zE\n");

			for (Item item : items) {
				sb.append(item.toString());
				sb.append("\n");
			}

			itemsWriter.append(sb.toString());
			
			sb = new StringBuilder();
			rowsWriter = new FileWriter(rowsOut);
			sb.append("id row;id layer;xO;yO;zO;xE;yE;zE\n");

			for (Row row : rows) {
				sb.append(row.toString());
				sb.append("\n");
			}

			rowsWriter.append(sb.toString());
			
			sb = new StringBuilder();
			layersWriter = new FileWriter(layersOut);
			sb.append("id layer;id stack;xO;yO;zO;xE;yE;zE;flag top\n");

			for (Layer layer : layers) {
				sb.append(layer.toString());
				sb.append("\n");
			}

			layersWriter.append(sb.toString());
			

			sb = new StringBuilder();
			stacksWriter = new FileWriter(stacksOut);
			sb.append("id stack;id bin;xO;yO;zO;xE;yE;zE\n");

			for (Stack stack : stacks) {
				sb.append(stack.toString());
				sb.append("\n");
			}

			stacksWriter.append(sb.toString());
			
			sb = new StringBuilder();
			binsWriter = new FileWriter(binsOut);
			sb.append("id bin;bin type\n");

			for (Bin bin : bins) {
				sb.append(bin.toString());
				sb.append("\n");
			}

			binsWriter.append(sb.toString());
			
		} catch (Exception e) {
			System.out.println("Error in CsvFileWriter.");
			e.printStackTrace();
		} finally {
			try {
				parametersWriter.flush();
				parametersWriter.close();
				inputBinsWriter.flush();
				inputBinsWriter.close();
				inputItemsWriter.flush();
				inputItemsWriter.close();
				itemsWriter.flush();
				itemsWriter.close();
				rowsWriter.flush();
				rowsWriter.close();
				layersWriter.flush();
				layersWriter.close();
				stacksWriter.flush();
				stacksWriter.close();
				binsWriter.flush();
				binsWriter.close();
			} catch (IOException e) {
				System.out.println("Error while flushing/closing fileWriter.");
				e.printStackTrace();
			}
		}
	}

	public void writeResult(String method,int size, int numBins, int numBinsModel, double objectiveValue, double gap, double runtime) {

		FileWriter fileWriter = null;
		File file = new File("teste.csv");
		StringBuilder sb = new StringBuilder();

		try {
			if (file.exists() && !file.isDirectory()) {
				fileWriter = new FileWriter(file, true);
				sb.append(instance + "," + method + "," + size + "," + numBins + "," + numBinsModel + "," + objectiveValue +  "," + gap + "," + runtime + "\n");
				fileWriter.append(sb.toString());

			} else {
				fileWriter = new FileWriter(file);
				sb.append("INSTANCIA,MODELO,TAMANHO INSTANCIA,NUMERO DE BINS,NUMERO DE BINS MODELO,FUNCAO OBJETIVO,GAP,TEMPO DE EXECUCAO\n");
				sb.append(instance + "," + method + "," + size + "," + numBins + "," + numBinsModel + "," + objectiveValue +  "," + gap + "," + runtime + "\n");
				fileWriter.append(sb.toString());
			}

		} catch (Exception e) {
			System.out.println("Error in CsvFileWriter !!!");
			e.printStackTrace();
		} finally {

			try {
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e) {
				System.out.println("Error while flushing/closing fileWriter !!!");
				e.printStackTrace();
			}

		}
	}
}
