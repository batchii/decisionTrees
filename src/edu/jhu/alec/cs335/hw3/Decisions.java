package edu.jhu.alec.cs335.hw3;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import edu.jhu.alec.cs335.hw3.traditional.GainRatioTree;
import edu.jhu.alec.cs335.hw3.traditional.InfoGainTree;

/**
 * Driver class for Decision Tree program; see README for description and terms
 * of use
 * 
 * @author Alec Tabatchnick
 */
public class Decisions {
	// 1. Data
	public static void main(String[] args) throws FileNotFoundException {
		String trainingSet = null, testSet = null;
		if (args.length < 2 || args.length > 2) {
			System.out
					.println("Proper usage: \"Training filename\"  \"Test filename\" ");
			System.exit(1);
		} else {
			trainingSet = args[0];
			testSet = args[1];
		}
		System.out.println("Please enter the column index with the class");
		Scanner sc = new Scanner(System.in);
		int classCol = Integer.parseInt(sc.next());
		// Import data
		Parser trainParser = new Parser(trainingSet);
		Parser setParser = new Parser(testSet);
		DecisionTree tree = null;
		int choice = 0;
		while (choice > 3 || choice < 1) {
			System.out
					.println("Please enter the type of tree you would like. "
							+ "(1 = Information Gain Tree, 2 = Gain Ratio Tree, 3 = Evolutionary Tree");
			choice = Integer.parseInt(sc.next());
		}
		
		ArrayList<Integer> columnsToIgnore = new ArrayList<Integer>();
		int toIgnore = 0;
		while(toIgnore != -1){
			System.out.println("Please enter any columns to ignore (-1 when finished)");
			toIgnore = Integer.parseInt(sc.next());
			if(toIgnore == -1){
				break;
			}
			columnsToIgnore.add(toIgnore);
		}

		switch (choice) {
		case 1:
			tree = new InfoGainTree();
			break;
		case 2:
			tree = new GainRatioTree();
			break;
		case 3:
			tree = new GeneticTree();
			break;
		}

		
		trainParser.download();

		long startTime = System.currentTimeMillis();
		tree.learn(trainParser, classCol, columnsToIgnore);
		long time = System.currentTimeMillis() - startTime;
		System.out.println("Made tree in: " + time + "ms");
		
		setParser.download();

		
		startTime = System.currentTimeMillis();
		tree.decide(setParser, classCol);
		time = System.currentTimeMillis() - startTime;
		System.out.println("Made all decisions in: " + time + "ms");
	}

}
