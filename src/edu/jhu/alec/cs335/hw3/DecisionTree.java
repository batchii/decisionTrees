package edu.jhu.alec.cs335.hw3;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import edu.jhu.alec.cs335.hw3.traditional.Node;

public abstract class DecisionTree {
	protected Node root;
	
	protected Map<String, Integer> classes;

	public DecisionTree() {
		root = new Node();
	}

	public void learn(Parser parser, int classCol, ArrayList<Integer> toIgnore) {
		this.classes = parser.countValueOccurrences(classCol);
	}

	public void decide(Parser testSet, int classCol) {
		int numCorrect = 0;
		int totalPossible = testSet.numRows();

		// generate confusion matrix
		Map<String, Integer> valueCounts = testSet
				.countValueOccurrences(classCol);
		System.out.println(testSet.getCol(classCol)[0]);
		System.out.println(valueCounts.size());
		// guessed class is row, actual is col
		double[][] confusionMatrix = new double[valueCounts.size()][valueCounts
				.size()];
		ArrayList<String> keys = new ArrayList<String>(valueCounts.keySet());

		for (int i = 0; i < totalPossible; i++) {
			confusionMatrix[keys.indexOf(decideCol(testSet.getRow(i)))][keys
					.indexOf(testSet.get(i, classCol))]++;
		}

		// Print confusion matrix
		System.out.print("Confusion Matrix: \n     ");
		for (int ii = 0; ii < keys.size(); ii++) {
			System.out.print(keys.get(ii) + "    ");
		}
		System.out.println();
		for (int i = 0; i < confusionMatrix.length; i++) {
			System.out.print(keys.get(i) + " ");
			for (int j = 0; j < confusionMatrix.length; j++) {
				if (i == j) {
					numCorrect += confusionMatrix[i][j];
				}
				System.out.print(confusionMatrix[i][j] + " ");
			}
			System.out.println();
		}
		// Recall and precision
		// Recall = true positives/ total Positives
		double precision = confusionMatrix[0][0]
				/ (confusionMatrix[0][0] + confusionMatrix[0][1]);
		double recall = confusionMatrix[0][0]
				/ (confusionMatrix[0][0] + confusionMatrix[1][0]);

		double accuracy = (double) numCorrect / totalPossible;
		System.out.println("Precision is: " + precision + " \nRecall is: "
				+ recall + "\nAccuracy is: " + accuracy);

	}

	// Figures out the classification
	public String decideCol(String[] row) {
		Node current = this.root;
		
		while (current.getClassType() == null) {
			if( current.getClassification(row[current.getNodeName()])== null){
				Map.Entry<String, Integer> maxEntry = null;
				for (Map.Entry<String, Integer> entry : classes.entrySet())
				{
				    if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0)
				    {
				        maxEntry = entry;
				    }
				}
				return maxEntry.getKey();
			} else {
				current = current.getClassification(row[current.getNodeName()]);
			}
			
			
		}
		
		return current.getClassType();
	}
}
