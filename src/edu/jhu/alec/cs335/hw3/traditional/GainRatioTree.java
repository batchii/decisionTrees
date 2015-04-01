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
				Map<String, Integer> ValueCount = parser.countValueOccurrences(i);
				Set<String> values = ValueCount.keySet();
				double iG = 0; // Info Gain
				Map<String, Node> children = n.getAllClassifications();
				Set<String> childrenNames = children.keySet();
				System.out.println(childrenNames.size());
				// IG = Entropy(parent) - averageEntropy(children)

				// Calculate parent Entropy

				// The proportion of the number of elements in class x to the number of
				// elements in the set
				// Log_2(prop)
				double parentEntropy = 0;
				double totalWeightedChildEntropy = 0;
				for (String s : values) {
					double prop = (double) ValueCount.get(s) / parser.numRows();
					parentEntropy -= prop * Math.log(prop) / Math.log(2);
					Map<String, Integer> childValues = parser.countValueOccurrences(classcol2);
					Set<String> ids = childValues.keySet();
					double entropy = 0;
					for(String id : ids){
						int numRows = parser.filter(i, s).numRows();
						double childProp = (double) childValues.get(id) / numRows;
						entropy -= childProp * Math.log(childProp)/Math.log(2);
					}
					
					totalWeightedChildEntropy += entropy * ((double)ValueCount.get(s) / parser.numRows());
					
				}
				values = ValueCount.keySet();
				double gain = 0;
				double value = 0;
				for (String val : values) {
					double prob = ((double) ValueCount.get(val))/parser.numRows(); 
					double temp = Math.log(prob)/Math.log(2) * prob;//term shared by both
					value -= temp;
					gain -= temp * prob;
					
				}

				
				//Say we filter by each value in values
				//figure out the number of occurences of each over the total

				//Gain Ratio = InformationGain / Entropy
				double val = (parentEntropy - totalWeightedChildEntropy);
				System.out.println(val);
				return (parentEntropy - totalWeightedChildEntropy)/value;
	}

}
