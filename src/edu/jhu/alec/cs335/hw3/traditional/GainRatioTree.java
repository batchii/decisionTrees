package edu.jhu.alec.cs335.hw3.traditional;

import java.util.Map;
import java.util.Set;

import edu.jhu.alec.cs335.hw3.Parser;

public class GainRatioTree extends TraditionalDecisionTree{
	
	public GainRatioTree(){
		super();
	}
	
	@Override
	public double fitness(int i, Parser parser, int classcol2,
			Parser originalTrainingSet, Node n){
		// Gain Ratio calculation
		Map<String, Integer> ValueCount = originalTrainingSet.countValueOccurrences(i);
		Set<String> values = ValueCount.keySet();
		double iG = 0; // Info Gain
		Map<String, Node> children = n.getAllClassifications();
		Set<String> childrenNames = children.keySet();
		// IG = Entropy(parent) - averageEntropy(children)
		
		double parentEntropy = 0;
		int totalNumPossible = originalTrainingSet.numRows();
		double weightedAverageChildEntropy = 0;
		for(String value : values){
			double prop = (double) ValueCount.get(value) / totalNumPossible;
			parentEntropy -= prop * (Math.log(prop) / Math.log(2));
			
			
			Map<String, Integer> children1 = parser.filter(i, value).countValueOccurrences(classcol2);
			Set<String> children1Names = children1.keySet();
			double totalNumChildrenPossible = parser.filter(i, value).numRows();
			double childEntropy = 0;
			for(String childrenName : children1Names){
				double childProp = (double) children1.get(childrenName) / totalNumChildrenPossible;
				childEntropy -= childProp * Math.log(childProp) / Math.log(2);
			}
			parentEntropy -= childEntropy * totalNumChildrenPossible/totalNumPossible;
			weightedAverageChildEntropy += childEntropy * totalNumChildrenPossible/totalNumPossible;
			
		}
		
		//Adjusted 
		return parentEntropy/weightedAverageChildEntropy;
	}

}
