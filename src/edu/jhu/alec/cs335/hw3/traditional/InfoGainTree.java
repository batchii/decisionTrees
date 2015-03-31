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
			Parser originalTrainingSet) {
		// Gain Ratio calculation
		Map<String, Integer> ValueCount = parser
				.countValueOccurrences(classcol2);
		Set<String> values = ValueCount.keySet();
		double iG = 0; // Info Gain
		

		// The proportion of the number of elements in class x to the number of
		// elements in the set
		// Log_2(prop)
		for (String s : values) {
			double prop = (double) ValueCount.get(s) / parser.numRows();
			iG += prop * prop * Math.log(prop) / Math.log(2);
		}
		System.out.println("IG" + iG);
		return iG;
	}

}
