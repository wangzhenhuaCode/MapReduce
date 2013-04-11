package mapreduce.node.logic;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


import mapreduce.sdk.OutputCollector;
import mapreduce.sdk.OutputFormat;
import mapreduce.sdk.Writable;

public class OutputCollection<K extends Writable, V> implements OutputCollector<K, V>, Serializable {
	
	class Entry<K,V> implements Serializable{
		K key;
		V value;
		public Entry(K key, V value) {
			
			this.key = key;
			this.value = value;
		}
		
		
	}
	private ArrayList<Entry<K,V>> outputList;
	@Override
	public void collect(K key, V value) throws IOException {
		Entry<K,V> entry=new Entry<K,V>(key,value);
		outputList.add(entry);
		
	}
	public OutputCollection() {
		outputList=new ArrayList<Entry<K,V>>();
	}
	public void sort(){
		Collections.sort(outputList,new Comparator<Entry<K,V>>(){

			@Override
			public int compare(Entry<K, V> arg0, Entry<K, V> arg1) {
				return arg0.key.compareTo(arg1.key);
				
			}
			
			
		});
	}
	public void serialize(String path)throws FileNotFoundException, IOException{
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path));
		out.writeObject(outputList);
		out.close();
	}
	public void output(String path, OutputFormat output) throws IOException {
		FileOutputStream out=new FileOutputStream(path);
		for(Entry<K,V> e:outputList){
			output.write(e.key, e.value, out);
		}
		out.close();
	}
	public OutputCollection(ArrayList<Entry<K, V>> outputList) {
		super();
		this.outputList = outputList;
	}
	public void add(String path) throws IOException, ClassNotFoundException{
		ObjectInputStream in = new ObjectInputStream(new FileInputStream(path));
		outputList.addAll((ArrayList<Entry<K, V>>) in.readObject());
		in.close();
	}
	public List<Entry<K, V>> getOutputList() {
		return outputList;
	}


}
