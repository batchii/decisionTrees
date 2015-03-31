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
			Parser originalTrainingSet){
		// Gain Ratio calculation
				Map<String, Integer> origValueCount = originalTrainingSet
						.countValueOccurrences(classcol2);
				Set<String> values = origValueCount.keySet();
				double entropy = 0;

				// The proportion of the number of elements in class x to the number of
				// elements in the set
				// Log_2(prop)
				for (String s : values) {
					double prop = (double) origValueCount.get(s) / originalTrainingSet.numRows();
					entropy -= prop * Math.log(prop) / Math.log(2);
				}

				// This will change as more things get removed
				Map<String, Integer> valueCount = parser
						.countValueOccurrences(classcol2);

				values = valueCount.keySet();
				double gain = 0;
				double value = 0;

				for (String s : values) {
					double prop = (double) valueCount.get(s) / parser.numRows();
					double temp = prop * Math.log(prop) / Math.log(2);
					value -= temp;					
					gain -= temp * prop;
				}
				return (double)(entropy - gain) / value;
	}

}
