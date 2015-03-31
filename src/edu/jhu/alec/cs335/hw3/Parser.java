package edu.jhu.alec.cs335.hw3;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Set;

/**
 * Imports data from files
 */
public class Parser {
	String path;
	ArrayList<String[]> data;
	/**
	 * Create new parser for the data in the given file name
	 * @param path name of the file
	 */
	public Parser(String path) {
		this.path = path;
		this.data = new ArrayList<String[]>();
	}
	
	/**
	 * Create a parser with the given data
	 * @param data the given data
	 */
	public Parser(ArrayList<String[]> data) {
		this.data = data;
	}
	
	/**
	 * Import all the data into memory
	 * @throws FileNotFoundException 
	 */
	public void download() throws FileNotFoundException {
		Scanner in = new Scanner(new File(this.path));
		String line = in.nextLine();
		while (in.hasNext()) {
			line = in.nextLine();
			if (line.split(",").length > 1)
				this.data.add(line.split(",")); 
			else {
				this.data.add(line.split(" ")); 
			}
		}
		in.close();
	}
	
	/**
	 * Get the number of rows
	 * @return number of rows
	 */
	public int numRows() {
		return this.data.size();
	}
	
	/**
	 * Get the number of columns
	 * @return number of columns
	 */
	public int numCols() {
		return this.data.get(0).length;
	}
	
	/**
	 * Get all values in the column
	 * @param col column index
	 * @return all values in column
	 */
	public String[] getCol(int col) {
		String[] temp = new String[this.data.size()];
		for (int i = 0; i < this.data.size(); i++) {
			temp[i] = this.data.get(i)[col];
		}
		return temp;
	}
	
	/**
	 * Get all values in the row
	 * @param row
	 * @return all values in row
	 */
	public String[] getRow(int row) {
		return data.get(row);
	}
	
	/**
	 * Get the element's attribute
	 * @param row element
	 * @param col attribute
	 * @return element's attribute
	 */
	public String get(int row, int col) {
		return this.data.get(row)[col];
	}
	
	/**
	 * Create a new parser filtered on the given criteria
	 * @param col feature to filter on
	 * @param val value to keep
	 * @return new parser with the filtered data
	 */
	public Parser filter(int col, String val) {
		ArrayList<String[]> arr = new ArrayList<String[]>();
		for (int i = 0; i < this.data.size(); i++) {
			if (val.equals(this.data.get(i)[col])) {
				arr.add(this.data.get(i));
			}
		}
		return new Parser(arr);
	}
	
	/**
	 * Get all unique values in a column
	 * @param col column
	 * @return all unique values
	 */
	public Set<String> getUniqueValues(int col) {
		Set<String> values = new HashSet<String>();
		for (int i = 0; i < this.data.size(); i++) {
			values.add(this.data.get(i)[col]);
		}
		return values;
	}
	
	/**
	 * Create a map of all unique values to how many times they 
	 * appear in a column
	 * @param col column
	 * @return map of value - count pairs
	 */
	public Map<String, Integer> countValueOccurrences(int col) {
		int rows = this.data.size();
		HashMap<String, Integer> valueCount = new HashMap<String, Integer>();
		//Count number of instances of each value for the attribute
		for (int i = 0; i < rows; i++) {
//			System.out.println(i + " " + col + " " + rows);
			String value = this.data.get(i)[col];
			if (valueCount.containsKey(value)) {
				valueCount.put(value, valueCount.get(value) + 1);
			} else {
				valueCount.put(value, 1);
			}
		}
		return valueCount;
	}
	
	/**
	 * Get the most common value in the column
	 * @param valueCount count of values
	 * @return most common value
	 */
	public String mostCommonValue(Map<String, Integer> valueCount) {
		Set<String> values = valueCount.keySet();
		int max = -1;
		String value = null;
		for (String val : values) {
			if (valueCount.get(val) > max) {
				max = valueCount.get(val);
				value = val;
			}
		}
		return value;
	}
	
	/**
	 * Get the most common value in the column
	 * @param col column
	 * @return most common value
	 */
	public String mostCommonValue(int col) {
		Map<String, Integer> valueCount = countValueOccurrences(col);
		Set<String> values = valueCount.keySet();
		int max = -1;
		String value = null;
		for (String val : values) {
			if (valueCount.get(val) > max) {
				max = valueCount.get(val);
				value = val;
			}
		}
		return value;
	}
	
	/**
	 * Get the ids of the attributes
	 * @param classCol class column
	 * @return ids of the attributes
	 */
	public ArrayList<Integer> getAttributeColumns(int classCol){
		ArrayList<Integer> idCols = new ArrayList<Integer>();
		for(int ii = 0; ii<data.get(0).length; ii++){
			if(ii != classCol){
				idCols.add(ii);
			}
		}
		return idCols;
	}
			
}
