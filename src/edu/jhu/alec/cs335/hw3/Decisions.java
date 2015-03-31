package edu.jhu.alec.cs335.hw3;

import java.io.FileNotFoundException;
import java.util.Scanner;

import edu.jhu.alec.cs335.hw3.traditional.GainRatioTree;
import edu.jhu.alec.cs335.hw3.traditional.InfoGainTree;
import edu.jhu.alec.cs335.hw3.traditional.TraditionalDecisionTree;

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
			System.out.println("Proper usage: \"Training filename\"  \"Test filename\" ");
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
	
		trainParser.download();
		tree = new InfoGainTree();
		tree.learn(trainParser, classCol);
		setParser.download();
		tree.decide(setParser, classCol);
		
	}

}
