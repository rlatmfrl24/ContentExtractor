package DBCE.Evaluation;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class SørensenDiceMain {
	 private static final Pattern SPACE_REG = Pattern.compile("\\s+");
	 private static int n = 3;
	 
	  public static Map<String, Integer> getNgram(final String string) {
	        HashMap<String, Integer> shingles = new HashMap<String, Integer>();

	        String string_no_space = SPACE_REG.matcher(string).replaceAll(" ");
	        for (int i = 0; i < (string_no_space.length() - n + 1); i++) {
	            String shingle = string_no_space.substring(i, i + n);
	            Integer old = shingles.get(shingle);
	            if (old!=null) {
	                shingles.put(shingle, old + 1);
	            } else {
	                shingles.put(shingle, 1);
	            }
	        }

	        return Collections.unmodifiableMap(shingles);
	    }
	
	 public static double similarity(final String s1, final String s2) {

	        if (s1.equals(s2)) {
	            return 1;
	        }

	        Map<String, Integer> str1 = getNgram(s1);
	        Map<String, Integer> str2 = getNgram(s2);

	        Set<String> union = new HashSet<String>();
	        union.addAll(str1.keySet());
	        union.addAll(str2.keySet());

	        int inter = 0;

	        for (String key : union) {
	            if (str1.containsKey(key) && str2.containsKey(key)) {
	                inter++;
	              
	            }
	        }
	        
	        return 2.0 * inter / (str1.size() + str2.size());
	    }
	 
	 
	public static void main(String[] args) throws Exception {
		
		
		BufferedReader a = new BufferedReader(new InputStreamReader(new FileInputStream("F:/dataset/nytimes/gold/3.txt"), "UTF8"));
		BufferedReader b = new BufferedReader(new InputStreamReader(new FileInputStream("F:/test/nytimes/3.txt"), "UTF8"));
	
		String c = "";
		String d = "";
		String tmp = "";
		
		while((tmp=a.readLine())!=null) c+=tmp;
		while((tmp=b.readLine())!=null) d+=tmp;
				
		System.out.println(c);
		System.out.println(d);

		similarity(c, d);
 
        System.out.println("result : " + similarity(c, d) * 100);
        a.close();
        b.close();
    }
 
	

}
