package edu.jhu.alec.cs335.hw3.traditional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class Node {

	private int attr_col;

	private String[] attributesArray;

	private String classType;

	private HashMap<String, Node> classifications;

	public Node() {
		classifications = new HashMap<String, Node>();
		classType = null;
	}

	public void setClassification(String value, Node n) {

		this.classifications.put(value, n);
	}

	public Node getClassification(String value) {
		return this.classifications.get(value);
	}
	
	public HashMap<String, Node> getAllClassifications(){
		return this.classifications;
	}

	public int getNodeName() {
		return attr_col;
	}

	public void setNodeName(int nodeName) {
		this.attr_col = nodeName;
	}

	public String[] getAttributesArray() {
		return attributesArray;
	}

	public void setAttributesArray(String[] attributesArray) {
		this.attributesArray = attributesArray;
	}

	public String getClassType() {
		return this.classType;
	}

	public void setClassType(String classification) {
		this.classType = classification;
	}

	// Create a deep copy of a tree
	public Node deepCopy() {
		Node newTree = new Node();
		ArrayList<Node> originalTreeNodes = new ArrayList<Node>();
		ArrayList<Node> copiedTreeNodes = new ArrayList<Node>();

		originalTreeNodes.add(this);
		copiedTreeNodes.add(newTree);
		Node tempToAdd, tempNewTree;
		while (!originalTreeNodes.isEmpty()) {
			tempToAdd = originalTreeNodes.remove(0);
			tempNewTree = copiedTreeNodes.remove(0);
			tempNewTree.attr_col = tempToAdd.attr_col;
			tempNewTree.classType = tempToAdd.classType;
			Set<String> keys = tempToAdd.classifications.keySet();

			for (String key : keys) {
				Node child = new Node();
				tempNewTree.setClassification(key, child);
				copiedTreeNodes.add(child);
				originalTreeNodes.add(tempToAdd.getClassification(key));
			}
		}
		return newTree;

	}

}
