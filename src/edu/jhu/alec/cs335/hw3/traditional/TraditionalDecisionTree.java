package edu.jhu.alec.cs335.hw3.traditional;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import edu.jhu.alec.cs335.hw3.DecisionTree;
import edu.jhu.alec.cs335.hw3.Parser;

public class TraditionalDecisionTree extends DecisionTree {

	private Parser trainParser;

	private int classCol;

	public TraditionalDecisionTree() {
		super();
	}

	public void learn(Parser trainingSet, int classCol, ArrayList<Integer> toIgnore) {
		// Take out the classColumn before building the set
		super.learn(trainingSet, classCol, toIgnore);
		ArrayList<Integer> allAttributes = new ArrayList<Integer>();
		for (int ii = 0; ii < trainingSet.numCols(); ii++) {
			if (ii != classCol && !toIgnore.contains(ii)) {
				allAttributes.add(ii);
			}
		}

		makeTree(trainingSet, trainingSet, allAttributes, classCol, this.root);

	}

	public void makeTree(Parser originalTrainingSet, Parser parser,
			ArrayList<Integer> attributes, int classcol, Node n) {
		double max = -1 * Double.MAX_VALUE;
		int attr = -1;
		// Find the column with the best gain to create a tree from
		for (int i : attributes) {
			double gain = fitness(i, parser, classcol, originalTrainingSet, n);
			if (gain > max) {
				max = gain;
				attr = i;
			}
		}
		n.setNodeName(attr);
		Set<String> values = originalTrainingSet.getUniqueValues(attr);

		ArrayList<Integer> childAttributes = new ArrayList<Integer>(attributes);
		childAttributes.remove(new Integer(attr));

		for (String s : values) {
			Node child = new Node();
			n.setClassification(s, child); // Add new item to list of classifications
			Parser filtered = parser.filter(attr, s);
			
			Map<String, Integer> valueCounts = filtered
					.countValueOccurrences(classcol);

			// Recurse
			if (filtered.numRows() == 0) {
				child.setClassType(parser.mostCommonValue(classcol));
			} else if (valueCounts.keySet().size() <= 1) {// all same class
				child.setClassType(filtered.mostCommonValue(valueCounts));
			} else if (childAttributes.isEmpty()) { // done with attributes
				child.setClassType(filtered.mostCommonValue(classcol));
			} else { // not stopping condition yet
				makeTree(originalTrainingSet, filtered, childAttributes,
						classcol, child);
			}
		}

	}



	protected double fitness(int i, Parser parser, int classcol2,
			Parser originalTrainingSet, Node n) {
		return 0;
	}

}
