package DBCE.RepeatMap;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

import DBCE.RepeatMap.diff_match_patch.Diff;
public class RepeatLineMap {
	private HashMap<String, Integer> RepeatMap = new HashMap<>();
	private LinkedList<Diff> dlist;
	
	public RepeatLineMap(){
	}
	
	public void Resize(int weight){
		HashMap<String, Integer> res = new HashMap<>();
		
		for(Entry<String, Integer> e : RepeatMap.entrySet()){
			if(e.getValue() >= weight){
				res.put(e.getKey(), e.getValue());
			}
		}
		this.RepeatMap=res;
	}
	
	public void UpdateMap(String s1, String s2){
		
		diff_match_patch dmp = new diff_match_patch();
<<<<<<< HEAD
		dlist = dmp.diff_main(s1.replaceAll("[0-9]", "0"), s2.replaceAll("[0-9]", "0"));
		dmp.diff_cleanupSemantic(dlist);
		
		LinkedList<Diff> equal_list = new LinkedList<>();
		for(int i=0; i<dlist.size(); i++){
			if(dlist.get(i).operation.toString().equals("EQUAL") && dlist.get(i).text.contains("\n")){
				equal_list.add(dlist.get(i));
			}
		}
		
		for (int i = 0; i < equal_list.size(); i++) {
			for (String s : equal_list.get(i).text.trim().split("\n")) {
				if (s.length() > 5) {
					if (RepeatMap.containsKey(s)) {
						RepeatMap.put(s, RepeatMap.get(s) + 1);
					} else {
						RepeatMap.put(s, 1);
					}
				}
			}
		}
	}
	
	public HashMap<String, Integer> getRepeatMap(){
		return this.RepeatMap;
	}
	
	public String refineByMap(String origin){
		String res = "";
		for(String line : origin.split("\n")){
			if(!RepeatMap.containsKey(line.replaceAll("[0-9]", "0"))){
				res += line+"\n";
			}else{
				System.out.println("[Msg]"+line+" Removed!");
=======
		dlist = dmp.diff_main(s1, s2);
		dmp.diff_cleanupSemantic(dlist);
		
		LinkedList<Diff> equal_list = new LinkedList<>();
		for(int i=0; i<dlist.size(); i++){
			if(dlist.get(i).operation.toString().equals("EQUAL") && dlist.get(i).text.contains("\n")){
				equal_list.add(dlist.get(i));
			}
		}
		
		for (int i = 0; i < equal_list.size(); i++) {
			for (String s : equal_list.get(i).text.trim().split("\n")) {
				if (s.length() > 5) {
					if (RepeatMap.containsKey(s)) {
						RepeatMap.put(s, RepeatMap.get(s) + 1);
					} else {
						RepeatMap.put(s, 1);
					}
				}
			}
		}
	}
	
	public HashMap<String, Integer> getRepeatMap(){
		return this.RepeatMap;
	}
	
	public String refineByMap(String origin){
		String res = "";
		for(String line : origin.split("\n")){
			if(!RepeatMap.containsKey(line)){
				res += line+"\n";
			}else{
				System.out.println(line);
>>>>>>> branch 'develop_1' of https://github.com/rlatmfrl24/Extract.git
			}
		}
		return res;
	}
}