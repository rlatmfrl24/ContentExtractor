package DBCE.Filter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

import DBCE.Utility.diff_match_patch;
import DBCE.Utility.diff_match_patch.Diff;
public class RepeatLineMap {
	private HashMap<String, Integer> RepeatMap = new HashMap<>();
	private LinkedList<Diff> dlist;
	
	public void refineByMap(String input_path, String output_path) {
		String content = "";
		String before = "";
		File output_file = new File(output_path);
		if(!output_file.exists()){
			output_file.mkdirs();
		}
		
		try{
			File f = new File(input_path);
			for (File rf : f.listFiles()) {
				if (rf.isFile()) {
					BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(rf), StandardCharsets.UTF_8));
					StringBuilder str = new StringBuilder();
					char[] c = new char[(int)rf.length()];
					br.read(c);
					str.append(c);
					content = str.toString();
					br.close();
					System.out.println("=> Find Repeat Phreas By "+rf.getName());
					UpdateMap(before, content);
					before = content;
					content = "";
					br.close();
				}
			}
			Resize(2);
			System.out.println(getRepeatMap());
			
			for (File rf : f.listFiles()) {
				if (rf.isFile()) {
					BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(rf), StandardCharsets.UTF_8));
					BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(output_path + rf.getName())), StandardCharsets.UTF_8));
					StringBuilder str = new StringBuilder();
					char[] c = new char[(int)rf.length()];
					br.read(c);
					str.append(c);
					content = str.toString();
					br.close();
					System.out.println(rf.getName());
					bw.write(refine(content, true));
					bw.flush();
					bw.close();
					content = "";
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void subDirList(String source){
		File dir = new File(source); 
		File[] fileList = dir.listFiles();
		
		try{
			for(int i = 0 ; i < fileList.length ; i++){
				File file = fileList[i]; 
				if(file.isFile()){
					file.delete();
				}else if(file.isDirectory()){
					subDirList(file.getCanonicalPath().toString()); 
					file.delete();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}

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
				if (s.length() > 2) {
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
	
	public String refine(String origin, boolean reverseOption) {
		String res = "";
		if (reverseOption) {
			for (String line : origin.split("\n")) {
				boolean isCon = true;
				for(String key : RepeatMap.keySet()){
					if(line.replaceAll("[0-9]", "0").contains(key.trim())) {
						System.out.println("[Msg]" + line + " Removed!");
						isCon=false;
						break;
					}
				}
				if(isCon) res += line + "\n";
			}
		} else {
			for (String line : origin.split("\n")) {
				if (!RepeatMap.containsKey(line.replaceAll("[0-9]", "0"))) {
					res += line + "\n";
				} else {
					System.out.println("[Msg]" + line + " Removed!");
				}
			}
		}
		return res.trim();
	}
}