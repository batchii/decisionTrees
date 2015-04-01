package edu.jhu.alec.cs335.hw3.traditional;

import java.util.Map;
import java.util.Set;

import edu.jhu.alec.cs335.hw3.Parser;

public class InfoGainTree extends TraditionalDecisionTree {

	public InfoGainTree() {
		super();
	}

	@Override
	public double fitness(int i, Parser parser, int classcol2,
			Parser originalTrainingSet, Node n) {
		Map<String, Integer> valueCount = parser.countValueOccurrences(i);


		
		
		
		
		Map<String, Integer> ValueCount = originalTrainingSet.countValueOccurrences(i);
		Set<String> values = ValueCount.keySet();
		double iG = 0; // Info Gain
		Map<String, Node> children = n.getAllClassifications();
		Set<String> childrenNames = children.keySet();
		// IG = Entropy(parent) - averageEntropy(children)
		
		double parentEntropy = 0;
		int totalNumPossible = originalTrainingSet.numRows();
		for(String value : values){
			System.out.println("ValueCount.get(Value): " + ValueCount.get(value) + "  totalNumPossible: " + totalNumPossible);
			double prop = (double) ValueCount.get(value) / totalNumPossible;
			parentEntropy -= prop * (Math.log(prop) / Math.log(2));
			System.out.println("Prop: " + prop + " and parentEntropy: " + parentEntropy);
			
			
			Map<String, Integer> children1 = parser.filter(i, value).countValueOccurrences(classcol2);
			Set<String> children1Names = children1.keySet();
			double totalNumChildrenPossible = parser.filter(i, value).numRows();
			double childEntropy = 0;
			for(String childrenName : children1Names){
				double childProp = (double) children1.get(childrenName) / totalNumChildrenPossible;
				childEntropy -= childProp * Math.log(childProp) / Math.log(2);
				System.out.println("Child Entropy: " + childEntropy);
			}
			parentEntropy -= childEntropy * totalNumChildrenPossible/totalNumPossible;
			System.out.println("Parent Entropy combined: " + parentEntropy);
			
		}
		
		
		// Calculate parent Entropy

		// The proportion of the number of elements in class x to the number of
		// elements in the set
		// Log_2(prop)
		/*double parentEntropy = 0;
		double totalWeightedChildEntropy = 0;
		for (String s : values) {
			System.out.println("ValueCount of " + s + " is: " + ValueCount.get(s));
			double prop = (double) ValueCount.get(s) / originalTrainingSet.numRows();
			System.out.println("Prop:" + prop);
			parentEntropy -= prop * (Math.log(prop) / Math.log(2));
			System.out.println("Parent Entropy: " + parentEntropy);
			
			
			Map<String, Integer> childValues = parser.countValueOccurrences(classcol2);
			Set<String> ids = childValues.keySet();
			double entropy = 0;
			for(String id : ids){
				int numRows = parser.filter(i, s).numRows();
				double childProp = (double) childValues.get(id) / numRows;
				entropy -= childProp * Math.log(childProp)/Math.log(2);
			}
			
			totalWeightedChildEntropy += entropy * ((double)ValueCount.get(s) / originalTrainingSet.numRows());
			
		}

		//Say we filter by each value in values
		//figure out the number of occurences of each over the total
		System.out.println(parentEntropy);
		System.out.println("blah " + totalWeightedChildEntropy);*/
		return parentEntropy;
	}

}
