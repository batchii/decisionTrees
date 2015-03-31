package edu.jhu.alec.cs335.hw3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;
import java.util.Set;

import edu.jhu.alec.cs335.hw3.traditional.Node;

public class GeneticTree extends DecisionTree {

	private Parser parser;

	private int classCol;

	private Random rand;

	final int NUM_TREES = 100;

	final int SELECTION_SIZE = 20;

	final double MUTATION_RATE = .01;

	final double CROSSOVER_RATE = .80;

	final int NUM_ITERATIONS = 1000;

	public GeneticTree() {
		super();
		rand = new Random();
	}

	// Do all the work
	// Create trees
	// Choose good trees
	// Redo as many times
	public void learn(Parser parser, int classCol) {
		this.parser = parser;
		this.classCol = classCol;
		// Need an array of trees
		ArrayList<Node> trees = makeRandomTrees(this.parser, this.classCol,
				this.NUM_TREES);

		ArrayList<Double> pastTestFitnesses = new ArrayList<Double>();

		// Start the mutations and crossovers
		int generation = 0;
		while (generation < NUM_ITERATIONS) {
			// Get the best trees from the list and do some genetics on them
			trees = getBest(trees, this.parser, this.classCol, SELECTION_SIZE);
			// Add the accuracy of the best tree to this array
			pastTestFitnesses.add(fitness(trees.get(0), parser, classCol));
			// Check if the past tests have been relatively the same
			if (pastTestFitnesses.size() >= 10) {

				if (stdev(pastTestFitnesses) > 1) {
					break;
				} else {
					pastTestFitnesses.remove(0);
				}
			}

			// Create a new set of children trees
			for (int ii = 0; ii < SELECTION_SIZE; ii++) {
				Node current = trees.get(ii);
				Node temp = current.deepCopy();

				Boolean changed = false;
				// Mutate
				if (rand.nextDouble() < MUTATION_RATE) {
					// do stuff
					mutate(temp, this.parser, classCol);
					changed = true;
				}
				// Crossover
				if (rand.nextDouble() < CROSSOVER_RATE) {
					// Do other stuff
					crossover(temp, trees.get(rand.nextInt(SELECTION_SIZE)));
				}
				// Add to list if mutated as new tree
				if (changed) {
					trees.add(temp);
				}

			}
			this.root = trees.get(0);
			generation++;
		}

	}

	private double stdev(ArrayList<Double> pastTestFitnesses) {
		double mean = 0;
		for (int ii = 0; ii < pastTestFitnesses.size(); ii++) {
			mean += pastTestFitnesses.get(ii);
		}
		mean = mean / pastTestFitnesses.size();

		double sd = 0;
		for (int ii = 0; ii < pastTestFitnesses.size(); ii++) {
			sd = sd + Math.pow(pastTestFitnesses.get(ii) - mean, 2);
		}

		return sd;
	}

	// Specify number of trees
	// Which column is the classifier
	// Return a list of random trees
	private ArrayList<Node> makeRandomTrees(Parser parser, int classCol, int n) {
		String[] classes = parser.getUniqueValues(classCol).toArray(
				new String[0]);
		ArrayList<Node> trees = new ArrayList<Node>(n);
		ArrayList<Integer> treeAttributes = parser
				.getAttributeColumns(classCol); // Get non class columns
		LinkedList<Node> queue = new LinkedList<Node>();
		// Make some random trees bby
		for (int ii = 0; ii < n; ii++) {
			Node current = new Node();
			trees.add(current);
			queue.add(trees.get(ii));
			// Grow a tree from this node
			int depth = 0;
			while (!queue.isEmpty()) {
				depth++;
				Node temp = queue.remove();
				// Pick an attribute for the next level
				// Make sure attribute has not been picked and is not the
				// classCol
				int attribute = treeAttributes.get(rand.nextInt(treeAttributes
						.size()));
				temp.setNodeName(attribute);
				Set<String> values = parser.getUniqueValues(attribute);
				for (String i : values) {
					Node child = new Node();
					temp.setClassification(i, child);
					// Odds of this node being a leaf node is 60%
					if (rand.nextDouble() < .4
							&& depth < treeAttributes.size() / 2) {
						queue.add(child);
					} else {
						// Set the classification of this node
						child.setClassType(classes[rand.nextInt(classes.length)]);
					}
				}
				System.out.println("Number of children " + temp.getAllClassifications().size());
				System.out.println("Should have: " + values.size());
			}

		}
		return trees;
	}

	// Measure ability of each tree to be accurate
	private double fitness(Node n, Parser p, int classCol2) {
		int totalCorrect = 0;
		int numRows = p.numRows();
		for (int ii = 0; ii < numRows; ii++) {
			String guessedClass = decideClass(n, p.getRow(ii));
			if (guessedClass.equals(p.getRow(ii)[classCol2])) {
				totalCorrect++;
			}
		}
		return ((double) totalCorrect) / numRows;
	}

	private String decideClass(Node n, String[] row) {
		Node temp = n;
		while (temp.getClassType() == null) {

			temp = temp.getClassification(row[temp.getNodeName()]);

		}
		return temp.getClassType();

	}

	// Swap subtrees
	private void crossover(Node tree1, Node tree2) {

		// Iterate down tree 1 randomly
		ArrayList<String> tree1Path = getRandomPath(tree1);
		ArrayList<String> tree2Path = getRandomPath(tree2);

		int random1Depth = rand.nextInt(tree1Path.size());
		int random2Depth = rand.nextInt(tree2Path.size());

		Node temp1 = tree1;
		Node temp2 = tree2;

		int depth = 0;
		for (int ii = 0; ii < random1Depth; ii++) {
			temp1 = temp1.getClassification(tree1Path.get(ii));
		}

		depth = 0;
		for (int ii = 0; ii < random2Depth; ii++) {
			temp2 = temp2.getClassification(tree2Path.get(ii));
		}

		// Swap
		temp1.setClassification(tree1Path.get(random1Depth),
				temp2.getClassification(tree2Path.get(random2Depth)));

	}

	private ArrayList<String> getRandomPath(Node tree) {
		Node current = tree;
		ArrayList<String> path = new ArrayList<String>();
		while (current.getClassType() == null) {
			String[] keys = current.getAllClassifications().keySet()
					.toArray(new String[0]);
			String key = keys[rand.nextInt(keys.length)];
			path.add(key);
			current = current.getClassification(key);
		}

		return path;
	}

	// Mutate
	private void mutate(Node tree1, Parser p, int classCol) {
		ArrayList<String> path = getRandomPath(tree1);
		Node current = tree1;
		Parser parser = p;
		Parser filtered = null;
		// Flip a coin, insert? or delete?
		if (rand.nextDouble() < .5) { // Insert
			for (int i = 0; i < path.size(); i++) {
				filtered = parser.filter(current.getNodeName(), path.get(i));
				if (filtered.numRows() != 0) {
					// If there are still items around with given filter then
					// set filtered to parser
					parser = filtered;
				}
				current = current.getClassification(path.get(i)); 
			}
			// reset the decision made by the node
			current.setClassType(null);
			//Pick a new identity for the node
			int newAttr = rand.nextInt(p.numCols());
			//Make sure it's not the class col
			while(newAttr == classCol){
				newAttr = rand.nextInt(p.numCols());
			}
			//Create leaves
			current.setNodeName(newAttr);
			Set<String> values = p.getUniqueValues(newAttr);
			for(String v : values){
				filtered = p.filter(newAttr, v);
				Node leaf = new Node();
				if(filtered.numRows() != 0){ 
					//"Democrat/Republican", which has more?
					leaf.setClassType(filtered.mostCommonValue(classCol));
				} else {
					//"If nothing exists it must be the other one
					leaf.setClassType(p.mostCommonValue(classCol));
				}
				current.setClassification(v, leaf);
			
				
			}
		} else { // Delete
			int depth = rand.nextInt(path.size());
			
			for(int ii = 0; ii <= depth; ii++){
				filtered = parser.filter(tree1.getNodeName(), path.get(ii));
				if(filtered.numRows() != 0){
					parser = filtered;
				}
				tree1 = tree1.getClassification(path.get(ii));
			}
			tree1.setClassType(parser.mostCommonValue(classCol));
		}

	}

	private ArrayList<Node> getBest(ArrayList<Node> generation, Parser p,
			int classCol, int selectionSize) {
		HashMap<Node, Double> map = new HashMap<Node, Double>();
		for (Node n : generation) {
			map.put(n, new Double(fitness(n, p, classCol)));
		}
		ArrayList<Double> fitnessValues = new ArrayList<Double>(map.values());
		Collections.sort(fitnessValues);
		ArrayList<Node> finalSet = new ArrayList<Node>();
		int count = 0;
		// Choose the best n
		for (int ii = fitnessValues.size() - 1; ii >= 0; ii--) {
			// Skim the best values from the bottom of the list
			for (Node value : generation) {
				if (map.get(value).equals(fitnessValues.get(ii))) {
					if (count >= selectionSize) {
						return finalSet;
					}
					finalSet.add(value);
					count++;
				}
			}
		}

		return finalSet;
	}
}
