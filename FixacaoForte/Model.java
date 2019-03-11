
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

import gurobi.GRB;
import gurobi.GRB.DoubleAttr;
import gurobi.GRBEnv;
import gurobi.GRBException;
import gurobi.GRBLinExpr;
import gurobi.GRBModel;
import gurobi.GRBVar;

public class Model {
	ArrayList<Item> items;
	ArrayList<Bin> bins;
	GRBVar[] n;
	GRBVar[] x;
	GRBVar[] y;
	GRBVar[] z;
	GRBVar[] lx;
	GRBVar[] ly;
	GRBVar[] lz;
	GRBVar[] wx;
	GRBVar[] wy;
	GRBVar[] wz;
	GRBVar[] hx;
	GRBVar[] hy;
	GRBVar[] hz;
	GRBVar[][] a;
	GRBVar[][] b;
	GRBVar[][] c;
	GRBVar[][] d;
	GRBVar[][] e;
	GRBVar[][] f;
	GRBVar[][] s;
	double[] nSol;
	double[] xSol;
	double[] ySol;
	double[] zSol;
	double[] lxSol;
	double[] lySol;
	double[] lzSol;
	double[] wxSol;
	double[] wySol;
	double[] wzSol;
	double[] hxSol;
	double[] hySol;
	double[] hzSol;
	double[][] aSol;
	double[][] bSol;
	double[][] cSol;
	double[][] dSol;
	double[][] eSol;
	double[][] fSol;
	double[][] sSol;
	double[][] aNew;
	double[][] bNew;
	double[][] cNew;
	double[][] dNew;
	double[][] eNew;
	double[][] fNew;
	double[][] sNew;
	double time;
	String instance;
	String method;
	long somatorio;

	public void fixacaoForte(ArrayList<Item> items, ArrayList<Bin> bins, String instance, String method, double time) {

		this.items = items;
		this.bins = bins;
		this.instance = instance;
		this.time = time;
		this.method = method;

		try {

			startVariables();
			formGroups();

			GRBEnv env = new GRBEnv();
			GRBModel modelo = new GRBModel(env);

			modelo.set(GRB.DoubleParam.TimeLimit, time);
			modelo.set(GRB.StringAttr.ModelName, "clp");
			modelo.set(GRB.IntAttr.ModelSense, GRB.MINIMIZE);

			GRBLinExpr sum = new GRBLinExpr();
			sum.addConstant(-somatorio);
			modelo.setObjective(sum);

			for (int j = 0; j < bins.size(); j++)
				n[j] = modelo.addVar(0, 1, bins.get(j).volume, GRB.BINARY, "n_" + j);

			for (int i = 0; i < items.size(); i++) {
				x[i] = modelo.addVar(0, bins.get(0).L, 0, GRB.INTEGER, "x_" + i);
				y[i] = modelo.addVar(0, bins.get(0).W, 0, GRB.INTEGER, "y_" + i);
				z[i] = modelo.addVar(0, bins.get(0).H, 0, GRB.INTEGER, "z_" + i);
				lx[i] = modelo.addVar(0, 1, 0, GRB.BINARY, "lx_" + i);
				ly[i] = modelo.addVar(0, 1, 0, GRB.BINARY, "ly_" + i);
				lz[i] = modelo.addVar(0, 1, 0, GRB.BINARY, "lz_" + i);
				wx[i] = modelo.addVar(0, 1, 0, GRB.BINARY, "wx_" + i);
				wy[i] = modelo.addVar(0, 1, 0, GRB.BINARY, "wy_" + i);
				wz[i] = modelo.addVar(0, 1, 0, GRB.BINARY, "wz_" + i);
				hx[i] = modelo.addVar(0, 1, 0, GRB.BINARY, "hx_" + i);
				hy[i] = modelo.addVar(0, 1, 0, GRB.BINARY, "hy_" + i);
				hz[i] = modelo.addVar(0, 1, 0, GRB.BINARY, "hz_" + i);
			}

			modelo.set(GRB.DoubleAttr.Start, n, nSol);
			modelo.set(GRB.DoubleAttr.Start, x, xSol);
			modelo.set(GRB.DoubleAttr.Start, y, ySol);
			modelo.set(GRB.DoubleAttr.Start, z, zSol);
			modelo.set(GRB.DoubleAttr.Start, lx, lxSol);
			modelo.set(GRB.DoubleAttr.Start, ly, lySol);
			modelo.set(GRB.DoubleAttr.Start, lz, lzSol);
			modelo.set(GRB.DoubleAttr.Start, wx, wxSol);
			modelo.set(GRB.DoubleAttr.Start, wy, wySol);
			modelo.set(GRB.DoubleAttr.Start, wz, wzSol);
			modelo.set(GRB.DoubleAttr.Start, hx, hxSol);
			modelo.set(GRB.DoubleAttr.Start, hy, hySol);
			modelo.set(GRB.DoubleAttr.Start, hz, hzSol);

			for (int i = 0; i < items.size(); i++) {
				for (int k = i + 1; k < items.size(); k++) {
					if (aNew[i][k] != -1) {
						a[i][k] = modelo.addVar(aNew[i][k], aNew[i][k], 0, GRB.BINARY, "a_" + i + "_" + k);
					} else {
						a[i][k] = modelo.addVar(0, 1, 0, GRB.BINARY, "a_" + i + "_" + k);
					}

					if (bNew[i][k] != -1) {
						b[i][k] = modelo.addVar(bNew[i][k], bNew[i][k], 0, GRB.BINARY, "b_" + i + "_" + k);
					} else {
						b[i][k] = modelo.addVar(0, 1, 0, GRB.BINARY, "b_" + i + "_" + k);
					}

					if (cNew[i][k] != -1) {
						c[i][k] = modelo.addVar(cNew[i][k], cNew[i][k], 0, GRB.BINARY, "c_" + i + "_" + k);
					} else {
						c[i][k] = modelo.addVar(0, 1, 0, GRB.BINARY, "c_" + i + "_" + k);
					}

					if (dNew[i][k] != -1) {
						d[i][k] = modelo.addVar(dNew[i][k], dNew[i][k], 0, GRB.BINARY, "d_" + i + "_" + k);
					} else {
						d[i][k] = modelo.addVar(0, 1, 0, GRB.BINARY, "d_" + i + "_" + k);
					}

					if (eNew[i][k] != -1) {
						e[i][k] = modelo.addVar(eNew[i][k], eNew[i][k], 0, GRB.BINARY, "e_" + i + "_" + k);
					} else {
						e[i][k] = modelo.addVar(0, 1, 0, GRB.BINARY, "e_" + i + "_" + k);
					}

					if (fNew[i][k] != -1) {
						f[i][k] = modelo.addVar(fNew[i][k], fNew[i][k], 0, GRB.BINARY, "f_" + i + "_" + k);
					} else {
						f[i][k] = modelo.addVar(0, 1, 0, GRB.BINARY, "f_" + i + "_" + k);
					}
					a[i][k].set(GRB.DoubleAttr.Start, aSol[i][k]);
					b[i][k].set(GRB.DoubleAttr.Start, bSol[i][k]);
					c[i][k].set(GRB.DoubleAttr.Start, cSol[i][k]);
					d[i][k].set(GRB.DoubleAttr.Start, dSol[i][k]);
					e[i][k].set(GRB.DoubleAttr.Start, eSol[i][k]);
					f[i][k].set(GRB.DoubleAttr.Start, fSol[i][k]);
				}
			}

			for (int i = 0; i < items.size(); i++)
				for (int j = 0; j < bins.size(); j++) {
					if (sNew[i][j] != -1) {
						s[i][j] = modelo.addVar(sNew[i][j], sNew[i][j], 0, GRB.BINARY, "s_" + i + "_" + j);
					} else {
						s[i][j] = modelo.addVar(0, 1, 0, GRB.BINARY, "s_" + i + "_" + j);
					}
					s[i][j].set(GRB.DoubleAttr.Start, sSol[i][j]);
				}

			for (int i = 0; i < items.size(); i++) {
				for (int k = i + 1; k < items.size(); k++) {

					// restricao (1)

					GRBLinExpr lhs = new GRBLinExpr();
					lhs.addTerm(1, x[i]);
					lhs.addTerm(items.get(i).p, lx[i]);
					lhs.addTerm(items.get(i).q, wx[i]);
					lhs.addTerm(items.get(i).r, hx[i]);

					GRBLinExpr rhs = new GRBLinExpr();
					rhs.addTerm(1, x[k]);
					rhs.addConstant(bins.get(0).L);
					rhs.addTerm(-bins.get(0).L, a[i][k]);

					modelo.addConstr(lhs, GRB.LESS_EQUAL, rhs, "left_" + i + "_" + k);

					// restricao (2)

					lhs = new GRBLinExpr();
					lhs.addTerm(1, x[k]);
					lhs.addTerm(items.get(k).p, lx[k]);
					lhs.addTerm(items.get(k).q, wx[k]);
					lhs.addTerm(items.get(k).r, hx[k]);

					rhs = new GRBLinExpr();
					rhs.addTerm(1, x[i]);
					rhs.addConstant(bins.get(0).L);
					rhs.addTerm(-bins.get(0).L, b[i][k]);

					modelo.addConstr(lhs, GRB.LESS_EQUAL, rhs, "right_" + i + "_" + k);

					// restricao (3)

					lhs = new GRBLinExpr();
					lhs.addTerm(1, y[i]);
					lhs.addTerm(items.get(i).q, wy[i]);
					lhs.addTerm(items.get(i).p, ly[i]);
					lhs.addTerm(items.get(i).r, hy[i]);

					rhs = new GRBLinExpr();
					rhs.addTerm(1, y[k]);
					rhs.addConstant(bins.get(0).W);
					rhs.addTerm(-bins.get(0).W, c[i][k]);

					modelo.addConstr(lhs, GRB.LESS_EQUAL, rhs, "behind_" + i + "_" + k);

					// restricao (4)

					lhs = new GRBLinExpr();
					lhs.addTerm(1, y[k]);
					lhs.addTerm(items.get(k).q, wy[k]);
					lhs.addTerm(items.get(k).p, ly[k]);
					lhs.addTerm(items.get(k).r, hy[k]);

					rhs = new GRBLinExpr();
					rhs.addTerm(1, y[i]);
					rhs.addConstant(bins.get(0).W);
					rhs.addTerm(-bins.get(0).W, d[i][k]);

					modelo.addConstr(lhs, GRB.LESS_EQUAL, rhs, "front_" + i + "_" + k);

					// restricao (5)

					lhs = new GRBLinExpr();
					lhs.addTerm(1, z[i]);
					lhs.addTerm(items.get(i).r, hz[i]);
					lhs.addTerm(items.get(i).q, wz[i]);
					lhs.addTerm(items.get(i).p, lz[i]);

					rhs = new GRBLinExpr();
					rhs.addTerm(1, z[k]);
					rhs.addConstant(bins.get(0).H);
					rhs.addTerm(-bins.get(0).H, e[i][k]);

					modelo.addConstr(lhs, GRB.LESS_EQUAL, rhs, "under_" + i + "_" + k);

					// restricao (6)

					lhs = new GRBLinExpr();
					lhs.addTerm(1, z[k]);
					lhs.addTerm(items.get(k).r, hz[k]);
					lhs.addTerm(items.get(k).q, wz[k]);
					lhs.addTerm(items.get(k).p, lz[k]);

					rhs = new GRBLinExpr();
					rhs.addTerm(1, z[i]);
					rhs.addConstant(bins.get(0).H);
					rhs.addTerm(-bins.get(0).H, f[i][k]);

					modelo.addConstr(lhs, GRB.LESS_EQUAL, rhs, "above_" + i + "_" + k);
				}
			}

			for (int i = 0; i < items.size(); i++) {

				// just one rotation

				GRBLinExpr lhs = new GRBLinExpr();
				lhs.addTerm(1, lx[i]);
				lhs.addTerm(1, ly[i]);
				lhs.addTerm(1, lz[i]);
				GRBLinExpr rhs = new GRBLinExpr();
				rhs.addConstant(1);

				modelo.addConstr(lhs, GRB.EQUAL, rhs, "rotationL_" + i);

				lhs = new GRBLinExpr();
				lhs.addTerm(1, wx[i]);
				lhs.addTerm(1, wy[i]);
				lhs.addTerm(1, wz[i]);
				rhs = new GRBLinExpr();
				rhs.addConstant(1);

				modelo.addConstr(lhs, GRB.EQUAL, rhs, "rotationW_" + i);

				lhs = new GRBLinExpr();
				lhs.addTerm(1, hx[i]);
				lhs.addTerm(1, hy[i]);
				lhs.addTerm(1, hz[i]);
				rhs = new GRBLinExpr();
				rhs.addConstant(1);

				modelo.addConstr(lhs, GRB.EQUAL, rhs, "rotationH_" + i);

				lhs = new GRBLinExpr();
				lhs.addTerm(1, lx[i]);
				lhs.addTerm(1, wx[i]);
				lhs.addTerm(1, hx[i]);
				rhs = new GRBLinExpr();
				rhs.addConstant(1);

				modelo.addConstr(lhs, GRB.EQUAL, rhs, "rotationX_" + i);

				lhs = new GRBLinExpr();
				lhs.addTerm(1, ly[i]);
				lhs.addTerm(1, wy[i]);
				lhs.addTerm(1, hy[i]);
				rhs = new GRBLinExpr();
				rhs.addConstant(1);

				modelo.addConstr(lhs, GRB.EQUAL, rhs, "rotationY_" + i);

				lhs = new GRBLinExpr();
				lhs.addTerm(1, lz[i]);
				lhs.addTerm(1, wz[i]);
				lhs.addTerm(1, hz[i]);
				rhs = new GRBLinExpr();
				rhs.addConstant(1);

				modelo.addConstr(lhs, GRB.EQUAL, rhs, "rotationZ_" + i);

			}

			for (int j = 0; j < bins.size(); j++)
				for (int i = 0; i < items.size(); i++)
					for (int k = i + 1; k < items.size(); k++) {

						// restricao (7)

						GRBLinExpr lhs = new GRBLinExpr();
						lhs.addTerm(1, a[i][k]);
						lhs.addTerm(1, b[i][k]);
						lhs.addTerm(1, c[i][k]);
						lhs.addTerm(1, d[i][k]);
						lhs.addTerm(1, e[i][k]);
						lhs.addTerm(1, f[i][k]);

						GRBLinExpr rhs = new GRBLinExpr();
						rhs.addTerm(1, s[i][j]);
						rhs.addTerm(1, s[k][j]);
						rhs.addConstant(-1);

						modelo.addConstr(lhs, GRB.GREATER_EQUAL, rhs, "overlap_" + i + "_" + k + "_" + j);
					}

			for (int i = 0; i < items.size(); i++) {

				// restricao (8)

				GRBLinExpr lhs = new GRBLinExpr();
				for (int j = 0; j < bins.size(); j++)
					lhs.addTerm(1, s[i][j]);

				GRBLinExpr rhs = new GRBLinExpr();
				rhs.addConstant(1);

				modelo.addConstr(lhs, GRB.EQUAL, rhs, "placement_" + i);
			}

			for (int i = 0; i < items.size(); i++) {
				for (int j = 0; j < bins.size(); j++) {

					// restricao (9)

					GRBLinExpr lhs = new GRBLinExpr();
					lhs.addTerm(1, s[i][j]);

					GRBLinExpr rhs = new GRBLinExpr();
					rhs.addTerm(1, n[j]);

					modelo.addConstr(lhs, GRB.LESS_EQUAL, rhs, "bin_used_" + i + "_" + j);

					// restricao (10)

					double maxDimension = Main.bin_max_dimension;

					lhs = new GRBLinExpr();
					lhs.addTerm(1, x[i]);
					lhs.addTerm(items.get(i).p, lx[i]);
					lhs.addTerm(items.get(i).q, wx[i]);
					lhs.addTerm(items.get(i).r, hx[i]);

					rhs = new GRBLinExpr();
					rhs.addConstant(bins.get(j).L);
					rhs.addConstant(maxDimension);
					rhs.addTerm(-maxDimension, s[i][j]);

					modelo.addConstr(lhs, GRB.LESS_EQUAL, rhs, "lenght_" + i + "_" + j);

					// restricao (11)

					lhs = new GRBLinExpr();
					lhs.addTerm(1, y[i]);
					lhs.addTerm(items.get(i).q, wy[i]);
					lhs.addTerm(items.get(i).p, ly[i]);
					lhs.addTerm(items.get(i).r, hy[i]);

					rhs = new GRBLinExpr();
					rhs.addConstant(bins.get(j).W);
					rhs.addConstant(maxDimension);
					rhs.addTerm(-maxDimension, s[i][j]);

					modelo.addConstr(lhs, GRB.LESS_EQUAL, rhs, "depth_" + i + "_" + j);

					// restricao (12)

					lhs = new GRBLinExpr();
					lhs.addTerm(1, z[i]);
					lhs.addTerm(items.get(i).r, hz[i]);
					lhs.addTerm(items.get(i).q, wz[i]);
					lhs.addTerm(items.get(i).p, lz[i]);

					rhs = new GRBLinExpr();
					rhs.addConstant(bins.get(j).H);
					rhs.addConstant(maxDimension);
					rhs.addTerm(-maxDimension, s[i][j]);

					modelo.addConstr(lhs, GRB.LESS_EQUAL, rhs, "height_" + i + "_" + j);

				}
			}

			for (int i = 0; i < items.size(); i++) {
				int soma = 0;
				for (int k = i + 1; k < items.size(); k++) {
					if (aNew[i][k] == 1 || bNew[i][k] == 1 || cNew[i][k] == 1 || dNew[i][k] == 1 || eNew[i][k] == 1
							|| fNew[i][k] == 1) {
						soma++;
					}
				}
				if (soma > 0) {
					GRBLinExpr lhs = new GRBLinExpr();
					GRBLinExpr rhs = new GRBLinExpr();
					lhs.addTerm(1, s[i][items.get(i).binId]);
					rhs.addConstant(1);
					modelo.addConstr(lhs, GRB.EQUAL, rhs, "s_" + i + "" + items.get(i).binId);
				}
			}

			modelo.optimize();

			// atualiza solução se valor encontrado for melhor

			Main.runtime = modelo.get(DoubleAttr.Runtime);
			Main.objectiveValue = modelo.get(DoubleAttr.ObjVal);
			Main.gap = modelo.get(DoubleAttr.MIPGap);

			getSolution(x, y, z, lx, ly, lz, wx, wy, wz, hx, hy, hz, n, s);
			// writeValues();

			// volta para os valores iniciais

			modelo.dispose();
			env.dispose();

		} catch (GRBException e) {
			System.out.println("Error code: " + e.getErrorCode() + ". " + e.getMessage());
		}
	}

	public void getSolution(GRBVar[] x, GRBVar[] y, GRBVar[] z, GRBVar[] lx, GRBVar[] ly, GRBVar[] lz, GRBVar[] wx,
			GRBVar[] wy, GRBVar[] wz, GRBVar[] hx, GRBVar[] hy, GRBVar[] hz, GRBVar[] n, GRBVar[][] s) {

		Main.itemsModel = new ArrayList<Item>();
		Main.rowsModel = new ArrayList<Row>();
		Main.layersModel = new ArrayList<Layer>();
		Main.stacksModel = new ArrayList<Stack>();
		Main.binsModel = new ArrayList<Bin>();
		Main.inputItemsModel = new ArrayList<InputItem>();

		try {

			int numBins = 0;
			for (int j = 0; j < n.length; j++) {
				if (n[j].get(GRB.DoubleAttr.X) == 1) {
					numBins++;
				}
				Main.binsModel.add(
						new Bin(j, bins.get(j).type, bins.get(j).L, bins.get(j).W, bins.get(j).H, bins.get(j).weight));
			}

			Main.numBinsModel = numBins;

			for (int i = 0; i < items.size(); i++) {

				InputItem inputItem = new InputItem();
				inputItem.code = items.get(i).itemId;
				if (items.get(i).p >= items.get(i).q) {
					inputItem.p = items.get(i).p * 100;
					inputItem.q = items.get(i).q * 100;
				} else {
					inputItem.p = items.get(i).q * 100;
					inputItem.q = items.get(i).p * 100;
				}
				inputItem.r = items.get(i).r * 100;
				inputItem.weight = 0;
				inputItem.packageMaterial = "METAL";
				inputItem.orientationConstrained = "";
				inputItem.product = "1";
				Main.inputItemsModel.add(inputItem);

				for (int j = 0; j < bins.size(); j++) {
					if (s[i][j].get(GRB.DoubleAttr.X) == 1.0) {
						Item item = new Item(i, i, (int) x[i].get(GRB.DoubleAttr.X) * 100,
								(int) y[i].get(GRB.DoubleAttr.X) * 100, (int) z[i].get(GRB.DoubleAttr.X) * 100,
								(int) x[i].get(GRB.DoubleAttr.X) * 100
										+ items.get(i).p * 100 * (int) lx[i].get(GRB.DoubleAttr.X)
										+ items.get(i).q * 100 * (int) wx[i].get(GRB.DoubleAttr.X)
										+ items.get(i).r * 100 * (int) hx[i].get(GRB.DoubleAttr.X),
								(int) y[i].get(GRB.DoubleAttr.X) * 100
										+ items.get(i).p * 100 * (int) ly[i].get(GRB.DoubleAttr.X)
										+ items.get(i).q * 100 * (int) wy[i].get(GRB.DoubleAttr.X)
										+ items.get(i).r * 100 * (int) hy[i].get(GRB.DoubleAttr.X),
								(int) z[i].get(GRB.DoubleAttr.X) * 100
										+ items.get(i).p * 100 * (int) lz[i].get(GRB.DoubleAttr.X)
										+ items.get(i).q * 100 * (int) wz[i].get(GRB.DoubleAttr.X)
										+ items.get(i).r * 100 * (int) hz[i].get(GRB.DoubleAttr.X));
						item.binId = j;
						Main.itemsModel.add(item);
						Main.binsModel.get(j).items.add(item);

						Main.rowsModel.add(new Row(i, i, (int) x[i].get(GRB.DoubleAttr.X) * 100,
								(int) y[i].get(GRB.DoubleAttr.X) * 100, (int) z[i].get(GRB.DoubleAttr.X) * 100,
								(int) x[i].get(GRB.DoubleAttr.X) * 100
										+ items.get(i).p * 100 * (int) lx[i].get(GRB.DoubleAttr.X)
										+ items.get(i).q * 100 * (int) wx[i].get(GRB.DoubleAttr.X)
										+ items.get(i).r * 100 * (int) hx[i].get(GRB.DoubleAttr.X),
								(int) y[i].get(GRB.DoubleAttr.X) * 100
										+ items.get(i).p * 100 * (int) ly[i].get(GRB.DoubleAttr.X)
										+ items.get(i).q * 100 * (int) wy[i].get(GRB.DoubleAttr.X)
										+ items.get(i).r * 100 * (int) hy[i].get(GRB.DoubleAttr.X),
								(int) z[i].get(GRB.DoubleAttr.X) * 100
										+ items.get(i).p * 100 * (int) lz[i].get(GRB.DoubleAttr.X)
										+ items.get(i).q * 100 * (int) wz[i].get(GRB.DoubleAttr.X)
										+ items.get(i).r * 100 * (int) hz[i].get(GRB.DoubleAttr.X)));
						Main.layersModel.add(new Layer(i, i, (int) x[i].get(GRB.DoubleAttr.X) * 100,
								(int) y[i].get(GRB.DoubleAttr.X) * 100, (int) z[i].get(GRB.DoubleAttr.X) * 100,
								(int) x[i].get(GRB.DoubleAttr.X) * 100
										+ items.get(i).p * 100 * (int) lx[i].get(GRB.DoubleAttr.X)
										+ items.get(i).q * 100 * (int) wx[i].get(GRB.DoubleAttr.X)
										+ items.get(i).r * 100 * (int) hx[i].get(GRB.DoubleAttr.X),
								(int) y[i].get(GRB.DoubleAttr.X) * 100
										+ items.get(i).p * 100 * (int) ly[i].get(GRB.DoubleAttr.X)
										+ items.get(i).q * 100 * (int) wy[i].get(GRB.DoubleAttr.X)
										+ items.get(i).r * 100 * (int) hy[i].get(GRB.DoubleAttr.X),
								(int) z[i].get(GRB.DoubleAttr.X) * 100
										+ items.get(i).p * 100 * (int) lz[i].get(GRB.DoubleAttr.X)
										+ items.get(i).q * 100 * (int) wz[i].get(GRB.DoubleAttr.X)
										+ items.get(i).r * 100 * (int) hz[i].get(GRB.DoubleAttr.X),
								0));
						Main.stacksModel.add(new Stack(i, j, (int) x[i].get(GRB.DoubleAttr.X) * 100,
								(int) y[i].get(GRB.DoubleAttr.X) * 100, (int) z[i].get(GRB.DoubleAttr.X) * 100,
								(int) x[i].get(GRB.DoubleAttr.X) * 100
										+ items.get(i).p * 100 * (int) lx[i].get(GRB.DoubleAttr.X)
										+ items.get(i).q * 100 * (int) wx[i].get(GRB.DoubleAttr.X)
										+ items.get(i).r * 100 * (int) hx[i].get(GRB.DoubleAttr.X),
								(int) y[i].get(GRB.DoubleAttr.X) * 100
										+ items.get(i).p * 100 * (int) ly[i].get(GRB.DoubleAttr.X)
										+ items.get(i).q * 100 * (int) wy[i].get(GRB.DoubleAttr.X)
										+ items.get(i).r * 100 * (int) hy[i].get(GRB.DoubleAttr.X),
								(int) z[i].get(GRB.DoubleAttr.X) * 100
										+ items.get(i).p * 100 * (int) lz[i].get(GRB.DoubleAttr.X)
										+ items.get(i).q * 100 * (int) wz[i].get(GRB.DoubleAttr.X)
										+ items.get(i).r * 100 * (int) hz[i].get(GRB.DoubleAttr.X)));
					}
				}
			}

		} catch (GRBException e) {
			System.out.println("Error code: " + e.getErrorCode() + ". " + e.getMessage());
		}
	}

	public void writeValues() {

		Path p1 = Paths.get(method + File.separator + instance);

		File nOutput = new File(p1 + File.separator + "variables.csv");
		FileWriter fileWriter = null;
		StringBuilder sb = new StringBuilder();
		try {
			Files.createDirectories(p1);

			fileWriter = new FileWriter(nOutput);

			sb.append("n_j");
			sb.append(",");
			for (int j = 0; j < n.length; j++) {
				sb.append(j);
				sb.append(",");
			}

			sb.append("\n");

			sb.append(" ");
			sb.append(",");

			for (int j = 0; j < n.length; j++) {
				sb.append(n[j].get(GRB.DoubleAttr.X));
				sb.append(",");
			}

			sb.append("\n");

			sb.append("s_ij");
			sb.append(",");
			for (int j = 0; j < n.length; j++) {
				sb.append(j);
				sb.append(",");
			}

			sb.append("\n");
			for (int i = 0; i < x.length; i++) {
				sb.append(i);
				sb.append(",");
				for (int j = 0; j < s[i].length; j++) {
					sb.append(s[i][j].get(GRB.DoubleAttr.X));
					sb.append(",");
				}
				sb.append("\n");
			}

			sb.append("i");
			sb.append(",");
			sb.append("x");
			sb.append(",");
			sb.append("y");
			sb.append(",");
			sb.append("z");
			sb.append(",");
			sb.append("lx");
			sb.append(",");
			sb.append("ly");
			sb.append(",");
			sb.append("lz");
			sb.append(",");
			sb.append("wx");
			sb.append(",");
			sb.append("wy");
			sb.append(",");
			sb.append("wz");
			sb.append(",");
			sb.append("hx");
			sb.append(",");
			sb.append("hy");
			sb.append(",");
			sb.append("hz");
			sb.append(",");
			sb.append("\n");

			for (int i = 0; i < x.length; i++) {
				sb.append(i);
				sb.append(",");
				sb.append(x[i].get(GRB.DoubleAttr.X));
				sb.append(",");
				sb.append(y[i].get(GRB.DoubleAttr.X));
				sb.append(",");
				sb.append(z[i].get(GRB.DoubleAttr.X));
				sb.append(",");
				sb.append(lx[i].get(GRB.DoubleAttr.X));
				sb.append(",");
				sb.append(ly[i].get(GRB.DoubleAttr.X));
				sb.append(",");
				sb.append(lz[i].get(GRB.DoubleAttr.X));
				sb.append(",");
				sb.append(wx[i].get(GRB.DoubleAttr.X));
				sb.append(",");
				sb.append(wy[i].get(GRB.DoubleAttr.X));
				sb.append(",");
				sb.append(wz[i].get(GRB.DoubleAttr.X));
				sb.append(",");
				sb.append(hx[i].get(GRB.DoubleAttr.X));
				sb.append(",");
				sb.append(hy[i].get(GRB.DoubleAttr.X));
				sb.append(",");
				sb.append(hz[i].get(GRB.DoubleAttr.X));
				sb.append("\n");
			}

			sb.append("a_ik");
			sb.append(",");
			for (int i = 0; i < a.length; i++) {
				sb.append(i);
				sb.append(",");
			}

			sb.append("\n");

			for (int i = 0; i < a.length; i++) {
				sb.append(i);
				sb.append(",");
				for (int k = 0; k < a.length; k++) {
					if (i < k) {
						sb.append(a[i][k].get(GRB.DoubleAttr.X));
						sb.append(",");
					} else {
						sb.append(" ");
						sb.append(",");
					}
				}
				sb.append("\n");
			}

			sb.append("b_ik");
			sb.append(",");
			for (int i = 0; i < b.length; i++) {
				sb.append(i);
				sb.append(",");
			}

			sb.append("\n");

			for (int i = 0; i < b.length; i++) {
				sb.append(i);
				sb.append(",");
				for (int k = 0; k < b.length; k++) {
					if (i < k) {
						sb.append(b[i][k].get(GRB.DoubleAttr.X));
						sb.append(",");
					} else {
						sb.append(" ");
						sb.append(",");
					}
				}
				sb.append("\n");
			}

			sb.append("c_ik");
			sb.append(",");
			for (int i = 0; i < c.length; i++) {
				sb.append(i);
				sb.append(",");
			}

			sb.append("\n");

			for (int i = 0; i < c.length; i++) {
				sb.append(i);
				sb.append(",");
				for (int k = 0; k < c.length; k++) {
					if (i < k) {
						sb.append(c[i][k].get(GRB.DoubleAttr.X));
						sb.append(",");
					} else {
						sb.append(" ");
						sb.append(",");
					}
				}
				sb.append("\n");
			}

			sb.append("d_ik");
			sb.append(",");
			for (int i = 0; i < d.length; i++) {
				sb.append(i);
				sb.append(",");
			}

			sb.append("\n");

			for (int i = 0; i < d.length; i++) {
				sb.append(i);
				sb.append(",");
				for (int k = 0; k < d.length; k++) {
					if (i < k) {
						sb.append(d[i][k].get(GRB.DoubleAttr.X));
						sb.append(",");
					} else {
						sb.append(" ");
						sb.append(",");
					}
				}
				sb.append("\n");
			}

			sb.append("e_ik");
			sb.append(",");
			for (int i = 0; i < e.length; i++) {
				sb.append(i);
				sb.append(",");
			}

			sb.append("\n");

			for (int i = 0; i < e.length; i++) {
				sb.append(i);
				sb.append(",");
				for (int k = 0; k < e.length; k++) {
					if (i < k) {
						sb.append(e[i][k].get(GRB.DoubleAttr.X));
						sb.append(",");
					} else {
						sb.append(" ");
						sb.append(",");
					}
				}
				sb.append("\n");
			}

			sb.append("f_ik");
			sb.append(",");
			for (int i = 0; i < f.length; i++) {
				sb.append(i);
				sb.append(",");
			}

			sb.append("\n");

			for (int i = 0; i < f.length; i++) {
				sb.append(i);
				sb.append(",");
				for (int k = 0; k < f.length; k++) {
					if (i < k) {
						sb.append(f[i][k].get(GRB.DoubleAttr.X));
						sb.append(",");
					} else {
						sb.append(" ");
						sb.append(",");
					}
				}
				sb.append("\n");
			}

			fileWriter.append(sb.toString());

		} catch (GRBException e) {
			System.out.println("Error code: " + e.getErrorCode() + ". " + e.getMessage());
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

	public void startVariables() {

		n = new GRBVar[bins.size()];
		x = new GRBVar[items.size()];
		y = new GRBVar[items.size()];
		z = new GRBVar[items.size()];
		lx = new GRBVar[items.size()];
		ly = new GRBVar[items.size()];
		lz = new GRBVar[items.size()];
		wx = new GRBVar[items.size()];
		wy = new GRBVar[items.size()];
		wz = new GRBVar[items.size()];
		hx = new GRBVar[items.size()];
		hy = new GRBVar[items.size()];
		hz = new GRBVar[items.size()];
		a = new GRBVar[items.size()][items.size()];
		b = new GRBVar[items.size()][items.size()];
		c = new GRBVar[items.size()][items.size()];
		d = new GRBVar[items.size()][items.size()];
		e = new GRBVar[items.size()][items.size()];
		f = new GRBVar[items.size()][items.size()];
		s = new GRBVar[items.size()][bins.size()];

		nSol = new double[bins.size()];
		xSol = new double[items.size()];
		ySol = new double[items.size()];
		zSol = new double[items.size()];
		lxSol = new double[items.size()];
		lySol = new double[items.size()];
		lzSol = new double[items.size()];
		wxSol = new double[items.size()];
		wySol = new double[items.size()];
		wzSol = new double[items.size()];
		hxSol = new double[items.size()];
		hySol = new double[items.size()];
		hzSol = new double[items.size()];
		aSol = new double[items.size()][items.size()];
		bSol = new double[items.size()][items.size()];
		cSol = new double[items.size()][items.size()];
		dSol = new double[items.size()][items.size()];
		eSol = new double[items.size()][items.size()];
		fSol = new double[items.size()][items.size()];
		sSol = new double[items.size()][bins.size()];

		somatorio = 0;
		for (Item item : items) {
			somatorio = somatorio + item.v;
		}

		for (int i = 0; i < bins.size(); i++) {
			nSol[i] = 1.0;
		}

		for (int i = 0; i < items.size(); i++) {
			xSol[i] = items.get(i).xO;
			ySol[i] = items.get(i).yO;
			zSol[i] = items.get(i).zO;
			lxSol[i] = 1;
			lySol[i] = 0;
			lzSol[i] = 0;
			wxSol[i] = 0;
			wySol[i] = 1;
			wzSol[i] = 0;
			hxSol[i] = 0;
			hySol[i] = 0;
			hzSol[i] = 1;

		}

		for (int i = 0; i < items.size(); i++) {
			for (int k = i + 1; k < items.size(); k++) {

				Item item1 = items.get(i);
				Item item2 = items.get(k);

				if (item1.binId == item2.binId) {

					// left or right
					if (item2.xO >= item1.xE) {
						aSol[item1.itemId][item2.itemId] = 1;
					}
					if (item1.xO >= item2.xE) {
						bSol[item1.itemId][item2.itemId] = 1;
					}
					// behind or front of
					if (item2.yO >= item1.yE) {
						cSol[item1.itemId][item2.itemId] = 1;
					}
					if (item1.yO >= item2.yE) {
						dSol[item1.itemId][item2.itemId] = 1;
					}
					// below or above
					if (item2.zO >= item1.zE) {
						eSol[item1.itemId][item2.itemId] = 1;
					}
					if (item1.zO >= item2.zE) {
						fSol[item1.itemId][item2.itemId] = 1;
					}
				}
			}
		}

		for (Bin bin : bins) {
			for (Item item : items) {
				if (item.binId == bin.binId) {
					sSol[item.itemId][bin.binId] = 1;
				} else {
					sSol[item.itemId][bin.binId] = 0;
				}
			}
		}
	}

	public void formGroups() {

		aNew = new double[items.size()][items.size()];
		bNew = new double[items.size()][items.size()];
		cNew = new double[items.size()][items.size()];
		dNew = new double[items.size()][items.size()];
		eNew = new double[items.size()][items.size()];
		fNew = new double[items.size()][items.size()];
		sNew = new double[items.size()][bins.size()];

		bins.sort(Comparator.comparing(Bin::getV).reversed());
		items.sort(Comparator.comparing(Item::getzO));

		// forma grupos de itens adjacentes que não estão no bin de menor volume

		ArrayList<ArrayList<Item>> groups = new ArrayList<ArrayList<Item>>();

		for (int i = 0; i < items.size(); i++) {
			groups.add(new ArrayList<Item>());
		}

		groups.get(items.get(0).itemId).add(items.get(0));
		// pilha
		for (Item item1 : items.subList(1, items.size() - 1)) {
			boolean added = false;
			if (item1.binId != bins.get(bins.size() - 1).binId) {
				for (int k = 0; k < items.size(); k++) {
					if (!groups.get(k).isEmpty()) {
						Item item2 = groups.get(k).get(groups.get(k).size() - 1);
						if (item1.binId == item2.binId) {
							if (item2.zE == item1.zO) {
								if ((item2.yO >= item1.yO && item2.yO < item1.yE)
										|| (item1.yO >= item2.yO && item1.yO < item2.yE)) {
									if ((item2.xO >= item1.xO && item2.xO < item1.xE)
											|| (item1.xO >= item2.xO && item1.xO < item2.xE)) {
										groups.get(k).add(item1);
										added = true;
									}
								}
							}
						}
					}
				}
				if (added == false)
					groups.get(item1.itemId).add(item1);
			}
		}

		groups.removeIf(g -> g.size() <= 1);

		items.sort(Comparator.comparing(Item::getItemId));

		// deixa os itens que estão no bin de menor volume livres

		for (int i = 0; i < items.size(); i++) {
			Item item1 = items.get(i);
			for (int k = i + 1; k < items.size(); k++) {
				Item item2 = items.get(k);
				if (item1.binId == bins.get(bins.size() - 1).binId || item2.binId == bins.get(bins.size() - 1).binId) {
					aNew[item1.itemId][item2.itemId] = -1;
					bNew[item1.itemId][item2.itemId] = -1;
					cNew[item1.itemId][item2.itemId] = -1;
					dNew[item1.itemId][item2.itemId] = -1;
					eNew[item1.itemId][item2.itemId] = -1;
					fNew[item1.itemId][item2.itemId] = -1;
				} else {
					if (item1.binId == item2.binId) {
						// left or right
						if (item2.xO >= item1.xE) {
							aNew[item1.itemId][item2.itemId] = 1;
						}
						if (item1.xO >= item2.xE) {
							bNew[item1.itemId][item2.itemId] = 1;
						}
						// behind or front of
						if (item2.yO >= item1.yE) {
							cNew[item1.itemId][item2.itemId] = 1;
						}
						if (item1.yO >= item2.yE) {
							dNew[item1.itemId][item2.itemId] = 1;
						}
						// below or above
						if (item2.zO >= item1.zE) {
							eNew[item1.itemId][item2.itemId] = 1;
						}
						if (item1.zO >= item2.zE) {
							fNew[item1.itemId][item2.itemId] = 1;
						}
					}
				}
			}
		}
		for (Item item : items) {
			if (item.binId == bins.get(bins.size() - 1).binId) {
				for (Bin bin : bins) {
					sNew[item.itemId][bin.binId] = -1;
				}
			} else {
				for (Bin bin : bins) {
					if (item.binId == bin.binId) {
						sNew[item.itemId][bin.binId] = 1;
					} else {
						sNew[item.itemId][bin.binId] = 0;
					}
				}
			}
		}

		// escolhe um grupo de itens adjacentes para deixá-los livres

		Random r = new Random();
		int num = r.nextInt(groups.size());

		ArrayList<Item> group = groups.get(num);
		group.sort(Comparator.comparing(Item::getItemId));

		for (int i = 0; i < group.size(); i++) {
			Item item1 = group.get(i);
			for (int k = item1.itemId + 1; k < items.size(); k++) {
				Item item2 = items.get(k);
				aNew[item1.itemId][item2.itemId] = -1;
				bNew[item1.itemId][item2.itemId] = -1;
				cNew[item1.itemId][item2.itemId] = -1;
				dNew[item1.itemId][item2.itemId] = -1;
				eNew[item1.itemId][item2.itemId] = -1;
				fNew[item1.itemId][item2.itemId] = -1;
			}
		}
		
		for (int i = 0; i < group.size(); i++) {
			Item item1 = group.get(i);
			for (int k = item1.itemId - 1; k >= 0; k--) {
				Item item2 = items.get(k);
				aNew[item2.itemId][item1.itemId] = -1;
				bNew[item2.itemId][item1.itemId] = -1;
				cNew[item2.itemId][item1.itemId] = -1;
				dNew[item2.itemId][item1.itemId] = -1;
				eNew[item2.itemId][item1.itemId] = -1;
				fNew[item2.itemId][item1.itemId] = -1;
			}
		}

		for (int i = 0; i < group.size(); i++) {
			Item item1 = group.get(i);
			for (int j = 0; j < bins.size(); j++) {
				sNew[item1.itemId][j] = -1;
			}
		}

		bins.sort(Comparator.comparing(Bin::getBinId));

	}
}
